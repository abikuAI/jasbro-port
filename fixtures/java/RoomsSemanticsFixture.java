import jasbro.Util;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.character.activities.requirements.CharacterRequirement;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.housing.RoomInfo;
import jasbro.game.world.RoomLoader;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Differential fixture for ROOM REQUIREMENT EVALUATION.
 *
 * Dumps a truth table: for a fixed, deterministic set of character groups, whether each
 * (room, activity) pair is valid according to the REAL Java requirement classes.
 *
 * Usage: java RoomsSemanticsFixture <output-file>   (working directory must contain rooms.xml)
 *
 * WHY A SUBCLASS RATHER THAN REAL CHARACTERS
 * ------------------------------------------
 * The requirement predicates touch exactly three things on a Charakter: getType(), getTraits() and
 * getSpecializations(). Building real characters would drag in CharacterBase, Jasbro.getInstance(),
 * the inventory and equipment trait modifiers - none of which the predicates consult. Subclassing
 * and overriding those three accessors isolates precisely the behaviour under test and makes the
 * fixture deterministic.
 *
 * The configurations are derived from the CONTENT ITSELF: every trait and specialization that
 * actually appears anywhere in rooms.xml gets its own configuration, so the table cannot silently
 * miss a requirement type that the shipped content exercises.
 */
public class RoomsSemanticsFixture {

    /** A Charakter whose three requirement-relevant accessors are supplied directly. */
    public static class FakeCharakter extends Charakter {
        private final CharacterType type;
        private final List<Trait> traits;
        private final Set<SpecializationType> specs;

        public FakeCharakter(CharacterType type, List<Trait> traits, Set<SpecializationType> specs) {
            super(null);
            this.type = type;
            this.traits = traits;
            this.specs = specs;
        }

        @Override public CharacterType getType() { return type; }
        @Override public List<Trait> getTraits() { return traits; }
        @Override public Set<SpecializationType> getSpecializations() { return specs; }
    }

    public static void main(String[] args) throws Exception {
        String outPath = args.length > 0 ? args[0] : "rooms-semantics-golden.txt";

        Map<String, RoomInfo> rooms = RoomLoader.loadRooms(null);

        // ---- 1. harvest every trait / specialization / char-type / count the content uses ----
        Set<Trait> usedTraits = new TreeSet<>(Comparator.comparing(Enum::name));
        Set<SpecializationType> usedSpecs = new TreeSet<>(Comparator.comparing(Enum::name));
        Set<CharacterType> usedTypes = new TreeSet<>(Comparator.comparing(Enum::name));

        List<String> roomIds = new ArrayList<>(rooms.keySet());
        roomIds.sort(Comparator.naturalOrder());

        for (String id : roomIds) {
            RoomInfo info = rooms.get(id);
            Map<ActivityType, ActivityRequirement> reqs = privateMap(info, "activityRequirements");
            for (ActivityRequirement r : reqs.values()) {
                harvest(r, usedTraits, usedSpecs, usedTypes);
            }
        }

        System.out.println("content uses: " + usedTraits.size() + " trait(s), "
                + usedSpecs.size() + " specialization(s), " + usedTypes.size() + " char-type(s)");

        // ---- 2. build the deterministic configuration set ----
        List<Config> configs = buildConfigs(usedTraits, usedSpecs, usedTypes);

        // ---- 3. evaluate every (room, activity, config) ----
        StringBuilder sb = new StringBuilder();
        sb.append("CONFIGS=").append(configs.size()).append('\n');
        for (int i = 0; i < configs.size(); i++) {
            sb.append("CONFIG ").append(i).append(' ').append(configs.get(i).label).append('\n');
        }

        int checks = 0;
        for (String id : roomIds) {
            RoomInfo info = rooms.get(id);

            // Activities in the room's own declared order, so the table matches the loader.
            List<ActivityType> acts = new ArrayList<>(info.getActivities());
            for (ActivityType act : acts) {
                sb.append("V ").append(id).append('/').append(act.name()).append(' ');
                for (int i = 0; i < configs.size(); i++) {
                    Config cfg = configs.get(i);
                    Util.TypeAmounts amounts = Util.getTypeAmounts(cfg.characters);
                    boolean valid = info.isActivityValid(act, cfg.characters, amounts);
                    sb.append(valid ? '1' : '0');
                    checks++;
                }
                sb.append('\n');
            }
        }

        try (PrintWriter w = new PrintWriter(outPath, "UTF-8")) {
            w.print(sb);
        }
        System.out.println("wrote " + outPath + " : " + configs.size() + " configs, "
                + checks + " evaluations");
    }

    // ------------------------------------------------------------------ configuration set

    private static class Config {
        final String label;
        final List<Charakter> characters = new ArrayList<>();
        Config(String label) { this.label = label; }
    }

    private static Charakter ch(CharacterType type) {
        return new FakeCharakter(type, new ArrayList<Trait>(), EnumSet.noneOf(SpecializationType.class));
    }

    private static Charakter chTrait(CharacterType type, Trait trait) {
        return new FakeCharakter(type, new ArrayList<>(Arrays.asList(trait)),
                EnumSet.noneOf(SpecializationType.class));
    }

    private static Charakter chSpec(CharacterType type, SpecializationType spec) {
        return new FakeCharakter(type, new ArrayList<Trait>(),
                EnumSet.of(spec));
    }

    private static List<Config> buildConfigs(Set<Trait> traits, Set<SpecializationType> specs,
                                             Set<CharacterType> types) {
        List<Config> configs = new ArrayList<>();

        // --- empty group: exercises the vacuous-truth edges of and/all-character ---
        configs.add(new Config("EMPTY"));

        // --- one bare character of each type ---
        for (CharacterType t : CharacterType.values()) {
            Config c = new Config("ONE_" + t.name());
            c.characters.add(ch(t));
            configs.add(c);
        }

        // --- two bare characters of each type ---
        for (CharacterType t : CharacterType.values()) {
            Config c = new Config("TWO_" + t.name());
            c.characters.add(ch(t));
            c.characters.add(ch(t));
            configs.add(c);
        }

        // --- each used trait: alone, all-slaves, and trainer+slave ---
        for (Trait tr : traits) {
            Config solo = new Config("TRAIT_" + tr.name());
            solo.characters.add(chTrait(CharacterType.SLAVE, tr));
            configs.add(solo);

            Config all = new Config("TRAITALL_" + tr.name());
            all.characters.add(chTrait(CharacterType.SLAVE, tr));
            all.characters.add(chTrait(CharacterType.SLAVE, tr));
            all.characters.add(chTrait(CharacterType.SLAVE, tr));
            configs.add(all);

            Config mix = new Config("TRAITMIX_" + tr.name());
            mix.characters.add(chTrait(CharacterType.SLAVE, tr));
            mix.characters.add(ch(CharacterType.TRAINER));
            configs.add(mix);
        }

        // --- each used specialization: alone and trainer+slave ---
        for (SpecializationType sp : specs) {
            Config solo = new Config("SPEC_" + sp.name());
            solo.characters.add(chSpec(CharacterType.SLAVE, sp));
            configs.add(solo);

            Config mix = new Config("SPECMIX_" + sp.name());
            mix.characters.add(chSpec(CharacterType.SLAVE, sp));
            mix.characters.add(ch(CharacterType.TRAINER));
            configs.add(mix);

            Config two = new Config("SPECTWO_" + sp.name());
            two.characters.add(chSpec(CharacterType.SLAVE, sp));
            two.characters.add(chSpec(CharacterType.SLAVE, sp));
            configs.add(two);
        }

        // --- each used char-type paired with an adult, for all-character / char-type gates ---
        for (CharacterType t : types) {
            Config withAdult = new Config("TYPEADULT_" + t.name());
            withAdult.characters.add(ch(t));
            withAdult.characters.add(ch(CharacterType.TRAINER));
            configs.add(withAdult);
        }

        // --- child-care shapes: these drive ChildCareRequirement's branches ---
        configs.add(group("CARE_INFANT_TRAINER", CharacterType.INFANT, CharacterType.TRAINER));
        configs.add(group("CARE_INFANT_ONLY", CharacterType.INFANT));
        configs.add(group("CARE_INFANT_CHILD_TRAINER",
                CharacterType.INFANT, CharacterType.CHILD, CharacterType.TRAINER));
        configs.add(group("CARE_INFANT_TEEN_TRAINER",
                CharacterType.INFANT, CharacterType.TEENAGER, CharacterType.TRAINER));
        configs.add(group("CARE_CHILD_TRAINER", CharacterType.CHILD, CharacterType.TRAINER));
        configs.add(group("CARE_TEEN_TRAINER", CharacterType.TEENAGER, CharacterType.TRAINER));
        configs.add(group("CARE_TWO_INFANT_TRAINER",
                CharacterType.INFANT, CharacterType.INFANT, CharacterType.TRAINER));
        configs.add(group("CARE_INFANT_TWO_TRAINER",
                CharacterType.INFANT, CharacterType.TRAINER, CharacterType.TRAINER));

        // --- occupancy boundaries, incl. sizes above the shipped max of 5 ---
        for (int n = 1; n <= 6; n++) {
            Config c = new Config("SIZE_" + n);
            for (int i = 0; i < n; i++) c.characters.add(ch(CharacterType.SLAVE));
            configs.add(c);
        }

        return configs;
    }

    private static Config group(String label, CharacterType... types) {
        Config c = new Config(label);
        for (CharacterType t : types) c.characters.add(ch(t));
        return c;
    }

    // ------------------------------------------------------------------ harvesting

    /** Walks a requirement tree, collecting the traits / specializations / char-types it names. */
    @SuppressWarnings("unchecked")
    private static void harvest(ActivityRequirement r,
                                Set<Trait> traits, Set<SpecializationType> specs, Set<CharacterType> types)
            throws Exception {
        if (r == null) return;
        String n = r.getClass().getSimpleName();

        if (n.equals("AndActivityRequirement")) {
            Object arr = field(r, "requirements");
            if (arr instanceof ActivityRequirement[]) {
                for (ActivityRequirement a : (ActivityRequirement[]) arr) harvest(a, traits, specs, types);
            }
        } else if (n.equals("MinimumCharacterRequirement") || n.equals("AllCharacterRequirement")) {
            Object cr = field(r, "requirement");
            if (cr instanceof CharacterRequirement) {
                harvestChar((CharacterRequirement) cr, traits, specs, types);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void harvestChar(CharacterRequirement r,
                                    Set<Trait> traits, Set<SpecializationType> specs, Set<CharacterType> types)
            throws Exception {
        if (r == null) return;
        String n = r.getClass().getSimpleName();

        if (n.equals("TraitRequirement")) {
            Object t = field(r, "trait");
            if (t instanceof Trait) traits.add((Trait) t);
        } else if (n.equals("SpecializationRequirement")) {
            Object s = field(r, "specialization");
            if (s instanceof SpecializationType) specs.add((SpecializationType) s);
        } else if (n.equals("CharacterTypeRequirement")) {
            Object t = field(r, "type");
            if (t instanceof CharacterType) types.add((CharacterType) t);
        } else if (n.equals("OrCharacterRequirement")) {
            Object arr = field(r, "requirements");
            if (arr instanceof CharacterRequirement[]) {
                for (CharacterRequirement c : (CharacterRequirement[]) arr) harvestChar(c, traits, specs, types);
            }
        }
    }

    // ------------------------------------------------------------------ reflection helpers

    @SuppressWarnings("unchecked")
    private static Map<ActivityType, ActivityRequirement> privateMap(RoomInfo info, String field) throws Exception {
        Field f = RoomInfo.class.getDeclaredField(field);
        f.setAccessible(true);
        return (Map<ActivityType, ActivityRequirement>) f.get(info);
    }

    private static Object field(Object o, String name) throws Exception {
        Class<?> cur = o.getClass();
        while (cur != null && cur != Object.class) {
            try {
                Field f = cur.getDeclaredField(name);
                f.setAccessible(true);
                return f.get(o);
            } catch (NoSuchFieldException e) {
                cur = cur.getSuperclass();
            }
        }
        return null;
    }
}
