/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits;

import jasbro.game.character.Charakter;
import jasbro.game.character.activities.BusinessMainActivity;
import jasbro.game.character.activities.RunningActivity;
import jasbro.game.character.attributes.Attribute;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.attributes.EssentialAttributes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.battle.Attack;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.traits.Perks;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.TraitEffect;
import jasbro.game.character.traits.perktrees.AlchemistPerks;
import jasbro.game.character.traits.perktrees.BartenderPerks;
import jasbro.game.character.traits.perktrees.DancerPerks;
import jasbro.game.character.traits.perktrees.DominatrixPerks;
import jasbro.game.character.traits.perktrees.FighterPerks;
import jasbro.game.character.traits.perktrees.FurryPerks;
import jasbro.game.character.traits.perktrees.KinkyPerks;
import jasbro.game.character.traits.perktrees.LegacyPerks;
import jasbro.game.character.traits.perktrees.MaidPerks;
import jasbro.game.character.traits.perktrees.MarketingPerks;
import jasbro.game.character.traits.perktrees.NursePerks;
import jasbro.game.character.traits.perktrees.SexPerks;
import jasbro.game.character.traits.perktrees.SlavePerks;
import jasbro.game.character.traits.perktrees.ThiefPerks;
import jasbro.game.character.traits.perktrees.TrainerPerks;
import jasbro.game.character.traits.perktrees.WhorePerks;
import jasbro.game.events.MyEvent;
import jasbro.game.events.business.Customer;
import jasbro.game.interfaces.MinObedienceModifier;
import jasbro.game.interfaces.Person;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.List;

public enum Trait implements MinObedienceModifier
{
    LOVELY(200, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, 0.3f)),
    FIT(200, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STAMINA, 0.3f)),
    STRONG(200, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STRENGTH, 0.3f)),
    CLEVER(200, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, 0.3f)),
    LEADER(200, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.COMMAND, 0.3f)),
    UGLY(-100, LOVELY, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, -0.3f)),
    UNFIT(-100, FIT, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STAMINA, -0.3f)),
    WEAK(-100, STRONG, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STRENGTH, -0.3f)),
    STUPID(-100, CLEVER, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, -0.3f)),
    FOLLOWER(-100, LEADER, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.COMMAND, -0.3f)),
    FRIGID(-100, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.05f, Sextype.values())),
    NATURAL(200, FRIGID, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.05f, Sextype.values())),
    NUMB(-200, (TraitEffect)new TraitEffect.Numb()),
    SENSITIVE(200, NUMB, (TraitEffect)new TraitEffect.Sensitive()),
    BITER(-200, (TraitEffect)new TraitEffect.Biter()),
    SENSUALTONGUE(200, BITER, (TraitEffect)new TraitEffect.SensualTongue()),
    DEADFISH(-200, (TraitEffect)new TraitEffect.DeadFish()),
    AMBITOUSLOVER(200, DEADFISH, (TraitEffect)new TraitEffect.AmbitousLover()),
    SINGLEMINDED(-200, (TraitEffect)new TraitEffect.SingleMinded()),
    MULTIFACETED(200, SINGLEMINDED, (TraitEffect)new TraitEffect.Multifaceted()),
    CLUMSY(-300, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.1f, SpecializationAttribute.COOKING, SpecializationAttribute.CLEANING, SpecializationAttribute.BARTENDING)),
    HELPFUL(400, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.1f, SpecializationAttribute.COOKING, SpecializationAttribute.CLEANING, SpecializationAttribute.BARTENDING)),
    PERSONALMAID(200, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.CLEANING)),
    DIRTYDEVIL(-200, PERSONALMAID, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.CLEANING)),
    RESTAURATEUR(200, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.COOKING)),
    COOKINGDISASTER(-200, RESTAURATEUR, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.COOKING)),
    ALTRUISTIC(200, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.MAGIC, SpecializationAttribute.MEDICALKNOWLEDGE)),
    SELFISH(-200, ALTRUISTIC, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.MAGIC, SpecializationAttribute.MEDICALKNOWLEDGE)),
    FURFANATIC(200, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.CATGIRL)),
    PROHUMANIST(-200, FURFANATIC, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.CATGIRL)),
    WILLINGSUBJECT(200, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, Sextype.BONDAGE)),
    RESISTANTVICTIM(-200, WILLINGSUBJECT, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, Sextype.BONDAGE)),
    BIGBOOBS(200, (TraitEffect)new TraitEffect.BigBoobs()),
    SMALLBOOBS(-50, (TraitEffect)new TraitEffect.SmallBoobs()),
    LOLI(0, (TraitEffect)new TraitEffect.Loli()),
    SHOTA(0, (TraitEffect)new TraitEffect.Loli()),
    FURRY(5000, (TraitEffect)new TraitEffect.Furry()),
    BESTIAL(5000, (TraitEffect)new TraitEffect.Bestial()),
    MORPHFELINE(0, (TraitEffect)new TraitEffect.MorphFeline()),
    MORPHCANINE(0, (TraitEffect)new TraitEffect.MorphCanine()),
    MORPHVULPINE(0, (TraitEffect)new TraitEffect.MorphVulpine()),
    MORPHREPTILIAN(0, (TraitEffect)new TraitEffect.MorphReptilian()),
    MORPHAVIAN(0, (TraitEffect)new TraitEffect.MorphAvian()),
    MORPHAQUATIC(0, (TraitEffect)new TraitEffect.MorphAquatic()),
    MORPHINSECT(0, (TraitEffect)new TraitEffect.MorphInsect()),
    MORPHLAGOMORPH(0, (TraitEffect)new TraitEffect.MorphLagomorph()),
    FEISTY(-300, (TraitEffect)new TraitEffect.Feisty()),
    OBEDIENT(300, FEISTY, (TraitEffect)new TraitEffect.Obedient()),
    NYMPHO(300, (TraitEffect)new TraitEffect.Nympho()),
    HEALTHY(400, (TraitEffect)new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 10)),
    SICKLY(-300, HEALTHY, (TraitEffect)new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),
    PERSEVERING(400, (TraitEffect)new TraitEffect.AttributeMaxModifier(EssentialAttributes.ENERGY, 10)),
    FLABBY(-300, PERSEVERING, (TraitEffect)new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),
    OPENMINDED(200, (TraitEffect)new TraitEffect.OpenMinded()),
    RESERVED(-200, OPENMINDED, (TraitEffect)new TraitEffect.Reserved()),
    NIMBLEFINGERS(200, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, SpecializationAttribute.PICKPOCKETING)),
    GRUBBYMITTS(-200, NIMBLEFINGERS, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(-0.3f, SpecializationAttribute.PICKPOCKETING)),
    SHY(-200, (TraitEffect)new TraitEffect.Shy()),
    UNINHIBITED(100, SHY, (TraitEffect)new TraitEffect.Uninhibited()),
    NICEBODY(100, (TraitEffect)new TraitEffect.Nicebody()),
    STIFF(-100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.STRIP, -0.3f)),
    SLUT(300, (TraitEffect)new TraitEffect.Slut()),
    FRAGILE(-400, (TraitEffect)new TraitEffect.Fragile()),
    WILD(200, (TraitEffect)new TraitEffect.Wild()),
    TSUNDERE(-150, (TraitEffect)new TraitEffect.Tsundere()),
    ABSORPTION(100, (TraitEffect)new TraitEffect.Absorption()),
    LOYAL,
    RAPACIOUS(LOYAL),
    FORMERNOBLE(1000),
    RARESLAVE(1000),
    EXTREMELYRARESLAVE(100000),
    EXTREMELYRARESLAVE2(1000000),
    UNSELLABLE(0, (TraitEffect)new TraitEffect.Unsellable()),
    CLONE(-50000),
    PERVERT(true, 100, (TraitEffect)new KinkyPerks.Pervert()),
    KINKY(true, 100, (TraitEffect)new KinkyPerks.Kinky()),
    MEATTOILET(true, 100, (TraitEffect)new KinkyPerks.MeatToilet()),
    SEXADDICT(true, 100, (TraitEffect)new KinkyPerks.SexAddict()),
    INSATIABLE(true, (TraitEffect)new Perks.AttributeMaxInfluence(EssentialAttributes.ENERGY, BaseAttributeTypes.STAMINA, 2.0f)),
    SUBMISSIVE(true, 300, (TraitEffect)new KinkyPerks.Submissive()),
    EXHIBITIONIST(true, 300, (TraitEffect)new KinkyPerks.Exhibitionist()),
    SEXMANIAC(true, 300, (TraitEffect)new KinkyPerks.SexManiac()),
    SEXFREAK(true, 400, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.9f, Sextype.GROUP, Sextype.MONSTER, Sextype.BONDAGE)),
    CUMSLUT(true, 300, (TraitEffect)new KinkyPerks.CumSlut()),
    PUBLICUSE(true, 100),
    BREEDER(true, 300, (TraitEffect)new KinkyPerks.Breeder()),
    GANGBANGQUEEN(true, 300, (TraitEffect)new KinkyPerks.GangbangQueen()),
    FLESHTOY(true, 300, (TraitEffect)new KinkyPerks.FleshToy()),
    MONSTERSOW(true, 100, (TraitEffect)new KinkyPerks.MonsterSow()),
    ANYPLACE(true, 300, (TraitEffect)new KinkyPerks.AnyplaceAnywhereAnytime()),
    FROMDUSKTILLDAWN(true, 300, (TraitEffect)new KinkyPerks.FromDuskTillDawn()),
    BESTIALFEATURES(true, 0, (TraitEffect)new FurryPerks.BestialFeatures()),
    AVIAN(true, 0, (TraitEffect)new FurryPerks.Avian()),
    AVIANPICKUP(true, 0, (TraitEffect)new FurryPerks.AvianPickup()),
    AVIANFLIGHT(true, 0),
    AVIANDRAG(true, 0),
    AVIANBIRDBRAIN(true, 0, (TraitEffect)new FurryPerks.AvianBirdbrain()),
    BOVINE(true, 0, (TraitEffect)new FurryPerks.Bovine()),
    REPTILIAN(true, 0, (TraitEffect)new FurryPerks.Reptilian()),
    EXTRACTABLECLAWS(true, 0, (TraitEffect)new FurryPerks.ExtractableClaws()),
    REPTILIANARMOR(true, 0, (TraitEffect)new FurryPerks.ReptilianArmor()),
    REPTILIANMOTIVATION(true, 0, (TraitEffect)new FurryPerks.ReptilianMotivation()),
    REPTILIANDOWNSIDE(true, 0, (TraitEffect)new FurryPerks.ReptilianDownside()),
    FLAMEBREATH(true, 0, (TraitEffect)new FurryPerks.FlameBreath()),
    CANINE(true, 0, (TraitEffect)new FurryPerks.Canine()),
    CANINESLEEP(true, 0, (TraitEffect)new FurryPerks.CanineSleep()),
    CANINECOMMAND(true, 0, (TraitEffect)new FurryPerks.CanineCommand()),
    FELINE(true, 0, (TraitEffect)new FurryPerks.Feline()),
    FELINEHEAT(true, 0),
    FELINESTRIP(true, 0),
    NOCTURNAL(true, 100, (TraitEffect)new FurryPerks.Nocturnal()),
    CATNAP(true, 100, (TraitEffect)new FurryPerks.CatNap()),
    VULPINE(true, 0, (TraitEffect)new FurryPerks.Vulpine()),
    BEASTINHEAT(true, 0, (TraitEffect)new FurryPerks.BeastInHeat()),
    AQUATIC(true, 0, (TraitEffect)new FurryPerks.Aquatic()),
    RELENTLESSBEAST(true, 300, (TraitEffect)new FurryPerks.RelentlessBeast()),
    AQUATICDOWNSIDE(true, 0, (TraitEffect)new FurryPerks.AquaticDownside()),
    AQUATICNURSE(true, 0, (TraitEffect)new FurryPerks.AquaticNurse()),
    AQUATICSWIM(true, 0, (TraitEffect)new FurryPerks.AquaticSwim()),
    INSECT(true, 0, (TraitEffect)new FurryPerks.Insect()),
    INSECTGATHER(true, 0, (TraitEffect)new FurryPerks.InsectGather()),
    INSECTRESILIENCE(true, 0, (TraitEffect)new FurryPerks.InsectResilience()),
    INSECTEFFICIENCY(true, 0, (TraitEffect)new FurryPerks.InsectEfficiency()),
    INSECTSEVERYWHERE(true, 0, (TraitEffect)new FurryPerks.InsectsEverywhere()),
    ARACHNID(true, 0, (TraitEffect)new FurryPerks.Arachnid()),
    OVIPOSITION(true, 0, (TraitEffect)new FurryPerks.Oviposition()),
    INHUMANPREGNANCY(true, 0),
    HEARTOFTHESWARM(true, 0, (TraitEffect)new FurryPerks.HeartOfTheSwarm()),
    AUTONOMOUSPERK(true, 0),
    AUTONOMOUS(0),
    ARACHNIDSLEEP(true, 0, (TraitEffect)new FurryPerks.ArachnidSleep()),
    LAGOMORPH(true, 0, (TraitEffect)new FurryPerks.Lagomorph()),
    LAGOMORPHENDURANCE(true, 0, (TraitEffect)new FurryPerks.LagomorphEndurance()),
    LAGOMORPHQUICKY(true, 0),
    LAGOMORPHORGY(true, 0, (TraitEffect)new FurryPerks.LagomorphOrgy()),
    LAGOMORPHHORNY(true, 0, (TraitEffect)new FurryPerks.LagomorphHorny()),
    TRANSFORMATION(0),
    PRACTICAL(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.CLEANING, BaseAttributeTypes.INTELLIGENCE, 0.2f)),
    DIETEXPERT(true, 300, (TraitEffect)new MaidPerks.DietExpert()),
    MOTHERLYCARE(true, 300, (TraitEffect)new MaidPerks.MotherlyCare()),
    TIMEMANIPULATION(true, 100, (TraitEffect)new MaidPerks.TimeManipulation()),
    PROFESSIONAL(true, 100, (TraitEffect)new MaidPerks.Professional()),
    CHEF(true, 100, (TraitEffect)new MaidPerks.Chef()),
    COMPULSIVECLEANER(true, 300, (TraitEffect)new MaidPerks.CompulsiveCleaner()),
    ALWAYSIMPROVE(true, 300, (TraitEffect)new MaidPerks.AlwaysImprove()),
    HOUSEFAIRY(true, 300, (TraitEffect)new MaidPerks.HouseFairy()),
    WASHINGANDIRONING(true, 300, (TraitEffect)new MaidPerks.WashingAndIroning()),
    ELEGANT(true, 1000, (TraitEffect)new MaidPerks.Elegant()),
    CORDONBLEU(true, 300, (TraitEffect)new MaidPerks.CordonBleu()),
    CULINARYDELIGHTS(true, 300, (TraitEffect)new MaidPerks.CulinaryDelights()),
    ONENIGHT(true, 100, (TraitEffect)new WhorePerks.OneNight()),
    SEDUCTRESS(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.CHARISMA, SpecializationAttribute.SEDUCTION, 0.2f)),
    ENDURANCE(true, 100, (TraitEffect)new WhorePerks.Endurance()),
    CHATTY(true, 100, (TraitEffect)new WhorePerks.Chatty()),
    COMPETITIVE(true, 100, (TraitEffect)new WhorePerks.Competitive()),
    COUPLEMORE(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(40.0, null, CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT)),
    SLOPPY(true, 100, (TraitEffect)new WhorePerks.Sloppy()),
    SITBACK(true, 100, (TraitEffect)new WhorePerks.SitBack()),
    FIRST(true, 100, (TraitEffect)new WhorePerks.MyFirst()),
    WENCH(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.BARTENDING, SpecializationAttribute.SEDUCTION, 0.25f)),
    STYLISH(true, 100, (TraitEffect)new WhorePerks.BeautySleep()),
    JUSTYOUANDME(true, 100, (TraitEffect)new WhorePerks.YouAndMe()),
    NUTBUSTER(true, 1000),
    TAKEOURTIME(true, 100, (TraitEffect)new WhorePerks.OurTime()),
    QUICKIE(true, 100),
    KEEPEMCOMING(true, 100, (TraitEffect)new WhorePerks.KeepEmComing()),
    THENIGHTISSTILLYOUNG(true, 100, (TraitEffect)new WhorePerks.Renowed()),
    NONNEGOCIABLE(true, 100, (TraitEffect)new WhorePerks.HighClass()),
    STREETSMARTS(true, 1000),
    THATGIRL(true, 1000),
    PURE(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.STRIP, BaseAttributeTypes.CHARISMA, 0.3f)),
    LEWD(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.STRIP, SpecializationAttribute.SEDUCTION, 0.2f)),
    CROWDLOVER(true, 100, (TraitEffect)new DancerPerks.CrowdLover()),
    REFINED(true, 100, (TraitEffect)new DancerPerks.Refined()),
    LASCIVIOUS(true, 100, (TraitEffect)new DancerPerks.Lascivious()),
    SMELLSLIKEKITTEN(true, 100),
    TRENDY(true, 100, (TraitEffect)new DancerPerks.Trendy()),
    TANLINES(true, 100),
    SKINCARE(true, 100),
    TEASER(true, 100, (TraitEffect)new DancerPerks.Teaser()),
    ACROBATICS(true, 100, (TraitEffect)new DancerPerks.Acrobatics()),
    NICEHIPS(true, 100, (TraitEffect)new DancerPerks.NiceHips()),
    PERFECTCONDITION(true, 400, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, BaseAttributeTypes.STAMINA, BaseAttributeTypes.CHARISMA)),
    ORIENTALCHARMS(true, 100, (TraitEffect)new DancerPerks.OrientalCharms()),
    THEUNTOUCHABLE(true, 100, (TraitEffect)new DancerPerks.TheUntouchable()),
    EXTRAS(true, 100),
    HORNY(true, 100, (TraitEffect)new DancerPerks.Horny()),
    TARGETAUDIENCE(true, 100, (TraitEffect)new DancerPerks.TargetAudience()),
    NIGHTSHIFT(true, 100, (TraitEffect)new BartenderPerks.NightShift()),
    CLASSY(true, 200, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, 0.3f)),
    LIQUORMASTER(true, 100, (TraitEffect)new BartenderPerks.LiquorMaster()),
    MULTITASKING(true, 100, (TraitEffect)new BartenderPerks.Multitasking()),
    THECONFIDENT(true, 100, (TraitEffect)new BartenderPerks.TheConfident()),
    FLIRTY(true, 100),
    DATASS(true, 100),
    CATMAID(true, 100, (TraitEffect)new BartenderPerks.CatMaid()),
    UNDERTHETABLE(true, 100),
    OUTGOING(true, 100, (TraitEffect)new BartenderPerks.Outgoing()),
    SHARPSENSES(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(5.0, null, CalculatedAttribute.HIT, CalculatedAttribute.DODGE)),
    WEAPONMASTERY(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(null, 1.0, CalculatedAttribute.DAMAGE)),
    MINDOFTHEFIGHTER(true, 100, (TraitEffect)new FighterPerks.MindOfTheFighter()),
    ELEMENTALSTUDY(true, 100, (TraitEffect)new FighterPerks.ElementalStudy()),
    TOUGH(true, (TraitEffect)new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 15)),
    DISTRACTION(true, 100, (TraitEffect)new FighterPerks.Distraction()),
    PLUNDERER(true, 100, (TraitEffect)new FighterPerks.Plunderer()),
    LOSTARTS(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.HIT, CalculatedAttribute.DAMAGE, CalculatedAttribute.SPEED, CalculatedAttribute.CRITCHANCE)),
    VITALPOINTSPIERCING(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(15.0, null, CalculatedAttribute.CRITDAMAGEAMOUNT)),
    CASTTIME(true, 100, (TraitEffect)new FighterPerks.CastTime()),
    IRONBODY(true, 100, (TraitEffect)new FighterPerks.IronBody()),
    ETHERSHIELD(true, 100, (TraitEffect)new FighterPerks.EtherShield()),
    MKIIWALKER(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(40.0, null, CalculatedAttribute.BLOCKAMOUNT)),
    SHOWTIME(true, 1000),
    GREEDY(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(25.0, null, CalculatedAttribute.STEALAMOUNTMODIFIER)),
    NIMBLEHANDS(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(20.0, null, CalculatedAttribute.STEALCHANCE)),
    PICKPOCKET(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.STEALITEMCHANCE)),
    ROGUE(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(4.0, null, CalculatedAttribute.CRITCHANCE, CalculatedAttribute.DAMAGE, CalculatedAttribute.DODGE)),
    LUPIN(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, BaseAttributeTypes.INTELLIGENCE, 0.15f)),
    BANDIT(true, 400, (TraitEffect)new TraitEffect.MultipleAttributeChangeInfluence(0.3f, BaseAttributeTypes.STAMINA, BaseAttributeTypes.STRENGTH)),
    CONARTIST(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, BaseAttributeTypes.CHARISMA, 0.2f)),
    RESELLER(true, 1000),
    LIAISONSDANGEREUSES(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, SpecializationAttribute.SEDUCTION, 0.3f)),
    NIGHTSHADE(true, 100, (TraitEffect)new ThiefPerks.NightShade()),
    SHADOWBODY(true, 100, (TraitEffect)new ThiefPerks.ShadowBody()),
    DOUBLEVIE(true, 100, (TraitEffect)new ThiefPerks.DoubleVie()),
    ASSASSIN(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(30.0, null, CalculatedAttribute.CRITDAMAGEAMOUNT)),
    PHANTOMTHIEF(true, 100),
    GROOMING(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.CHARISMA, SpecializationAttribute.CATGIRL, 0.1f)),
    STRAYCAT(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.OBEDIENCE, SpecializationAttribute.CATGIRL, -0.1f)),
    DOMESTICATED(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.OBEDIENCE, SpecializationAttribute.CATGIRL, 0.1f)),
    CATSANDRATS(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.STAMINA, SpecializationAttribute.CATGIRL, 0.1f)),
    CUNNING(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.INTELLIGENCE, SpecializationAttribute.CATGIRL, 0.1f)),
    PAWERFUL(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.STRENGTH, SpecializationAttribute.CATGIRL, 0.1f)),
    ALLPURRPOSE(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.CLEANING, SpecializationAttribute.CATGIRL, 0.1f)),
    CATBURGLAR(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, SpecializationAttribute.CATGIRL, 0.1f)),
    WETPUSSY(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.SEDUCTION, SpecializationAttribute.CATGIRL, 0.1f)),
    CATTRACTIVE(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.ADVERTISING, SpecializationAttribute.CATGIRL, 0.1f)),
    CATWALK(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.STRIP, SpecializationAttribute.CATGIRL, 0.1f)),
    HIGHCATNESSRATING(true, 400, (TraitEffect)new TraitEffect.AttributeMaxModifier(SpecializationAttribute.CATGIRL, 96)),
    CATSAREASSHOLES(true, 100),
    CERTIFIEDTRAINER(true, 500, (TraitEffect)new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    GOODSLAVE(true, 50, (TraitEffect)new SlavePerks.GoodSlave()),
    BASICTRAINING1(true, 100, (TraitEffect)new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    BASICTRAINING2(true, 100, (TraitEffect)new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    BASICTRAINING3(true, 100, (TraitEffect)new TraitEffect.BaseAttributeChangeInfluence(0.1f)),
    SEXTRAINING1(true, 100, (TraitEffect)new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SEXTRAINING2(true, 100, (TraitEffect)new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SEXTRAINING3(true, 100, (TraitEffect)new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SPECIALIZATIONTRAINING1(true, 100, (TraitEffect)new TraitEffect.SpecializationAttributeChangeInfluence(0.1f)),
    SPECIALIZATIONTRAINING2(true, 100, (TraitEffect)new TraitEffect.SpecializationAttributeChangeInfluence(0.1f)),
    SPECIALIZATIONTRAINING3(true, 100, (TraitEffect)new TraitEffect.SpecializationAttributeChangeInfluence(0.1f)),
    PERFECTTRAINER(true, 500, (TraitEffect)new TraitEffect.AllAttributeChangeInfluence(0.3f)),
    PERFECTSLAVE(true, 500, (TraitEffect)new TraitEffect.AllAttributeChangeInfluence(0.3f)),
    TRAINING(true, 100, (TraitEffect)new TraitEffect.AllAttributeChangeInfluence(0.05f)),
    EFFECTIVETRAINER(true, 100, (TraitEffect)new TrainerPerks.EffectiveTrainer()),
    MOTIVATOR(true, 100, (TraitEffect)new TrainerPerks.Motivator()),
    RESPECTED(true, 100, (TraitEffect)new TraitEffect.InfluenceAttributeLoss(BaseAttributeTypes.COMMAND, -0.35f)),
    ONEOFUS(true, 100, (TraitEffect)new TraitEffect.InfluenceAttributeLoss(BaseAttributeTypes.COMMAND, -0.75f)),
    STRONGPRESENCE(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.CONTROL)),
    EYESEVERYWHERE(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(20.0, null, CalculatedAttribute.CONTROL)),
    SPYNETWORK(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(50.0, null, CalculatedAttribute.CONTROL)),
    CONTENTPERK(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(3.0, null, CalculatedAttribute.CONTROL)),
    TRUSTEDSLAVE(true, 100, (TraitEffect)new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.CONTROL)),
    GENIUS(true, 500, (TraitEffect)new TraitEffect.BaseAttributeChangeInfluence(0.3f)),
    LEGACYWHORE(true, 100),
    LEGACYSTRIPPER(true, 100),
    LEGACYMAID(true, 100),
    LEGACYBARTENDER(true, 100),
    LEGACYMASSEUR(true, 100),
    LEGACYADVENTURER(true, 100),
    LEGACYNONE(true, 100, (TraitEffect)new LegacyPerks.LegacyNone()),
    LEGACYWHORE2(true, 100),
    LEGACYSTRIPPER2(true, 100),
    LEGACYMAID2(true, 100),
    LEGACYBARTENDER2(true, 100),
    LEGACYMASSEUR2(true, 100),
    LEGACYADVENTURER2(true, 100),
    LEGACYNONE2(true, 100),
    HIDDENLIBRARY(true, 100),
    TOUGHERMISSIONS1(true, 100),
    TOUGHERMISSIONS2(true, 100),
    TOUGHERMISSIONS3(true, 100),
    TOUGHERMISSIONS4(true, 100),
    TOUGHERMISSIONS5(true, 100),
    DISCOUNTSCHOOL(true, 100),
    DISCOUNTLIBRARY(true, 100),
    DISCOUNTSLAVES(true, 100),
    DISCOUNTSHOPS(true, 100),
    TAXEVASION(true, 100),
    BENEFACTORSTREETS(true, 0, (TraitEffect)new LegacyPerks.BenefactorStreets()),
    BENEFACTORSHOPS(true, 0, (TraitEffect)new LegacyPerks.BenefactorShops()),
    BENEFACTORSLAVEMARKET(true, 0, (TraitEffect)new LegacyPerks.BenefactorSlavemarket()),
    BENEFACTORCARPENTERS(true, 0, (TraitEffect)new LegacyPerks.BenefactorCarpenters()),
    BENEFACTORKINGDOM(true, 0, (TraitEffect)new LegacyPerks.BenefactorKingdom()),
    DEBUTANTE(true, 100, (TraitEffect)new TraitEffect.SexAttributeChangeInfluence(0.1f)),
    SENSITIVECLIT(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(Sextype.VAGINAL, 0.2f)),
    DEEPLOVE(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(Sextype.ANAL, 0.2f)),
    LOVETHETASTE(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(Sextype.ORAL, 0.2f)),
    AFLEURDEPEAU(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(Sextype.FOREPLAY, 0.2f)),
    MEATBUNS(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(Sextype.TITFUCK, 0.2f)),
    COZYCUNT(true, 100, (TraitEffect)new SexPerks.CozyCunt()),
    ROWDYRUMP(true, 100, (TraitEffect)new SexPerks.RowdyRump()),
    SLURPYSLURP(true, 100, (TraitEffect)new SexPerks.SlurpySlurp()),
    TOUCHYFEELY(true, 100, (TraitEffect)new SexPerks.TouchyFeely()),
    PUFFPUFF(true, 100, (TraitEffect)new SexPerks.PuffPuff()),
    BEDROOMPRINCESS(true, 100, (TraitEffect)new SexPerks.BedroomPrincess()),
    WETFORYOU(true, 100, (TraitEffect)new SexPerks.WetForYou()),
    BACKDOOROPEN(true, 100, (TraitEffect)new SexPerks.BackdoorOpen()),
    THIRSTY(true, 100, (TraitEffect)new SexPerks.Thirsty()),
    FEELMEUP(true, 100, (TraitEffect)new SexPerks.FeelMeUp()),
    COMETOMOMMY(true, 100, (TraitEffect)new SexPerks.ComeToMommy()),
    STEAMY(true, 100, (TraitEffect)new SexPerks.Steamy()),
    BEDGODDESS(true, 100, (TraitEffect)new SexPerks.BedGoddess()),
    INITIATE(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.ADVERTISING, 0.2f)),
    SAMPLINGTHEGOODS(true, 1000),
    SHOWINGTHEGOODS(true, 1000),
    SHOWOFF(true, 1000),
    CATCHY(true, 1000),
    HEYGUYSBOOSE(true, 1000),
    SPIRITED(true, 100, (TraitEffect)new MarketingPerks.Spirited()),
    TARGETBUM(true, 100, (TraitEffect)new MarketingPerks.TargetBum()),
    TARGETPEASANT(true, 100, (TraitEffect)new MarketingPerks.TargetPeasants()),
    TARGETSOLDIER(true, 100, (TraitEffect)new MarketingPerks.TargetSoldiers()),
    TARGETBUSINESSMEN(true, 100, (TraitEffect)new MarketingPerks.TargetBusinessmen()),
    CONFIRMEDSALESPERSON(true, 100, (TraitEffect)new MarketingPerks.Salesperson()),
    SALESPROMOTION(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.ADVERTISING, BaseAttributeTypes.INTELLIGENCE, 0.1f)),
    NICHEMARKETINGNOBLE(true, 100, (TraitEffect)new MarketingPerks.TargetNobles()),
    NICHEMARKETINGLORD(true, 100, (TraitEffect)new MarketingPerks.TargetLords()),
    NICHEMARKETINGCELEBRITY(true, 100, (TraitEffect)new MarketingPerks.TargetCelebrities()),
    NICHEMARKETINGGROUPS(true, 100, (TraitEffect)new MarketingPerks.TargetGroups()),
    RECOGNIZED(true, 100, (TraitEffect)new MarketingPerks.Recognized()),
    BUSINESSRELATIONS(true, 100),
    ERUDITE(true, 100, (TraitEffect)new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, 0.3f)),
    SELFTAUGHT(true, 100, (TraitEffect)new AlchemistPerks.SelfTaught()),
    GATEOFTRUTH(true, 400, (TraitEffect)new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.MAGIC, 1.0f)),
    GREENTHUMB(true, 100, (TraitEffect)new AlchemistPerks.GreenThumb()),
    FLOWERARRANGEMENT(true, 100),
    APHRODISIACS(true, 100, (TraitEffect)new AlchemistPerks.Aphrodisiacs()),
    CALMINGINCENCES(true, 100, (TraitEffect)new AlchemistPerks.RelaxingIncences()),
    DARKRITUAL(true, 100),
    PERSONNALOFFERING(true, 100),
    MANAFLOW(true, (TraitEffect)new Perks.AttributeInfluence(BaseAttributeTypes.STAMINA, SpecializationAttribute.MAGIC, 0.15f)),
    ETHERWALKER(true, (TraitEffect)new Perks.AttributeInfluence(SpecializationAttribute.AGILITY, SpecializationAttribute.MAGIC, 0.15f)),
    BENEVOLENT(true, 100),
    BREWER(true, 100, (TraitEffect)new NursePerks.Brewer()),
    TANTRIC(true, 100),
    MAGICALHEALING(true, 100),
    LOVEANDCARE(true, 100, (TraitEffect)new NursePerks.LoveAndCare()),
    ONSENPRINCESS(true, 100),
    OILY(true, 100),
    CASTCURE(true, 100),
    FIRSTAID(true, 100),
    COSMETICS(true, 100),
    SEXPERT(true, 100, (TraitEffect)new NursePerks.SeXpert()),
    BLESSEDAURA(true, 100, (TraitEffect)new NursePerks.BlessedAura()),
    GIVEANDTAKE(true, 100, (TraitEffect)new DominatrixPerks.GiveAndTake()),
    CRUELMASTER(true, 100, (TraitEffect)new DominatrixPerks.CruelMaster()),
    HITMEHARDER(true, 100, (TraitEffect)new DominatrixPerks.HitMeHarder()),
    PLEASURETHROUGHPAIN(true, 100, (TraitEffect)new DominatrixPerks.PleasureThroughPain()),
    PLEASUREANDPAIN(true, 100, (TraitEffect)new DominatrixPerks.PleasureAndPain()),
    PLEASUREINPAIN(true, 100, (TraitEffect)new DominatrixPerks.PleasureInPain()),
    MYWHIPISALLINEED(true, 100, (TraitEffect)new DominatrixPerks.MyWhipIsAllINeed()),
    SADIST(true, 100, (TraitEffect)new DominatrixPerks.Sadist()),
    GOODMASTERSARETHEBESTSUBS(true, 200, (TraitEffect)new DominatrixPerks.GoodMastersAreTheBestSubs()),
    MASOCHIST(true, 300, (TraitEffect)new DominatrixPerks.Masochist()),
    LEATHERQUEEN(true, 300, (TraitEffect)new DominatrixPerks.LeatherQueen()),
    EVERYONEBEHAVE(true, 100, (TraitEffect)new DominatrixPerks.EveryoneBehave()),
    AGGRESSIVEADVERTISEMENT(true, 100, (TraitEffect)new DominatrixPerks.AggressiveAdvertisement()),
    SQUEALANDHEAL(true, 100, (TraitEffect)new DominatrixPerks.SquealAndHeal()),
    LICKITUP(true, 200, (TraitEffect)new DominatrixPerks.LickItUp()),
    LEATHERMISTRESS(true, 300, (TraitEffect)new DominatrixPerks.LeatherMistress()),
    WHATDOESNTKILLYOU(true, 300, (TraitEffect)new DominatrixPerks.KillYou()),
    MASTERANDSLAVE(true, 300, (TraitEffect)new DominatrixPerks.MasterAndSlave());

    private boolean perk = false;
    private int valueModifier = 0;
    private Trait opposingTrait;
    private TraitEffect traitEffect;
    private String text = this.toString();

    private Trait() {
        this(false, 0, null, null);
    }

    private Trait(int valueModifier) {
        this(false, valueModifier);
    }

    private Trait(Trait opposingTrait) {
        this(false, 0, opposingTrait, null);
    }

    private Trait(boolean perk, int valueModifier) {
        this(perk, valueModifier, null);
    }

    private Trait(boolean perk, TraitEffect traitEffect) {
        this(perk, 0, traitEffect);
    }

    private Trait(int valueModifier, TraitEffect traitEffect) {
        this(false, valueModifier, traitEffect);
    }

    private Trait(boolean perk, int valueModifier, TraitEffect traitEffect) {
        this(perk, valueModifier, null, traitEffect);
    }

    private Trait(int valueModifier, Trait opposingTrait, TraitEffect traitEffect) {
        this(false, valueModifier, opposingTrait, traitEffect);
    }

    private Trait(boolean perk, int valueModifier, Trait opposingTrait, TraitEffect traitEffect) {
        this.perk = perk;
        this.valueModifier = valueModifier;
        this.opposingTrait = opposingTrait;
        if (opposingTrait != null && opposingTrait.opposingTrait == null) {
            opposingTrait.opposingTrait = this;
        }
        this.traitEffect = traitEffect;
    }

    public void handleEvent(MyEvent e, Charakter character) {
        if (this.traitEffect != null) {
            this.traitEffect.handleEvent(e, character, this);
        }
    }

    @Override
    public int getMinObedienceModified(int curMinObedience, Charakter character, RunningActivity activity) {
        if (this.traitEffect != null) {
            return this.traitEffect.getMinObedienceModified(curMinObedience, character, activity);
        }
        return curMinObedience;
    }

    public boolean addTrait(Charakter character, List<Trait> traits) {
        Trait opposedTrait = null;
        for (Trait curTrait : traits) {
            if (!this.isOpposed(curTrait)) continue;
            opposedTrait = curTrait;
            break;
        }
        if (opposedTrait == null) {
            if (this.traitEffect != null) {
                return this.traitEffect.addTrait(character);
            }
            return true;
        }
        character.removeTrait(opposedTrait);
        return false;
    }

    public boolean removeTrait(Charakter character) {
        if (this.traitEffect != null) {
            return this.traitEffect.removeTrait(character);
        }
        return true;
    }

    public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
        if (this.traitEffect != null) {
            return this.traitEffect.getAttributeModified(calculatedAttribute, currentValue, character);
        }
        return currentValue;
    }

    public boolean isOpposed(Trait trait) {
        if (this == BIGBOOBS && (trait == LOLI || trait == SMALLBOOBS)) {
            return true;
        }
        if ((this == LOLI || this == SMALLBOOBS) && trait == BIGBOOBS) {
            return true;
        }
        return this.opposingTrait != null && trait == this.opposingTrait;
    }

    public static List<Trait> getBasicTraits() {
        ArrayList<Trait> traits = new ArrayList<Trait>();
        for (Trait trait : Trait.values()) {
            if (trait.isPerk()) continue;
            traits.add(trait);
        }
        return traits;
    }

    public int getValueModifier() {
        return this.valueModifier;
    }

    public boolean isPerk() {
        return this.perk;
    }

    public String getText() {
        return TextUtil.t(this.text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void resetText() {
        this.text = this.toString();
    }

    public String getText(Person person) {
        return TextUtil.t(this.text, person);
    }

    public String getDescription() {
        return TextUtil.t(this.text + ".description");
    }

    public String getDescriptionWithName(Charakter character) {
        return TextUtil.t(this.text + ".description", character);
    }

    public String getDescription(Person person) {
        return TextUtil.t(this.text + ".description", person);
    }

    public SkillTree getAssociatedSkillTree() {
        if (this.traitEffect != null) {
            return this.traitEffect.getSkillTree();
        }
        return null;
    }

    public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
        if (this.traitEffect != null) {
            this.traitEffect.modifyPossibleAttacks(attacks, character);
        }
    }

    public float getAttributeModifier(Attribute attribute) {
        if (this.traitEffect != null) {
            return this.traitEffect.getAttributeModifier(attribute);
        }
        return 0.0f;
    }

    public int modifyCustomerRating(int initialRating, Customer customer, BusinessMainActivity businessMainActivity) {
        if (this.traitEffect != null) {
            return this.traitEffect.modifyCustomerRating(initialRating, customer, businessMainActivity);
        }
        return initialRating;
    }
}

