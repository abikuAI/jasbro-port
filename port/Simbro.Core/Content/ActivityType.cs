namespace Simbro.Core.Content;

/// <summary>
/// Every activity the game can schedule. Ported from <c>jasbro.game.character.activities.ActivityType</c>.
/// </summary>
/// <remarks>
/// <para>
/// <b>The constant names are load-bearing.</b> They are written into saves by name and looked up
/// from <c>rooms.xml</c> via <c>ActivityType.valueOf(id)</c>, so renaming one silently breaks both
/// existing saves and existing content. Do not reorder or rename.
/// </para>
/// <para>
/// The Java enum also carries constructor arguments — the running-activity class, and three flags
/// (<c>groupActivity</c>, <c>customerDependent</c>, <c>minimumObedience</c>, <c>hasSelectionOptions</c>).
/// The class reference has no C# equivalent and is deliberately dropped; behaviour will be
/// dispatched by name instead. The numeric flags are recorded where they are read, rather than
/// mirrored here, so there is one source of truth for each.
/// </para>
/// <para>
/// Order matches the Java declaration order. 60 constants.
/// </para>
/// </remarks>
public enum ActivityType
{
    REFUSEDTOWORK,
    SLEEP,
    IDLE,
    CAMP,
    SEX,
    THREESOME,
    ORGY,
    COOK,
    CLEAN,
    GOVERN,
    SUNBATHE,
    EAT,
    WALK,
    ROB,
    TRAINTOFIGHT,
    BATHE,
    READ,
    TRAIN,
    TEACH,
    TALK,
    RELAX,
    FISH,
    GARDENING,
    RITUAL,
    PRACTICE,
    WHORE,
    WHORESTREETS,
    PRAY,
    OFFERINGS,
    BARTEND,
    SOAK,
    SUBMITTOMONSTER,
    STUDY,
    WORKGUILD,
    SWIM,
    NURSE,
    SELLFOOD,
    HARVEST,
    STRIP,
    CATSHOW,
    BREAK,
    DOMINATE,
    PUBLICUSE,
    SUBMIT,
    SUCK,
    TEASE,
    PAMPER,
    BODYWRAP,
    FIGHT,
    ATTEND,
    ADVERTISE,
    BATHATTENDANT,
    MASSAGE,
    EVENT,
    CUSTOMEVENT,
    MONSTERFIGHT,
    PUBLICIZE,
    PLAY,
    EXPLORE,
    STRUGGLE,
}
