import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import jasbro.game.character.AgeProgressionData;
import jasbro.game.character.Charakter;
import jasbro.game.character.CharacterBase;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * EMPIRICAL TEST: how does XStream 1.4.7 handle AgeProgressionData's WeakReference fields,
 * in the two situations that actually occur in a real save?
 *
 * Flagged as an open question by the character-lifecycle audit because no golden fixture
 * contains a character with ageProgressionData.
 *
 * Two cases, both of which occur in real play:
 *   CASE 1 — the mother is ALSO a character in data.characters (the normal case).
 *            Does the weak reference become a proper XStream reference to that same instance,
 *            or an inline COPY (which would silently duplicate the character on load)?
 *   CASE 2 — the mother has been garbage-collected (the reference is cleared).
 *            What gets written, and does it still load?
 *
 * Usage: java -cp <game.jar>;<libs>;[outdir] AgeProgressionFixture
 */
public class AgeProgressionFixture {

    private static final XStream XS = makeXStream();

    private static XStream makeXStream() {
        XStream x = new XStream(new StaxDriver());
        x.autodetectAnnotations(true);
        return x;
    }

    /** A character with a base so getName() is meaningful. */
    private static Charakter makeChar(String id, String name) {
        CharacterBase b = new CharacterBase();
        b.setId(id);
        b.setName(name);
        Charakter c = new Charakter(b);
        c.setName(name);
        return c;
    }

    /**
     * Sets the ageProgressionData field directly. Reflection is used because the field is the
     * persisted state and the public surface is not the subject of this test.
     */
    private static void setAgeData(Charakter c, AgeProgressionData d) {
        try {
            java.lang.reflect.Field f = Charakter.class.getDeclaredField("ageProgressionData");
            f.setAccessible(true);
            f.set(c, d);
        } catch (Exception e) {
            throw new RuntimeException("could not set ageProgressionData: " + e, e);
        }
    }

    private static AgeProgressionData getAgeData(Charakter c) {
        try {
            java.lang.reflect.Field f = Charakter.class.getDeclaredField("ageProgressionData");
            f.setAccessible(true);
            return (AgeProgressionData) f.get(c);
        } catch (Exception e) {
            throw new RuntimeException("could not read ageProgressionData: " + e, e);
        }
    }

    private static AgeProgressionData makeAgeData(Charakter mother, Charakter father) {
        AgeProgressionData d = new AgeProgressionData();
        d.setInfantBase("InfantBase");
        d.setNameMother("Mother-Name");
        d.setNameFather("Father-Name");
        d.setMother(mother == null ? null : new WeakReference<>(mother));
        d.setFather(father == null ? null : new WeakReference<>(father));
        return d;
    }

    public static void main(String[] args) throws Exception {
        case1_motherAlsoACharacter(args);
        case2_motherGarbageCollected();
    }

    // ------------------------------------------------------------------ CASE 1
    private static void case1_motherAlsoACharacter(String[] args) throws Exception {
        System.out.println("############ CASE 1 — mother is ALSO in the characters list ############");

        Charakter mother = makeChar("MotherBase", "Mother");
        Charakter child = makeChar("ChildBase", "Child");
        setAgeData(child, makeAgeData(mother, null));

        List<Charakter> characters = new ArrayList<>();
        characters.add(mother);   // strong ref, and serialised first
        characters.add(child);

        String xml = XS.toXML(characters);

        // Emit the golden fixture when asked. This is the ONLY fixture exercising an UNINDEXED
        // reference whose final path segment is a bare type name — the form a real save uses
        // whenever a character carries ageProgressionData.
        if (args.length >= 1) {
            java.io.PrintStream out =
                    new java.io.PrintStream(new java.io.FileOutputStream(args[0]), true, "UTF-8");
            out.print(xml);
            out.close();
            System.out.println("wrote " + args[0]);
        }

        System.out.println("--- does the child's weak reference become an XStream reference? ---");
        int ageIdx = xml.indexOf("AgeProgressionData");
        String tail = ageIdx >= 0 ? xml.substring(ageIdx) : xml;
        System.out.println(tail);
        System.out.println();
        System.out.println("contains reference=\"...\": " + tail.contains("reference="));
        System.out.println("contains inline referent    : " + tail.contains("<referent"));

        // Round-trip and count distinct Charakter instances.
        @SuppressWarnings("unchecked")
        List<Charakter> back = (List<Charakter>) XS.fromXML(xml);
        Charakter motherBack = back.get(0);
        Charakter childBack = back.get(1);
        Charakter viaWeak = getAgeData(childBack) == null || getAgeData(childBack).getMother() == null
                ? null : getAgeData(childBack).getMother().get();

        System.out.println();
        System.out.println("--- after round trip ---");
        System.out.println("characters in list        : " + back.size());
        System.out.println("weak target resolved      : " + (viaWeak != null));
        System.out.println("weak target name          : " + (viaWeak == null ? "<null>" : viaWeak.getName()));
        System.out.println("SAME INSTANCE as list[0]  : " + (viaWeak == motherBack));
        System.out.println();
        if (viaWeak == motherBack) {
            System.out.println("VERDICT: XStream restores the weak reference to the SAME instance. Parentage is");
            System.out.println("         preserved by identity, and the port must resolve it the same way.");
        } else if (viaWeak != null) {
            System.out.println("VERDICT: the weak reference is restored to a SEPARATE COPY of the mother.");
            System.out.println("         Identity is NOT preserved — the child's mother is a different object");
            System.out.println("         from the mother in the character list.");
        } else {
            System.out.println("VERDICT: the weak reference target did NOT survive.");
        }
        System.out.println();
    }

    // ------------------------------------------------------------------ CASE 2
    private static void case2_motherGarbageCollected() {
        System.out.println("############ CASE 2 — mother has been garbage-collected ############");

        AgeProgressionData data;
        {
            // Scoped so nothing holds a strong reference once the block exits.
            Charakter transientMother = makeChar("MotherBase", "TransientMother");
            data = makeAgeData(transientMother, null);
        }
        // Force collection so the weak reference is actually cleared.
        for (int i = 0; i < 5; i++) {
            System.gc();
            try { Thread.sleep(30); } catch (InterruptedException ignored) { }
        }

        boolean cleared = data.getMother() == null || data.getMother().get() == null;
        System.out.println("weak reference cleared before save: " + cleared);
        System.out.println("nameMother still readable         : " + data.getNameMother());

        String xml;
        try {
            xml = XS.toXML(data);
        } catch (Throwable t) {
            System.out.println("toXML THREW: " + t.getClass().getName() + ": " + t.getMessage());
            System.out.println();
            System.out.println("VERDICT: saving a character whose parent has been collected THROWS.");
            System.out.println("         The String nameMother/nameFather fields are the only durable record.");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println("--- XML ---");
        System.out.println(xml);

        try {
            AgeProgressionData back = (AgeProgressionData) XS.fromXML(xml);
            System.out.println();
            System.out.println("--- after round trip ---");
            System.out.println("mother ref    : " + back.getMother());
            System.out.println("mother target : " + (back.getMother() == null ? "<n/a>" : back.getMother().get()));
            System.out.println("nameMother    : " + back.getNameMother());
            System.out.println();
            System.out.println("VERDICT: a cleared weak reference saves and loads cleanly as empty.");
        } catch (Throwable t) {
            System.out.println("fromXML THREW: " + t.getClass().getName() + ": " + t.getMessage());
            System.out.println();
            System.out.println("VERDICT: a cleared weak reference WRITES but FAILS TO LOAD.");
        }
        System.out.println();
    }
}
