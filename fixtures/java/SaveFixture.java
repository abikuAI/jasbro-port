import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import jasbro.game.GameData;
import jasbro.game.world.Time;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;

/**
 * Generates a GOLDEN SAVE FIXTURE using the real decompiled game classes.
 *
 * <p>This is the reference the C# port must reproduce. It uses the exact same XStream
 * configuration as the game itself (SaveAndLoadPerformer.java):
 * {@code new XStream(new StaxDriver())} + {@code autodetectAnnotations(true)}.
 *
 * <p>{@code GameData.init()} cannot be used headlessly: QuestManager's constructor calls
 * {@code Jasbro.getInstance().getData()}, and the singleton needs the Swing GUI. So each
 * optional component is attached individually and skipped if it needs the singleton.
 *
 * <p>Usage: {@code java -cp <game.jar>;<libs> SaveFixture <output.xml>}
 */
public class SaveFixture {

    private static int attached = 0;
    private static int skipped = 0;

    private static void set(Object target, String fieldName, Object value) throws Exception {
        Field f = target.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }

    /** Attach a component by class name, skipping (and reporting) anything that needs the singleton. */
    private static void attach(GameData data, String fieldName, String className) {
        try {
            Class<?> c = Class.forName(className);
            Object instance = c.getDeclaredConstructor().newInstance();
            set(data, fieldName, instance);
            attached++;
            System.out.println("  attached " + fieldName + " <- " + c.getSimpleName());
        } catch (Throwable t) {
            skipped++;
            Throwable root = t;
            while (root.getCause() != null) root = root.getCause();
            System.out.println("  SKIPPED  " + fieldName + " (" + root.getClass().getSimpleName()
                    + (root.getMessage() == null ? "" : ": " + root.getMessage()) + ")");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("usage: SaveFixture <output.xml>");
            System.exit(2);
        }

        GameData data = new GameData();

        // Deterministic, known values the C# verifier asserts on.
        data.setDay(42);
        data.setTime(Time.NIGHT);
        set(data, "money", 1234L);

        System.out.println("attaching optional components:");
        attach(data, "inventory", "jasbro.game.items.Inventory");
        attach(data, "unlocks", "jasbro.game.world.Unlocks");
        attach(data, "defaultPreferences", "jasbro.game.DefaultPreferences");
        attach(data, "characters", "java.util.ArrayList");
        attach(data, "houses", "java.util.ArrayList");
        attach(data, "otherLocationMap", "java.util.EnumMap");
        attach(data, "shop", "jasbro.game.world.market.Shop");
        attach(data, "eventManager", "jasbro.game.events.EventManager");
        attach(data, "questManager", "jasbro.game.world.market.QuestManager");

        System.out.println("attached=" + attached + " skipped=" + skipped);

        write(data, args[0]);

        // ---- Second fixture: the same core state PLUS a real character. -----------
        // Characters are where save-format complexity actually lives (nested objects,
        // polymorphic attribute maps, enum sets, conditions), so this is the more
        // demanding reference for a port.
        if (args.length >= 2) {
            System.out.println();
            System.out.println("building character fixture:");
            try {
                GameData d2 = new GameData();
                d2.setDay(7);
                d2.setTime(Time.AFTERNOON);
                set(d2, "money", 99L);

                jasbro.game.character.CharacterBase base = new jasbro.game.character.CharacterBase();
                base.setId("TestChar");
                base.setName("Test Subject");
                base.setType(jasbro.game.character.CharacterType.SLAVE);
                base.setGender(jasbro.game.character.Gender.FEMALE);
                base.setAttribute(jasbro.game.character.attributes.BaseAttributeTypes.CHARISMA, 12);
                base.setAttribute(jasbro.game.character.attributes.BaseAttributeTypes.INTELLIGENCE, 7);
                base.addTrait(jasbro.game.character.traits.Trait.LOYAL);
                base.addTrait(jasbro.game.character.traits.Trait.FIT);

                jasbro.game.character.Charakter ch = new jasbro.game.character.Charakter(base);

                // Charakter.base is TRANSIENT, so the save does not contain the CharacterBase at
                // all. Charakter keeps its OWN copies of these fields and links back to content
                // via baseId. Set them explicitly to produce a realistic save document.
                set(ch, "name", "Test Subject");
                set(ch, "type", jasbro.game.character.CharacterType.SLAVE);
                set(ch, "gender", jasbro.game.character.Gender.FEMALE);
                set(ch, "baseId", "TestChar");
                set(ch, "traits", new java.util.ArrayList<jasbro.game.character.traits.Trait>(
                        java.util.Arrays.asList(jasbro.game.character.traits.Trait.LOYAL,
                                                jasbro.game.character.traits.Trait.FIT)));
                System.out.println("  character name now: " + ch.getName());

                java.util.List<jasbro.game.character.Charakter> chars = new java.util.ArrayList<>();
                chars.add(ch);
                set(d2, "characters", chars);

                write(d2, args[1]);

                // ---- Third fixture: shared object reference. ----------------------
                // GameData.protagonist is a Charakter that is ALSO in characters. XStream
                // writes the first occurrence inline and the second as a reference, so this
                // reproduces the real-game shape exactly. Real saves always contain these.
                if (args.length >= 3) {
                    GameData d3 = new GameData();
                    d3.setDay(3);
                    d3.setTime(Time.MORNING);
                    set(d3, "money", 500L);

                    jasbro.game.character.Charakter ch2 = new jasbro.game.character.Charakter(null);
                    set(ch2, "name", "First Subject");
                    set(ch2, "type", jasbro.game.character.CharacterType.TRAINER);
                    set(ch2, "gender", jasbro.game.character.Gender.FEMALE);
                    set(ch2, "baseId", "TestChar");

                    // A SECOND character of the same class, so the reference path must
                    // disambiguate between them and reveal XStream's index syntax.
                    jasbro.game.character.Charakter ch3 = new jasbro.game.character.Charakter(null);
                    set(ch3, "name", "Second Subject");
                    set(ch3, "type", jasbro.game.character.CharacterType.SLAVE);
                    set(ch3, "gender", jasbro.game.character.Gender.FUTA);
                    set(ch3, "baseId", "TestChar");

                    java.util.List<jasbro.game.character.Charakter> chars2 = new java.util.ArrayList<>();
                    chars2.add(ch2);
                    chars2.add(ch3);
                    set(d3, "characters", chars2);

                    // Point at the SECOND one. If XStream indexes from 1 we expect [2].
                    set(d3, "protagonist", ch3);

                    write(d3, args[2]);
                }
            } catch (Throwable t) {
                System.out.println("  character fixture FAILED: " + t);
                t.printStackTrace(System.out);
            }
        }
    }

    private static void write(GameData data, String path) throws Exception {
        XStream xstream = new XStream(new StaxDriver());
        xstream.autodetectAnnotations(true);
        String xml = xstream.toXML(data);
        try (Writer w = new OutputStreamWriter(new FileOutputStream(path), "UTF-8")) {
            w.write(xml);
        }
        System.out.println("wrote " + path + " (" + xml.length() + " chars)");
        System.out.println(xml);
    }
}
