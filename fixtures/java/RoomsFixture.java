import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.housing.RoomInfo;
import jasbro.game.housing.RoomSlotType;
import jasbro.game.world.RoomLoader;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Dumps rooms.xml as loaded by the REAL Java RoomLoader, to a canonical text form.
 *
 * Usage: java RoomsFixture <output-file>     (working directory must contain rooms.xml)
 *
 * RoomInfo deliberately exposes no getter for its requirement maps - only isActivityValid() - so
 * reflection is used to reach the private fields. That is a feature for this purpose: it means the
 * dump reflects exactly what the loader built, not what a public API chose to reveal.
 *
 * The output is sorted by room id and by activity name so that Java's HashMap iteration order does
 * not leak into the golden file.
 */
public class RoomsFixture {

    public static void main(String[] args) throws Exception {
        String outPath = args.length > 0 ? args[0] : "rooms-golden.txt";

        Map<String, RoomInfo> rooms = RoomLoader.loadRooms(null);

        List<String> ids = new ArrayList<>(rooms.keySet());
        ids.sort(Comparator.naturalOrder());

        StringBuilder sb = new StringBuilder();
        sb.append("ROOMS=").append(ids.size()).append('\n');

        for (String id : ids) {
            RoomInfo info = rooms.get(id);
            sb.append('\n');
            sb.append("ROOM ").append(info.getId()).append('\n');
            sb.append("  cost=").append(info.getCost()).append('\n');
            sb.append("  maxOccupancy=").append(info.getMaxOccupancy()).append('\n');
            sb.append("  image=").append(imageName(info)).append('\n');

            // Java stores slot types in an EnumSet, so iteration order is enum-declaration order.
            // Dump in that order without re-sorting, so a port that sorts differently shows up.
            sb.append("  slots=");
            Set<RoomSlotType> slots = info.getSlotTypes();
            boolean first = true;
            for (RoomSlotType s : slots) {
                if (!first) sb.append(',');
                sb.append(s.name());
                first = false;
            }
            sb.append('\n');

            sb.append("  activities=");
            first = true;
            for (ActivityType a : info.getActivities()) {
                if (!first) sb.append(',');
                sb.append(a.name());
                first = false;
            }
            sb.append('\n');

            Map<ActivityType, ActivityRequirement> reqs = privateMap(info, "activityRequirements");
            List<ActivityType> actKeys = new ArrayList<>(reqs.keySet());
            actKeys.sort(Comparator.comparing(Enum::name));
            for (ActivityType a : actKeys) {
                sb.append("  req ").append(a.name()).append(" = ")
                  .append(describeActivity(reqs.get(a))).append('\n');
            }

            Map<ActivityType, ActivityRequirement> childReqs = privateMap(info, "childCareActivityRequirements");
            if (!childReqs.isEmpty()) {
                List<ActivityType> childKeys = new ArrayList<>(childReqs.keySet());
                childKeys.sort(Comparator.comparing(Enum::name));
                for (ActivityType a : childKeys) {
                    sb.append("  childreq ").append(a.name()).append(" = ")
                      .append(describeActivity(childReqs.get(a))).append('\n');
                }
            }
        }

        try (PrintWriter w = new PrintWriter(outPath, "UTF-8")) {
            w.print(sb);
        }
        System.out.println("wrote " + outPath + " (" + sb.length() + " chars, ROOMS=" + ids.size() + ")");
    }

    private static String imageName(RoomInfo info) {
        try {
            Object img = info.getImage();
            if (img == null) return "null";
            try {
                Object fn = img.getClass().getMethod("getFilename").invoke(img);
                if (fn != null) return String.valueOf(fn);
            } catch (NoSuchMethodException ignored) {
                // fall through to toString
            }
            return String.valueOf(img);
        } catch (Exception e) {
            return "<error:" + e.getClass().getSimpleName() + ">";
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<ActivityType, ActivityRequirement> privateMap(RoomInfo info, String field) throws Exception {
        Field f = RoomInfo.class.getDeclaredField(field);
        f.setAccessible(true);
        return (Map<ActivityType, ActivityRequirement>) f.get(info);
    }

    // ------------------------------------------------------------------ requirement description

    private static String describeActivity(ActivityRequirement r) {
        if (r == null) return "null";
        String n = r.getClass().getSimpleName();
        switch (n) {
            case "NoActivityRequirement":      return "none";
            case "ChildCareRequirement":       return "child-care";
            case "MinimumOccupantRequirement": return "min-occupant(" + intField(r, "minimum") + ")";
            case "MaximumOccupantRequirement": return "max-occupant(" + intField(r, "maximum") + ")";
            case "ExactOccupantRequirement":   return "exact-occupant(" + intField(r, "count") + ")";
            case "AndActivityRequirement": {
                ActivityRequirement[] arr = arrayField(r);
                List<String> parts = new ArrayList<>();
                if (arr != null) for (ActivityRequirement a : arr) parts.add(describeActivity(a));
                return "and[" + String.join(", ", parts) + "]";
            }
            case "MinimumCharacterRequirement": {
                CharacterRequirement cr = (CharacterRequirement) objectField(r, "requirement");
                return "min-character(" + intField(r, "minimum")
                     + ", " + describeCharacter(cr) + ")";
            }
            case "AllCharacterRequirement": {
                CharacterRequirement cr = (CharacterRequirement) objectField(r, "requirement");
                return "all-character(" + describeCharacter(cr) + ")";
            }
            default:
                return n + "?" + fields(r);
        }
    }

    private static String describeCharacter(CharacterRequirement r) {
        if (r == null) return "null";
        String n = r.getClass().getSimpleName();
        switch (n) {
            case "TraitRequirement":
                return "trait(" + enumField(r, "trait", "traitType") + ")";
            case "SpecializationRequirement":
                return "specialization(" + enumField(r, "specialization", "specializationType") + ")";
            case "CharacterTypeRequirement":
                return "char-type(" + enumField(r, "characterType", "charType", "type") + ")";
            case "OrCharacterRequirement": {
                CharacterRequirement[] arr = charArrayField(r);
                List<String> parts = new ArrayList<>();
                if (arr != null) for (CharacterRequirement c : arr) parts.add(describeCharacter(c));
                return "or[" + String.join(", ", parts) + "]";
            }
            default:
                return n + "?" + fields(r);
        }
    }

    // ---------------------------------------------------------------- reflection helpers

    /** Returns the first declared field of the given names that exists, as an int. */
    private static int intField(Object o) { return 0; }

    private static int intField(Object o, String... names) {
        Object v = objectField(o, names);
        return v instanceof Number ? ((Number) v).intValue() : 0;
    }

    private static String enumField(Object o, String... names) {
        Object v = objectField(o, names);
        return v == null ? "null" : String.valueOf(v);
    }

    private static Object objectField(Object o, String... names) {
        for (String name : names) {
            Field f = findField(o.getClass(), name);
            if (f != null) {
                try {
                    f.setAccessible(true);
                    return f.get(o);
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }

    private static ActivityRequirement[] arrayField(Object o) {
        Object v = objectField(o, "requirements", "requirementArray");
        return v instanceof ActivityRequirement[] ? (ActivityRequirement[]) v : null;
    }

    private static CharacterRequirement[] charArrayField(Object o) {
        Object v = objectField(o, "requirements", "requirementArray");
        return v instanceof CharacterRequirement[] ? (CharacterRequirement[]) v : null;
    }

    private static Field findField(Class<?> c, String name) {
        Class<?> cur = c;
        while (cur != null && cur != Object.class) {
            try {
                return cur.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                cur = cur.getSuperclass();
            }
        }
        return null;
    }

    /** Fallback: dump every declared field, so an unexpected shape is still visible in the golden file. */
    private static String fields(Object o) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Field f : o.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
            try {
                f.setAccessible(true);
                if (!first) sb.append(", ");
                sb.append(f.getName()).append('=').append(String.valueOf(f.get(o)));
                first = false;
            } catch (Exception ignored) {
            }
        }
        return sb.append('}').toString();
    }
}
