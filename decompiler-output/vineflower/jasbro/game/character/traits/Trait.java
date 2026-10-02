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

public enum Trait implements MinObedienceModifier {
   LOVELY(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, 0.3F)),
   FIT(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STAMINA, 0.3F)),
   STRONG(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STRENGTH, 0.3F)),
   CLEVER(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, 0.3F)),
   LEADER(200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.COMMAND, 0.3F)),
   UGLY(-100, LOVELY, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, -0.3F)),
   UNFIT(-100, FIT, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STAMINA, -0.3F)),
   WEAK(-100, STRONG, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.STRENGTH, -0.3F)),
   STUPID(-100, CLEVER, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, -0.3F)),
   FOLLOWER(-100, LEADER, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.COMMAND, -0.3F)),
   FRIGID(-100, new TraitEffect.MultipleAttributeChangeInfluence(-0.05F, Sextype.values())),
   NATURAL(200, FRIGID, new TraitEffect.MultipleAttributeChangeInfluence(0.05F, Sextype.values())),
   NUMB(-200, new TraitEffect.Numb()),
   SENSITIVE(200, NUMB, new TraitEffect.Sensitive()),
   BITER(-200, new TraitEffect.Biter()),
   SENSUALTONGUE(200, BITER, new TraitEffect.SensualTongue()),
   DEADFISH(-200, new TraitEffect.DeadFish()),
   AMBITOUSLOVER(200, DEADFISH, new TraitEffect.AmbitousLover()),
   SINGLEMINDED(-200, new TraitEffect.SingleMinded()),
   MULTIFACETED(200, SINGLEMINDED, new TraitEffect.Multifaceted()),
   CLUMSY(
      -300,
      new TraitEffect.MultipleAttributeChangeInfluence(
         -0.1F, SpecializationAttribute.COOKING, SpecializationAttribute.CLEANING, SpecializationAttribute.BARTENDING
      )
   ),
   HELPFUL(
      400,
      new TraitEffect.MultipleAttributeChangeInfluence(
         0.1F, SpecializationAttribute.COOKING, SpecializationAttribute.CLEANING, SpecializationAttribute.BARTENDING
      )
   ),
   PERSONALMAID(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, SpecializationAttribute.CLEANING)),
   DIRTYDEVIL(-200, PERSONALMAID, new TraitEffect.MultipleAttributeChangeInfluence(-0.3F, SpecializationAttribute.CLEANING)),
   RESTAURATEUR(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, SpecializationAttribute.COOKING)),
   COOKINGDISASTER(-200, RESTAURATEUR, new TraitEffect.MultipleAttributeChangeInfluence(-0.3F, SpecializationAttribute.COOKING)),
   ALTRUISTIC(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, SpecializationAttribute.MAGIC, SpecializationAttribute.MEDICALKNOWLEDGE)),
   SELFISH(-200, ALTRUISTIC, new TraitEffect.MultipleAttributeChangeInfluence(-0.3F, SpecializationAttribute.MAGIC, SpecializationAttribute.MEDICALKNOWLEDGE)),
   FURFANATIC(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, SpecializationAttribute.CATGIRL)),
   PROHUMANIST(-200, FURFANATIC, new TraitEffect.MultipleAttributeChangeInfluence(-0.3F, SpecializationAttribute.CATGIRL)),
   WILLINGSUBJECT(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, Sextype.BONDAGE)),
   RESISTANTVICTIM(-200, WILLINGSUBJECT, new TraitEffect.MultipleAttributeChangeInfluence(-0.3F, Sextype.BONDAGE)),
   BIGBOOBS(200, new TraitEffect.BigBoobs()),
   SMALLBOOBS(-50, new TraitEffect.SmallBoobs()),
   LOLI(0, new TraitEffect.Loli()),
   SHOTA(0, new TraitEffect.Loli()),
   FURRY(5000, new TraitEffect.Furry()),
   BESTIAL(5000, new TraitEffect.Bestial()),
   MORPHFELINE(0, new TraitEffect.MorphFeline()),
   MORPHCANINE(0, new TraitEffect.MorphCanine()),
   MORPHVULPINE(0, new TraitEffect.MorphVulpine()),
   MORPHREPTILIAN(0, new TraitEffect.MorphReptilian()),
   MORPHAVIAN(0, new TraitEffect.MorphAvian()),
   MORPHAQUATIC(0, new TraitEffect.MorphAquatic()),
   MORPHINSECT(0, new TraitEffect.MorphInsect()),
   MORPHLAGOMORPH(0, new TraitEffect.MorphLagomorph()),
   FEISTY(-300, new TraitEffect.Feisty()),
   OBEDIENT(300, FEISTY, new TraitEffect.Obedient()),
   NYMPHO(300, new TraitEffect.Nympho()),
   HEALTHY(400, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 10)),
   SICKLY(-300, HEALTHY, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),
   PERSEVERING(400, new TraitEffect.AttributeMaxModifier(EssentialAttributes.ENERGY, 10)),
   FLABBY(-300, PERSEVERING, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, -10)),
   OPENMINDED(200, new TraitEffect.OpenMinded()),
   RESERVED(-200, OPENMINDED, new TraitEffect.Reserved()),
   NIMBLEFINGERS(200, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, SpecializationAttribute.PICKPOCKETING)),
   GRUBBYMITTS(-200, NIMBLEFINGERS, new TraitEffect.MultipleAttributeChangeInfluence(-0.3F, SpecializationAttribute.PICKPOCKETING)),
   SHY(-200, new TraitEffect.Shy()),
   UNINHIBITED(100, SHY, new TraitEffect.Uninhibited()),
   NICEBODY(100, new TraitEffect.Nicebody()),
   STIFF(-100, new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.STRIP, -0.3F)),
   SLUT(300, new TraitEffect.Slut()),
   FRAGILE(-400, new TraitEffect.Fragile()),
   WILD(200, new TraitEffect.Wild()),
   TSUNDERE(-150, new TraitEffect.Tsundere()),
   ABSORPTION(100, new TraitEffect.Absorption()),
   LOYAL,
   RAPACIOUS(LOYAL),
   FORMERNOBLE(1000),
   RARESLAVE(1000),
   EXTREMELYRARESLAVE(100000),
   EXTREMELYRARESLAVE2(1000000),
   UNSELLABLE(0, new TraitEffect.Unsellable()),
   CLONE(-50000),
   PERVERT(true, 100, new KinkyPerks.Pervert()),
   KINKY(true, 100, new KinkyPerks.Kinky()),
   MEATTOILET(true, 100, new KinkyPerks.MeatToilet()),
   SEXADDICT(true, 100, new KinkyPerks.SexAddict()),
   INSATIABLE(true, new Perks.AttributeMaxInfluence(EssentialAttributes.ENERGY, BaseAttributeTypes.STAMINA, 2.0F)),
   SUBMISSIVE(true, 300, new KinkyPerks.Submissive()),
   EXHIBITIONIST(true, 300, new KinkyPerks.Exhibitionist()),
   SEXMANIAC(true, 300, new KinkyPerks.SexManiac()),
   SEXFREAK(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(0.9F, Sextype.GROUP, Sextype.MONSTER, Sextype.BONDAGE)),
   CUMSLUT(true, 300, new KinkyPerks.CumSlut()),
   PUBLICUSE(true, 100),
   BREEDER(true, 300, new KinkyPerks.Breeder()),
   GANGBANGQUEEN(true, 300, new KinkyPerks.GangbangQueen()),
   FLESHTOY(true, 300, new KinkyPerks.FleshToy()),
   MONSTERSOW(true, 100, new KinkyPerks.MonsterSow()),
   ANYPLACE(true, 300, new KinkyPerks.AnyplaceAnywhereAnytime()),
   FROMDUSKTILLDAWN(true, 300, new KinkyPerks.FromDuskTillDawn()),
   BESTIALFEATURES(true, 0, new FurryPerks.BestialFeatures()),
   AVIAN(true, 0, new FurryPerks.Avian()),
   AVIANPICKUP(true, 0, new FurryPerks.AvianPickup()),
   AVIANFLIGHT(true, 0),
   AVIANDRAG(true, 0),
   AVIANBIRDBRAIN(true, 0, new FurryPerks.AvianBirdbrain()),
   BOVINE(true, 0, new FurryPerks.Bovine()),
   REPTILIAN(true, 0, new FurryPerks.Reptilian()),
   EXTRACTABLECLAWS(true, 0, new FurryPerks.ExtractableClaws()),
   REPTILIANARMOR(true, 0, new FurryPerks.ReptilianArmor()),
   REPTILIANMOTIVATION(true, 0, new FurryPerks.ReptilianMotivation()),
   REPTILIANDOWNSIDE(true, 0, new FurryPerks.ReptilianDownside()),
   FLAMEBREATH(true, 0, new FurryPerks.FlameBreath()),
   CANINE(true, 0, new FurryPerks.Canine()),
   CANINESLEEP(true, 0, new FurryPerks.CanineSleep()),
   CANINECOMMAND(true, 0, new FurryPerks.CanineCommand()),
   FELINE(true, 0, new FurryPerks.Feline()),
   FELINEHEAT(true, 0),
   FELINESTRIP(true, 0),
   NOCTURNAL(true, 100, new FurryPerks.Nocturnal()),
   CATNAP(true, 100, new FurryPerks.CatNap()),
   VULPINE(true, 0, new FurryPerks.Vulpine()),
   BEASTINHEAT(true, 0, new FurryPerks.BeastInHeat()),
   AQUATIC(true, 0, new FurryPerks.Aquatic()),
   RELENTLESSBEAST(true, 300, new FurryPerks.RelentlessBeast()),
   AQUATICDOWNSIDE(true, 0, new FurryPerks.AquaticDownside()),
   AQUATICNURSE(true, 0, new FurryPerks.AquaticNurse()),
   AQUATICSWIM(true, 0, new FurryPerks.AquaticSwim()),
   INSECT(true, 0, new FurryPerks.Insect()),
   INSECTGATHER(true, 0, new FurryPerks.InsectGather()),
   INSECTRESILIENCE(true, 0, new FurryPerks.InsectResilience()),
   INSECTEFFICIENCY(true, 0, new FurryPerks.InsectEfficiency()),
   INSECTSEVERYWHERE(true, 0, new FurryPerks.InsectsEverywhere()),
   ARACHNID(true, 0, new FurryPerks.Arachnid()),
   OVIPOSITION(true, 0, new FurryPerks.Oviposition()),
   INHUMANPREGNANCY(true, 0),
   HEARTOFTHESWARM(true, 0, new FurryPerks.HeartOfTheSwarm()),
   AUTONOMOUSPERK(true, 0),
   AUTONOMOUS(0),
   ARACHNIDSLEEP(true, 0, new FurryPerks.ArachnidSleep()),
   LAGOMORPH(true, 0, new FurryPerks.Lagomorph()),
   LAGOMORPHENDURANCE(true, 0, new FurryPerks.LagomorphEndurance()),
   LAGOMORPHQUICKY(true, 0),
   LAGOMORPHORGY(true, 0, new FurryPerks.LagomorphOrgy()),
   LAGOMORPHHORNY(true, 0, new FurryPerks.LagomorphHorny()),
   TRANSFORMATION(0),
   PRACTICAL(true, new Perks.AttributeInfluence(SpecializationAttribute.CLEANING, BaseAttributeTypes.INTELLIGENCE, 0.2F)),
   DIETEXPERT(true, 300, new MaidPerks.DietExpert()),
   MOTHERLYCARE(true, 300, new MaidPerks.MotherlyCare()),
   TIMEMANIPULATION(true, 100, new MaidPerks.TimeManipulation()),
   PROFESSIONAL(true, 100, new MaidPerks.Professional()),
   CHEF(true, 100, new MaidPerks.Chef()),
   COMPULSIVECLEANER(true, 300, new MaidPerks.CompulsiveCleaner()),
   ALWAYSIMPROVE(true, 300, new MaidPerks.AlwaysImprove()),
   HOUSEFAIRY(true, 300, new MaidPerks.HouseFairy()),
   WASHINGANDIRONING(true, 300, new MaidPerks.WashingAndIroning()),
   ELEGANT(true, 1000, new MaidPerks.Elegant()),
   CORDONBLEU(true, 300, new MaidPerks.CordonBleu()),
   CULINARYDELIGHTS(true, 300, new MaidPerks.CulinaryDelights()),
   ONENIGHT(true, 100, new WhorePerks.OneNight()),
   SEDUCTRESS(true, new Perks.AttributeInfluence(BaseAttributeTypes.CHARISMA, SpecializationAttribute.SEDUCTION, 0.2F)),
   ENDURANCE(true, 100, new WhorePerks.Endurance()),
   CHATTY(true, 100, new WhorePerks.Chatty()),
   COMPETITIVE(true, 100, new WhorePerks.Competitive()),
   COUPLEMORE(true, 100, new Perks.SimpleTraitEffect(40.0, null, CalculatedAttribute.AMOUNTCUSTOMERSPERSHIFT)),
   SLOPPY(true, 100, new WhorePerks.Sloppy()),
   SITBACK(true, 100, new WhorePerks.SitBack()),
   FIRST(true, 100, new WhorePerks.MyFirst()),
   WENCH(true, new Perks.AttributeInfluence(SpecializationAttribute.BARTENDING, SpecializationAttribute.SEDUCTION, 0.25F)),
   STYLISH(true, 100, new WhorePerks.BeautySleep()),
   JUSTYOUANDME(true, 100, new WhorePerks.YouAndMe()),
   NUTBUSTER(true, 1000),
   TAKEOURTIME(true, 100, new WhorePerks.OurTime()),
   QUICKIE(true, 100),
   KEEPEMCOMING(true, 100, new WhorePerks.KeepEmComing()),
   THENIGHTISSTILLYOUNG(true, 100, new WhorePerks.Renowed()),
   NONNEGOCIABLE(true, 100, new WhorePerks.HighClass()),
   STREETSMARTS(true, 1000),
   THATGIRL(true, 1000),
   PURE(true, new Perks.AttributeInfluence(SpecializationAttribute.STRIP, BaseAttributeTypes.CHARISMA, 0.3F)),
   LEWD(true, new Perks.AttributeInfluence(SpecializationAttribute.STRIP, SpecializationAttribute.SEDUCTION, 0.2F)),
   CROWDLOVER(true, 100, new DancerPerks.CrowdLover()),
   REFINED(true, 100, new DancerPerks.Refined()),
   LASCIVIOUS(true, 100, new DancerPerks.Lascivious()),
   SMELLSLIKEKITTEN(true, 100),
   TRENDY(true, 100, new DancerPerks.Trendy()),
   TANLINES(true, 100),
   SKINCARE(true, 100),
   TEASER(true, 100, new DancerPerks.Teaser()),
   ACROBATICS(true, 100, new DancerPerks.Acrobatics()),
   NICEHIPS(true, 100, new DancerPerks.NiceHips()),
   PERFECTCONDITION(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, BaseAttributeTypes.STAMINA, BaseAttributeTypes.CHARISMA)),
   ORIENTALCHARMS(true, 100, new DancerPerks.OrientalCharms()),
   THEUNTOUCHABLE(true, 100, new DancerPerks.TheUntouchable()),
   EXTRAS(true, 100),
   HORNY(true, 100, new DancerPerks.Horny()),
   TARGETAUDIENCE(true, 100, new DancerPerks.TargetAudience()),
   NIGHTSHIFT(true, 100, new BartenderPerks.NightShift()),
   CLASSY(true, 200, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.CHARISMA, 0.3F)),
   LIQUORMASTER(true, 100, new BartenderPerks.LiquorMaster()),
   MULTITASKING(true, 100, new BartenderPerks.Multitasking()),
   THECONFIDENT(true, 100, new BartenderPerks.TheConfident()),
   FLIRTY(true, 100),
   DATASS(true, 100),
   CATMAID(true, 100, new BartenderPerks.CatMaid()),
   UNDERTHETABLE(true, 100),
   OUTGOING(true, 100, new BartenderPerks.Outgoing()),
   SHARPSENSES(true, 100, new Perks.SimpleTraitEffect(5.0, null, CalculatedAttribute.HIT, CalculatedAttribute.DODGE)),
   WEAPONMASTERY(true, 100, new Perks.SimpleTraitEffect(null, 1.0, CalculatedAttribute.DAMAGE)),
   MINDOFTHEFIGHTER(true, 100, new FighterPerks.MindOfTheFighter()),
   ELEMENTALSTUDY(true, 100, new FighterPerks.ElementalStudy()),
   TOUGH(true, new TraitEffect.AttributeMaxModifier(EssentialAttributes.HEALTH, 15)),
   DISTRACTION(true, 100, new FighterPerks.Distraction()),
   PLUNDERER(true, 100, new FighterPerks.Plunderer()),
   LOSTARTS(
      true,
      100,
      new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.HIT, CalculatedAttribute.DAMAGE, CalculatedAttribute.SPEED, CalculatedAttribute.CRITCHANCE)
   ),
   VITALPOINTSPIERCING(true, 100, new Perks.SimpleTraitEffect(15.0, null, CalculatedAttribute.CRITDAMAGEAMOUNT)),
   CASTTIME(true, 100, new FighterPerks.CastTime()),
   IRONBODY(true, 100, new FighterPerks.IronBody()),
   ETHERSHIELD(true, 100, new FighterPerks.EtherShield()),
   MKIIWALKER(true, 100, new Perks.SimpleTraitEffect(40.0, null, CalculatedAttribute.BLOCKAMOUNT)),
   SHOWTIME(true, 1000),
   GREEDY(true, 100, new Perks.SimpleTraitEffect(25.0, null, CalculatedAttribute.STEALAMOUNTMODIFIER)),
   NIMBLEHANDS(true, 100, new Perks.SimpleTraitEffect(20.0, null, CalculatedAttribute.STEALCHANCE)),
   PICKPOCKET(true, 100, new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.STEALITEMCHANCE)),
   ROGUE(true, 100, new Perks.SimpleTraitEffect(4.0, null, CalculatedAttribute.CRITCHANCE, CalculatedAttribute.DAMAGE, CalculatedAttribute.DODGE)),
   LUPIN(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, BaseAttributeTypes.INTELLIGENCE, 0.15F)),
   BANDIT(true, 400, new TraitEffect.MultipleAttributeChangeInfluence(0.3F, BaseAttributeTypes.STAMINA, BaseAttributeTypes.STRENGTH)),
   CONARTIST(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, BaseAttributeTypes.CHARISMA, 0.2F)),
   RESELLER(true, 1000),
   LIAISONSDANGEREUSES(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, SpecializationAttribute.SEDUCTION, 0.3F)),
   NIGHTSHADE(true, 100, new ThiefPerks.NightShade()),
   SHADOWBODY(true, 100, new ThiefPerks.ShadowBody()),
   DOUBLEVIE(true, 100, new ThiefPerks.DoubleVie()),
   ASSASSIN(true, 100, new Perks.SimpleTraitEffect(30.0, null, CalculatedAttribute.CRITDAMAGEAMOUNT)),
   PHANTOMTHIEF(true, 100),
   GROOMING(true, new Perks.AttributeInfluence(BaseAttributeTypes.CHARISMA, SpecializationAttribute.CATGIRL, 0.1F)),
   STRAYCAT(true, new Perks.AttributeInfluence(BaseAttributeTypes.OBEDIENCE, SpecializationAttribute.CATGIRL, -0.1F)),
   DOMESTICATED(true, new Perks.AttributeInfluence(BaseAttributeTypes.OBEDIENCE, SpecializationAttribute.CATGIRL, 0.1F)),
   CATSANDRATS(true, new Perks.AttributeInfluence(BaseAttributeTypes.STAMINA, SpecializationAttribute.CATGIRL, 0.1F)),
   CUNNING(true, new Perks.AttributeInfluence(BaseAttributeTypes.INTELLIGENCE, SpecializationAttribute.CATGIRL, 0.1F)),
   PAWERFUL(true, new Perks.AttributeInfluence(BaseAttributeTypes.STRENGTH, SpecializationAttribute.CATGIRL, 0.1F)),
   ALLPURRPOSE(true, new Perks.AttributeInfluence(SpecializationAttribute.CLEANING, SpecializationAttribute.CATGIRL, 0.1F)),
   CATBURGLAR(true, new Perks.AttributeInfluence(SpecializationAttribute.PICKPOCKETING, SpecializationAttribute.CATGIRL, 0.1F)),
   WETPUSSY(true, new Perks.AttributeInfluence(SpecializationAttribute.SEDUCTION, SpecializationAttribute.CATGIRL, 0.1F)),
   CATTRACTIVE(true, new Perks.AttributeInfluence(SpecializationAttribute.ADVERTISING, SpecializationAttribute.CATGIRL, 0.1F)),
   CATWALK(true, new Perks.AttributeInfluence(SpecializationAttribute.STRIP, SpecializationAttribute.CATGIRL, 0.1F)),
   HIGHCATNESSRATING(true, 400, new TraitEffect.AttributeMaxModifier(SpecializationAttribute.CATGIRL, 96)),
   CATSAREASSHOLES(true, 100),
   CERTIFIEDTRAINER(true, 500, new TraitEffect.BaseAttributeChangeInfluence(0.1F)),
   GOODSLAVE(true, 50, new SlavePerks.GoodSlave()),
   BASICTRAINING1(true, 100, new TraitEffect.BaseAttributeChangeInfluence(0.1F)),
   BASICTRAINING2(true, 100, new TraitEffect.BaseAttributeChangeInfluence(0.1F)),
   BASICTRAINING3(true, 100, new TraitEffect.BaseAttributeChangeInfluence(0.1F)),
   SEXTRAINING1(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1F)),
   SEXTRAINING2(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1F)),
   SEXTRAINING3(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1F)),
   SPECIALIZATIONTRAINING1(true, 100, new TraitEffect.SpecializationAttributeChangeInfluence(0.1F)),
   SPECIALIZATIONTRAINING2(true, 100, new TraitEffect.SpecializationAttributeChangeInfluence(0.1F)),
   SPECIALIZATIONTRAINING3(true, 100, new TraitEffect.SpecializationAttributeChangeInfluence(0.1F)),
   PERFECTTRAINER(true, 500, new TraitEffect.AllAttributeChangeInfluence(0.3F)),
   PERFECTSLAVE(true, 500, new TraitEffect.AllAttributeChangeInfluence(0.3F)),
   TRAINING(true, 100, new TraitEffect.AllAttributeChangeInfluence(0.05F)),
   EFFECTIVETRAINER(true, 100, new TrainerPerks.EffectiveTrainer()),
   MOTIVATOR(true, 100, new TrainerPerks.Motivator()),
   RESPECTED(true, 100, new TraitEffect.InfluenceAttributeLoss(BaseAttributeTypes.COMMAND, -0.35F)),
   ONEOFUS(true, 100, new TraitEffect.InfluenceAttributeLoss(BaseAttributeTypes.COMMAND, -0.75F)),
   STRONGPRESENCE(true, 100, new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.CONTROL)),
   EYESEVERYWHERE(true, 100, new Perks.SimpleTraitEffect(20.0, null, CalculatedAttribute.CONTROL)),
   SPYNETWORK(true, 100, new Perks.SimpleTraitEffect(50.0, null, CalculatedAttribute.CONTROL)),
   CONTENTPERK(true, 100, new Perks.SimpleTraitEffect(3.0, null, CalculatedAttribute.CONTROL)),
   TRUSTEDSLAVE(true, 100, new Perks.SimpleTraitEffect(10.0, null, CalculatedAttribute.CONTROL)),
   GENIUS(true, 500, new TraitEffect.BaseAttributeChangeInfluence(0.3F)),
   LEGACYWHORE(true, 100),
   LEGACYSTRIPPER(true, 100),
   LEGACYMAID(true, 100),
   LEGACYBARTENDER(true, 100),
   LEGACYMASSEUR(true, 100),
   LEGACYADVENTURER(true, 100),
   LEGACYNONE(true, 100, new LegacyPerks.LegacyNone()),
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
   BENEFACTORSTREETS(true, 0, new LegacyPerks.BenefactorStreets()),
   BENEFACTORSHOPS(true, 0, new LegacyPerks.BenefactorShops()),
   BENEFACTORSLAVEMARKET(true, 0, new LegacyPerks.BenefactorSlavemarket()),
   BENEFACTORCARPENTERS(true, 0, new LegacyPerks.BenefactorCarpenters()),
   BENEFACTORKINGDOM(true, 0, new LegacyPerks.BenefactorKingdom()),
   DEBUTANTE(true, 100, new TraitEffect.SexAttributeChangeInfluence(0.1F)),
   SENSITIVECLIT(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.VAGINAL, 0.2F)),
   DEEPLOVE(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.ANAL, 0.2F)),
   LOVETHETASTE(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.ORAL, 0.2F)),
   AFLEURDEPEAU(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.FOREPLAY, 0.2F)),
   MEATBUNS(true, 100, new TraitEffect.AttributeChangeInfluence(Sextype.TITFUCK, 0.2F)),
   COZYCUNT(true, 100, new SexPerks.CozyCunt()),
   ROWDYRUMP(true, 100, new SexPerks.RowdyRump()),
   SLURPYSLURP(true, 100, new SexPerks.SlurpySlurp()),
   TOUCHYFEELY(true, 100, new SexPerks.TouchyFeely()),
   PUFFPUFF(true, 100, new SexPerks.PuffPuff()),
   BEDROOMPRINCESS(true, 100, new SexPerks.BedroomPrincess()),
   WETFORYOU(true, 100, new SexPerks.WetForYou()),
   BACKDOOROPEN(true, 100, new SexPerks.BackdoorOpen()),
   THIRSTY(true, 100, new SexPerks.Thirsty()),
   FEELMEUP(true, 100, new SexPerks.FeelMeUp()),
   COMETOMOMMY(true, 100, new SexPerks.ComeToMommy()),
   STEAMY(true, 100, new SexPerks.Steamy()),
   BEDGODDESS(true, 100, new SexPerks.BedGoddess()),
   INITIATE(true, 100, new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.ADVERTISING, 0.2F)),
   SAMPLINGTHEGOODS(true, 1000),
   SHOWINGTHEGOODS(true, 1000),
   SHOWOFF(true, 1000),
   CATCHY(true, 1000),
   HEYGUYSBOOSE(true, 1000),
   SPIRITED(true, 100, new MarketingPerks.Spirited()),
   TARGETBUM(true, 100, new MarketingPerks.TargetBum()),
   TARGETPEASANT(true, 100, new MarketingPerks.TargetPeasants()),
   TARGETSOLDIER(true, 100, new MarketingPerks.TargetSoldiers()),
   TARGETBUSINESSMEN(true, 100, new MarketingPerks.TargetBusinessmen()),
   CONFIRMEDSALESPERSON(true, 100, new MarketingPerks.Salesperson()),
   SALESPROMOTION(true, new Perks.AttributeInfluence(SpecializationAttribute.ADVERTISING, BaseAttributeTypes.INTELLIGENCE, 0.1F)),
   NICHEMARKETINGNOBLE(true, 100, new MarketingPerks.TargetNobles()),
   NICHEMARKETINGLORD(true, 100, new MarketingPerks.TargetLords()),
   NICHEMARKETINGCELEBRITY(true, 100, new MarketingPerks.TargetCelebrities()),
   NICHEMARKETINGGROUPS(true, 100, new MarketingPerks.TargetGroups()),
   RECOGNIZED(true, 100, new MarketingPerks.Recognized()),
   BUSINESSRELATIONS(true, 100),
   ERUDITE(true, 100, new TraitEffect.AttributeChangeInfluence(BaseAttributeTypes.INTELLIGENCE, 0.3F)),
   SELFTAUGHT(true, 100, new AlchemistPerks.SelfTaught()),
   GATEOFTRUTH(true, 400, new TraitEffect.AttributeChangeInfluence(SpecializationAttribute.MAGIC, 1.0F)),
   GREENTHUMB(true, 100, new AlchemistPerks.GreenThumb()),
   FLOWERARRANGEMENT(true, 100),
   APHRODISIACS(true, 100, new AlchemistPerks.Aphrodisiacs()),
   CALMINGINCENCES(true, 100, new AlchemistPerks.RelaxingIncences()),
   DARKRITUAL(true, 100),
   PERSONNALOFFERING(true, 100),
   MANAFLOW(true, new Perks.AttributeInfluence(BaseAttributeTypes.STAMINA, SpecializationAttribute.MAGIC, 0.15F)),
   ETHERWALKER(true, new Perks.AttributeInfluence(SpecializationAttribute.AGILITY, SpecializationAttribute.MAGIC, 0.15F)),
   BENEVOLENT(true, 100),
   BREWER(true, 100, new NursePerks.Brewer()),
   TANTRIC(true, 100),
   MAGICALHEALING(true, 100),
   LOVEANDCARE(true, 100, new NursePerks.LoveAndCare()),
   ONSENPRINCESS(true, 100),
   OILY(true, 100),
   CASTCURE(true, 100),
   FIRSTAID(true, 100),
   COSMETICS(true, 100),
   SEXPERT(true, 100, new NursePerks.SeXpert()),
   BLESSEDAURA(true, 100, new NursePerks.BlessedAura()),
   GIVEANDTAKE(true, 100, new DominatrixPerks.GiveAndTake()),
   CRUELMASTER(true, 100, new DominatrixPerks.CruelMaster()),
   HITMEHARDER(true, 100, new DominatrixPerks.HitMeHarder()),
   PLEASURETHROUGHPAIN(true, 100, new DominatrixPerks.PleasureThroughPain()),
   PLEASUREANDPAIN(true, 100, new DominatrixPerks.PleasureAndPain()),
   PLEASUREINPAIN(true, 100, new DominatrixPerks.PleasureInPain()),
   MYWHIPISALLINEED(true, 100, new DominatrixPerks.MyWhipIsAllINeed()),
   SADIST(true, 100, new DominatrixPerks.Sadist()),
   GOODMASTERSARETHEBESTSUBS(true, 200, new DominatrixPerks.GoodMastersAreTheBestSubs()),
   MASOCHIST(true, 300, new DominatrixPerks.Masochist()),
   LEATHERQUEEN(true, 300, new DominatrixPerks.LeatherQueen()),
   EVERYONEBEHAVE(true, 100, new DominatrixPerks.EveryoneBehave()),
   AGGRESSIVEADVERTISEMENT(true, 100, new DominatrixPerks.AggressiveAdvertisement()),
   SQUEALANDHEAL(true, 100, new DominatrixPerks.SquealAndHeal()),
   LICKITUP(true, 200, new DominatrixPerks.LickItUp()),
   LEATHERMISTRESS(true, 300, new DominatrixPerks.LeatherMistress()),
   WHATDOESNTKILLYOU(true, 300, new DominatrixPerks.KillYou()),
   MASTERANDSLAVE(true, 300, new DominatrixPerks.MasterAndSlave());

   private boolean perk = false;
   private int valueModifier = 0;
   private Trait opposingTrait;
   private TraitEffect traitEffect;
   private String text = this.toString();

   Trait() {
      this(false, 0, null, null);
   }

   Trait(int valueModifier) {
      this(false, valueModifier);
   }

   Trait(Trait opposingTrait) {
      this(false, 0, opposingTrait, null);
   }

   Trait(boolean perk, int valueModifier) {
      this(perk, valueModifier, null);
   }

   Trait(boolean perk, TraitEffect traitEffect) {
      this(perk, 0, traitEffect);
   }

   Trait(int valueModifier, TraitEffect traitEffect) {
      this(false, valueModifier, traitEffect);
   }

   Trait(boolean perk, int valueModifier, TraitEffect traitEffect) {
      this(perk, valueModifier, null, traitEffect);
   }

   Trait(int valueModifier, Trait opposingTrait, TraitEffect traitEffect) {
      this(false, valueModifier, opposingTrait, traitEffect);
   }

   Trait(boolean perk, int valueModifier, Trait opposingTrait, TraitEffect traitEffect) {
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
      return this.traitEffect != null ? this.traitEffect.getMinObedienceModified(curMinObedience, character, activity) : curMinObedience;
   }

   public boolean addTrait(Charakter character, List<Trait> traits) {
      Trait opposedTrait = null;

      for (Trait curTrait : traits) {
         if (this.isOpposed(curTrait)) {
            opposedTrait = curTrait;
            break;
         }
      }

      if (opposedTrait == null) {
         return this.traitEffect != null ? this.traitEffect.addTrait(character) : true;
      }

      character.removeTrait(opposedTrait);
      return false;
   }

   public boolean removeTrait(Charakter character) {
      return this.traitEffect != null ? this.traitEffect.removeTrait(character) : true;
   }

   public double getAttributeModified(CalculatedAttribute calculatedAttribute, double currentValue, Charakter character) {
      return this.traitEffect != null ? this.traitEffect.getAttributeModified(calculatedAttribute, currentValue, character) : currentValue;
   }

   public boolean isOpposed(Trait trait) {
      if (this != BIGBOOBS || trait != LOLI && trait != SMALLBOOBS) {
         return (this == LOLI || this == SMALLBOOBS) && trait == BIGBOOBS ? true : this.opposingTrait != null && trait == this.opposingTrait;
      } else {
         return true;
      }
   }

   public static List<Trait> getBasicTraits() {
      List<Trait> traits = new ArrayList<>();

      for (Trait trait : values()) {
         if (!trait.isPerk()) {
            traits.add(trait);
         }
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
      return this.traitEffect != null ? this.traitEffect.getSkillTree() : null;
   }

   public void modifyPossibleAttacks(List<Attack> attacks, Charakter character) {
      if (this.traitEffect != null) {
         this.traitEffect.modifyPossibleAttacks(attacks, character);
      }
   }

   public float getAttributeModifier(Attribute attribute) {
      return this.traitEffect != null ? this.traitEffect.getAttributeModifier(attribute) : 0.0F;
   }

   public int modifyCustomerRating(int initialRating, Customer customer, BusinessMainActivity businessMainActivity) {
      return this.traitEffect != null ? this.traitEffect.modifyCustomerRating(initialRating, customer, businessMainActivity) : initialRating;
   }
}
