import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import jasbro.game.GameData;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;

/**
 * Loads a save file with the REAL game classes and prints the parsed state.
 *
 * This is the other half of the cross-language compatibility test: C# writes a save,
 * this program (running the actual shipped Java) reads it back. If the Java side can
 * reconstruct the values, the port's writer is genuinely compatible.
 *
 * Usage: java -cp <game.jar>;<libs> SaveLoader <input.xml>
 */
public class SaveLoader {

    private static void print(Object target, String fieldName) {
        try {
            Field f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object v = f.get(target);
            if (v instanceof java.util.Collection) {
                System.out.println(fieldName + "=" + ((java.util.Collection<?>) v).size() + " item(s)");
            } else {
                System.out.println(fieldName + "=" + v);
            }
        } catch (Throwable t) {
            System.out.println(fieldName + "=<ERROR " + t.getClass().getSimpleName() + ": " + t.getMessage() + ">");
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("usage: SaveLoader <input.xml>");
            System.exit(2);
        }

        XStream xstream = new XStream(new StaxDriver());
        xstream.autodetectAnnotations(true);

        GameData data;
        try (Reader r = new InputStreamReader(new FileInputStream(args[0]), "UTF-8")) {
            data = (GameData) xstream.fromXML(r);
        }

        System.out.println("LOADED-OK");
        print(data, "day");
        print(data, "time");
        print(data, "money");
        print(data, "houses");
        print(data, "characters");
        print(data, "inventory");
        print(data, "eventManager");
        print(data, "defaultPreferences");
        print(data, "unlocks");

        // Character detail — the demanding case. The content definition is NOT in the save
        // (Charakter.base is transient), so name/type/gender/traits must have come from the
        // character's own serialised fields.
        java.util.List<jasbro.game.character.Charakter> chars = data.getCharacters();
        if (chars != null) {
            for (int i = 0; i < chars.size(); i++) {
                jasbro.game.character.Charakter c = chars.get(i);
                System.out.println("CHAR[" + i + "].name=" + c.getName());
                System.out.println("CHAR[" + i + "].type=" + c.getType());
                System.out.println("CHAR[" + i + "].gender=" + c.getGender());
                System.out.println("CHAR[" + i + "].baseId=" + c.getBaseId());
                System.out.println("CHAR[" + i + "].traits=" + c.getTraits());
            }
        }
    }
}
