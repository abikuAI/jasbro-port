using System.Globalization;
using System.Xml.Linq;
using Simbro.Core.Content;

namespace Simbro.Core.Content.Rooms;

/// <summary>
/// Thrown when <c>rooms.xml</c> cannot be loaded. Mirrors the exceptions the Java loader lets
/// escape (Apache Commons <c>ValidationException</c>, <c>NumberFormatException</c>,
/// <c>IllegalArgumentException</c> from <c>valueOf</c>).
/// </summary>
public sealed class RoomLoadException : Exception
{
    public RoomLoadException(string message) : base(message) { }
    public RoomLoadException(string message, Exception inner) : base(message, inner) { }
}

/// <summary>
/// Loads <c>rooms.xml</c>. Ported from <c>jasbro.game.world.RoomLoader</c>.
/// </summary>
/// <remarks>
/// <para>
/// Unlike the character and event content, this file is <b>not</b> XStream. It is a plain DOM walk
/// with <b>attribute-based dispatch</b>: every requirement element is named either
/// <c>requirement</c> or <c>char-requirement</c>, and the actual type comes from the <c>type</c>
/// attribute. See <c>CONTENT-MODEL.md</c>.
/// </para>
/// <para>
/// <b>Element names are ignored almost everywhere.</b> Only one place checks a tag name:
/// <see cref="ParseActivity"/> distinguishes <c>child-activity</c>. This is not an oversight to
/// tidy up — shipped content depends on it. <c>rooms.xml:645</c> writes
/// <c>&lt;requirement type="specialization"&gt;</c> where convention says
/// <c>&lt;char-requirement&gt;</c>, and it only works because the character-side path never looks
/// at the tag. <b>A port that validated element names would reject shipped content.</b>
/// </para>
/// </remarks>
public static class RoomLoader
{
    public const string DefaultRoomFile = "rooms.xml";

    private static readonly Dictionary<string, Func<XElement, ActivityRequirement>> ActivityParsers = new()
    {
        ["all-character"] = ParseAllCharacter,
        ["and"] = ParseAnd,
        ["child-care"] = ParseChildCare,
        ["exact-occupant"] = e => new ExactOccupantRequirement(ReadSingleDigitCount(e)),
        ["max-occupant"] = e => new MaximumOccupantRequirement(ReadSingleDigitCount(e)),
        ["min-character"] = ParseMinimumCharacter,
        ["min-occupant"] = e => new MinimumOccupantRequirement(ReadSingleDigitCount(e)),
        ["none"] = _ => new NoActivityRequirement(),
    };

    private static readonly Dictionary<string, Func<XElement, CharacterRequirement>> CharacterParsers = new()
    {
        ["char-type"] = ParseCharacterType,
        ["or"] = ParseOr,
        ["specialization"] = ParseSpecialization,
        ["trait"] = ParseTrait,
    };

    /// <summary>
    /// Loads every room in <paramref name="path"/>, or <c>rooms.xml</c> when null.
    /// </summary>
    /// <remarks>
    /// The default path is <b>relative to the working directory</b>, matching
    /// <c>new FileInputStream("rooms.xml")</c>. The caller controls the CWD.
    /// </remarks>
    public static Dictionary<string, RoomDefinition> LoadRooms(string? path = null)
    {
        var file = path ?? DefaultRoomFile;
        if (!File.Exists(file))
        {
            throw new RoomLoadException($"Room file not found: '{file}'");
        }

        XDocument doc;
        try
        {
            doc = XDocument.Load(file);
        }
        catch (Exception e) when (e is System.Xml.XmlException or IOException)
        {
            throw new RoomLoadException($"Could not parse room file '{file}'", e);
        }

        var rooms = new Dictionary<string, RoomDefinition>(StringComparer.Ordinal);
        var root = doc.Root ?? throw new RoomLoadException($"Room file '{file}' has no root element");

        // NOTE: no name check. Java iterates every ELEMENT child of the root and treats each as a
        // room, so a misspelled <rom> would still be parsed.
        foreach (var child in root.Elements())
        {
            ParseRoomElement(child, rooms);
        }

        return rooms;
    }

    private static void ParseRoomElement(XElement element, Dictionary<string, RoomDefinition> rooms)
    {
        // Java logs and SKIPS a room that fails validation.
        if (!ValidateRoomElement(element))
        {
            return;
        }

        var id = element.Attribute("id")!.Value;
        if (rooms.ContainsKey(id))
        {
            // Java logs a warning and overwrites.
        }

        var maxOccupancy = ParseJavaInt(element.Attribute("max-occupancy")!.Value, "max-occupancy");
        var cost = ParseJavaInt(element.Attribute("cost")!.Value, "cost");
        var image = element.Attribute("image")!.Value;

        var room = new RoomDefinition(id, cost, maxOccupancy, image);

        // getElementsByTagName is a DESCENDANT search, and takes the first match. Using
        // Descendants() rather than Elements() reproduces that - it matters for nested cases.
        var activities = element.Descendants("activities").FirstOrDefault();
        var slots = element.Descendants("slots").FirstOrDefault();

        ParseSlotTypes(slots, room);
        ParseActivities(activities, room);

        rooms[id] = room;
    }

    private static bool ValidateRoomElement(XElement element)
        => element.Attribute("id") is not null
        && element.Attribute("cost") is not null
        && element.Attribute("max-occupancy") is not null
        && element.Attribute("image") is not null;

    private static void ParseSlotTypes(XElement? slots, RoomDefinition room)
    {
        if (slots is null)
        {
            ApplyDefaultSlots(room);
            return;
        }

        foreach (var child in slots.Elements())
        {
            var type = child.Attribute("type")?.Value
                ?? throw new RoomLoadException(
                    $"'slot' element has missing or blank attribute 'type' in room '{room.Id}'");

            if (string.IsNullOrWhiteSpace(type))
            {
                throw new RoomLoadException(
                    $"'slot' element has missing or blank attribute 'type' in room '{room.Id}'");
            }

            if (!Enum.TryParse<RoomSlotType>(type, out var slotType))
            {
                throw new RoomLoadException(
                    $"Invalid slot type '{type}' in room '{room.Id}'. " +
                    $"Known values: {string.Join(", ", Enum.GetNames<RoomSlotType>())}");
            }

            room.AddSlotType(slotType);
        }

        if (room.SlotTypes.Count == 0)
        {
            ApplyDefaultSlots(room);
        }

        // Quirk, preserved deliberately: declaring SMALLROOM silently also grants LARGEROOM.
        if (room.SlotTypes.Contains(RoomSlotType.SMALLROOM) && !room.SlotTypes.Contains(RoomSlotType.LARGEROOM))
        {
            room.AddSlotType(RoomSlotType.LARGEROOM);
        }
    }

    private static void ApplyDefaultSlots(RoomDefinition room)
    {
        room.AddSlotType(RoomSlotType.SMALLROOM);
        room.AddSlotType(RoomSlotType.LARGEROOM);
    }

    private static void ParseActivities(XElement? activities, RoomDefinition room)
    {
        if (activities is null)
        {
            // Java: Validate.notNull -> ValidationException. This escapes loadRooms entirely.
            throw new RoomLoadException($"Required element 'activities' missing for room '{room.Id}'");
        }

        foreach (var child in activities.Elements())
        {
            ParseActivity(child, room);
        }
    }

    private static void ParseActivity(XElement element, RoomDefinition room)
    {
        var id = element.Attribute("id")?.Value;
        if (string.IsNullOrWhiteSpace(id))
        {
            throw new RoomLoadException(
                $"'activity' element has missing or blank attribute 'id' in room '{room.Id}'");
        }

        if (!Enum.TryParse<ActivityType>(id, out var activity))
        {
            // Java: ActivityType.valueOf -> IllegalArgumentException, which escapes loadRooms.
            throw new RoomLoadException(
                $"Unknown activity id '{id}' in room '{room.Id}'. " +
                $"Known values: {string.Join(", ", Enum.GetNames<ActivityType>())}");
        }

        var requirementElement = element.Descendants("requirement").FirstOrDefault();
        if (requirementElement is null)
        {
            throw new RoomLoadException(
                $"Required element 'requirement' missing under 'activity' for room '{room.Id}'");
        }

        var requirement = ParseActivityRequirement(requirementElement);

        // The ONE place a tag name is consulted.
        if (element.Name.LocalName == "child-activity")
        {
            room.AddChildCareActivity(activity, requirement);
        }
        else
        {
            room.AddActivity(activity, requirement);
        }
    }

    /// <summary>Dispatches an activity requirement on its <c>type</c> attribute.</summary>
    public static ActivityRequirement ParseActivityRequirement(XElement element)
    {
        var type = element.Attribute("type")?.Value;
        if (string.IsNullOrWhiteSpace(type))
        {
            throw new RoomLoadException(
                "Required attribute 'type' missing on element 'requirement'");
        }

        if (!ActivityParsers.TryGetValue(type, out var parser))
        {
            throw new RoomLoadException(
                $"Attribute 'type' with value '{type}' does not map to any known parser. " +
                $"Known values: {string.Join(", ", ActivityParsers.Keys.OrderBy(k => k, StringComparer.Ordinal))}");
        }

        return parser(element);
    }

    /// <summary>
    /// Dispatches a character requirement on its <c>type</c> attribute.
    /// </summary>
    /// <remarks>
    /// <b>The element name is never checked.</b> This is what lets <c>rooms.xml:645</c>'s
    /// <c>&lt;requirement type="specialization"&gt;</c> be parsed as a character requirement.
    /// </remarks>
    public static CharacterRequirement ParseCharacterRequirement(XElement element)
    {
        var type = element.Attribute("type")?.Value;
        if (string.IsNullOrWhiteSpace(type))
        {
            throw new RoomLoadException(
                "Required attribute 'type' missing on element 'requirement'");
        }

        if (!CharacterParsers.TryGetValue(type, out var parser))
        {
            throw new RoomLoadException(
                $"Attribute 'type' with value '{type}' does not map to any known parser. " +
                $"Known values: {string.Join(", ", CharacterParsers.Keys.OrderBy(k => k, StringComparer.Ordinal))}");
        }

        return parser(element);
    }

    // -----------------------------------------------------------------------------------------
    // Activity requirement parsers
    // -----------------------------------------------------------------------------------------

    private static ActivityRequirement ParseAllCharacter(XElement e)
    {
        var inner = FirstElementChild(e, "all-character");
        return new AllCharacterRequirement(ParseCharacterRequirement(inner));
    }

    private static ActivityRequirement ParseMinimumCharacter(XElement e)
    {
        var count = ParseJavaInt(e.Attribute("count")?.Value ?? "", "count");
        var inner = FirstElementChild(e, "min-character");
        return new MinimumCharacterRequirement(ParseCharacterRequirement(inner), count);
    }

    private static ActivityRequirement ParseAnd(XElement e)
    {
        var parts = e.Elements().Select(ParseActivityRequirement).ToList();
        return new AndActivityRequirement(parts);
    }

    private static ActivityRequirement ParseChildCare(XElement e) => new ChildCareRequirement();

    // -----------------------------------------------------------------------------------------
    // Character requirement parsers
    // -----------------------------------------------------------------------------------------

    private static CharacterRequirement ParseTrait(XElement e)
    {
        var raw = e.Attribute("trait")?.Value;
        if (string.IsNullOrWhiteSpace(raw))
        {
            throw new RoomLoadException("Required attribute 'trait' missing or blank");
        }

        if (!Enum.TryParse<Trait>(raw, out var trait))
        {
            throw new RoomLoadException($"Unknown trait '{raw}'");
        }

        return new TraitRequirement(trait);
    }

    private static CharacterRequirement ParseSpecialization(XElement e)
    {
        var raw = e.Attribute("specialization")?.Value;
        if (string.IsNullOrWhiteSpace(raw))
        {
            throw new RoomLoadException("Required attribute 'specialization' missing or blank");
        }

        if (!Enum.TryParse<SpecializationType>(raw, out var spec))
        {
            throw new RoomLoadException($"Unknown specialization '{raw}'");
        }

        return new SpecializationRequirement(spec);
    }

    private static CharacterRequirement ParseCharacterType(XElement e)
    {
        // Reads 'char-type', NOT 'type' - 'type' was already consumed for dispatch.
        var raw = e.Attribute("char-type")?.Value;
        if (string.IsNullOrWhiteSpace(raw))
        {
            throw new RoomLoadException("Required attribute 'char-type' missing or blank");
        }

        if (!Enum.TryParse<CharacterType>(raw, out var charType))
        {
            throw new RoomLoadException($"Unknown character type '{raw}'");
        }

        return new CharacterTypeRequirement(charType);
    }

    private static CharacterRequirement ParseOr(XElement e)
    {
        var parts = e.Elements().Select(ParseCharacterRequirement).ToList();
        return new OrCharacterRequirement(parts);
    }

    // -----------------------------------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------------------------------

    /// <summary>
    /// Takes the first element child, regardless of its name — matching the Java parsers, which
    /// grab <c>children.item(i)</c> and break on the first <c>ELEMENT_NODE</c>.
    /// </summary>
    private static XElement FirstElementChild(XElement parent, string context)
        => parent.Elements().FirstOrDefault()
           ?? throw new RoomLoadException($"'{context}' has no child element to parse");

    /// <summary>
    /// Reads an integer with Java's <c>Integer.parseInt</c> semantics.
    /// </summary>
    /// <remarks>
    /// <para>
    /// <b>Not the same as C#'s <c>int.Parse</c>.</b> C# accepts leading and trailing whitespace by
    /// default (<c>"  2  "</c> parses fine); Java rejects it. <c>NumberStyles.AllowLeadingSign</c>
    /// with an invariant culture matches Java: optional leading <c>-</c>, then digits, nothing else.
    /// Getting this wrong would silently accept content the original rejects.
    /// </para>
    /// <para>
    /// <b>Deliberate deviation (Class A fix, see PORT-POLICY.md).</b> Java's occupant parsers
    /// validate <c>count</c> against the regex <c>"[0-9]"</c>, which — because
    /// <c>Validate.matchesPattern</c> anchors the whole string — permits <b>exactly one digit</b>.
    /// <c>count="10"</c> would therefore throw. Every shipped value is 1-5, so this is latent, not
    /// active. The port accepts any non-negative integer instead of reproducing a crash on valid
    /// content. Behaviour on all shipped content is identical, which is what the parity test checks.
    /// </para>
    /// </remarks>
    private static int ParseJavaInt(string text, string attribute)
    {
        if (!int.TryParse(text, NumberStyles.AllowLeadingSign, CultureInfo.InvariantCulture, out var value))
        {
            throw new RoomLoadException($"Value '{text}' for '{attribute}' is not a valid integer");
        }

        return value;
    }

    /// <summary>Occupant counts, with the single-digit restriction lifted (see <see cref="ParseJavaInt"/>).</summary>
    private static int ReadSingleDigitCount(XElement e)
    {
        var raw = e.Attribute("count")?.Value ?? "";
        var value = ParseJavaInt(raw, "count");
        if (value < 0)
        {
            throw new RoomLoadException($"Value '{raw}' for 'count' must not be negative");
        }

        return value;
    }
}
