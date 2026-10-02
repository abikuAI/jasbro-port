/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.PerkHandler;
import jasbro.game.character.traits.SkillTree;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.AttributeType;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SkillTreeItem {
    private Trait perk;
    private List<SkillTreeItem> nextItems = new ArrayList<SkillTreeItem>();
    private List<PerkRequirement> perkRequirements = new ArrayList<PerkRequirement>();
    private List<PerkRequirement> perkUnRequirements = new ArrayList<PerkRequirement>();
    private ImageData icon;
    private List<SkillTreeItem> parentItems = new ArrayList<SkillTreeItem>();

    public SkillTreeItem(ImageData icon) {
        this.icon = icon;
    }

    public SkillTreeItem(Trait perk, ImageData icon, SkillTreeItem ... nextItems) {
        this(icon);
        this.perk = perk;
        for (SkillTreeItem skillTreeItem : nextItems) {
            this.nextItems.add(skillTreeItem);
        }
    }

    public boolean add(Charakter character) {
        if (this.checkRequirementsMet(character)) {
            character.addTrait(this.perk);
            return true;
        }
        return false;
    }

    public void remove(Charakter character) {
        character.removeTrait(this.perk);
    }

    public List<PerkRequirement> getPerkRequirements() {
        return this.perkRequirements;
    }

    public List<PerkRequirement> getPerkUnRequirements() {
        return this.perkUnRequirements;
    }

    public void setPerkRequirements(List<PerkRequirement> perkRequirements) {
        this.perkRequirements = perkRequirements;
    }

    public void setPerkUnRequirements(List<PerkRequirement> perkRequirements) {
        this.perkRequirements = perkRequirements;
    }

    public Trait getPerk() {
        return this.perk;
    }

    public List<SkillTreeItem> getNextItems() {
        return this.nextItems;
    }

    public boolean isParentItem(SkillTreeItem skillTreeItem) {
        return this.parentItems.contains(skillTreeItem);
    }

    public void addParentItem(SkillTreeItem parentItem) {
        this.parentItems.add(parentItem);
    }

    public boolean isParentLearned(Charakter character) {
        if (this.getParentItems().size() == 0) {
            return true;
        }
        List<Trait> characterTraits = character.getTraits();
        for (SkillTreeItem parentItem : this.parentItems) {
            if (!characterTraits.contains(parentItem.getPerk())) continue;
            return true;
        }
        return false;
    }

    public List<SkillTreeItem> getParentItems() {
        return this.parentItems;
    }

    public static void link(Trait perk1, Trait perk2, Map<Trait, SkillTreeItem> map) {
        map.get(perk1).getNextItems().add(map.get(perk2));
        map.get(perk2).addParentItem(map.get(perk1));
    }

    public static void create(Trait perk, Map<Trait, SkillTreeItem> map, ImageData icon) {
        map.put(perk, new SkillTreeItem(perk, icon, new SkillTreeItem[0]));
    }

    public ImageData getIcon() {
        return this.icon;
    }

    public boolean canLearn(SkillTree skillTree, Charakter character) {
        int baseRequirement;
        if (character.getUnspentPerkPoints() < 1) {
            return false;
        }
        if (!this.isParentLearned(character)) {
            return false;
        }
        SpecializationType specializationType = PerkHandler.getConnectedSpecializationType(skillTree);
        if (specializationType != null && (baseRequirement = PerkHandler.getBaseRequirement(this, skillTree, character)) > 0 && Util.getAverage(specializationType, character) < (float)baseRequirement) {
            return false;
        }
        return this.checkRequirementsMet(character);
    }

    public boolean checkRequirementsMet(Charakter character) {
        for (PerkRequirement perkRequirement : this.getPerkRequirements()) {
            if (perkRequirement.isRequirementMet(character)) continue;
            return false;
        }
        for (PerkRequirement perkRequirement : this.getPerkUnRequirements()) {
            if (!perkRequirement.isRequirementMet(character)) continue;
            return false;
        }
        return true;
    }

    public String getDescription(SkillTree skillTree, Charakter character) {
        String text = this.perk.getDescription(character) + "\n";
        if (!character.getTraits().contains(this.perk)) {
            int baseRequirement;
            SpecializationType specializationType = PerkHandler.getConnectedSpecializationType(skillTree);
            if (specializationType != null && (baseRequirement = PerkHandler.getBaseRequirement(this, skillTree, character)) > 0) {
                Object[] arguments = new Object[]{baseRequirement, skillTree.getText(), (int)Util.getAverage(specializationType, character)};
                text = text + TextUtil.t("perks.baseRequirement", arguments) + "\n";
            }
            for (PerkRequirement perkRequirement : this.getPerkRequirements()) {
                if (perkRequirement.isRequirementMet(character)) continue;
                text = text + perkRequirement.getRequirementDescription();
            }
        }
        return text;
    }

    public static class DominatrixTreeStart
    extends SkillTreeItem {
        public DominatrixTreeStart() {
            super(Trait.GIVEANDTAKE, new ImageData("images/icons/perks/recycle.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.GIVEANDTAKE, this);
            DominatrixTreeStart.create(Trait.CRUELMASTER, map, new ImageData("images/icons/perks/crowned-heart.png"));
            DominatrixTreeStart.create(Trait.HITMEHARDER, map, new ImageData("images/icons/perks/internal-injury.png"));
            DominatrixTreeStart.create(Trait.PLEASURETHROUGHPAIN, map, new ImageData("images/icons/perks/ifrit.png"));
            DominatrixTreeStart.create(Trait.PLEASUREANDPAIN, map, new ImageData("images/icons/perks/beastblood.png"));
            DominatrixTreeStart.create(Trait.PLEASUREINPAIN, map, new ImageData("images/icons/perks/bleeding-heart.png"));
            DominatrixTreeStart.create(Trait.MYWHIPISALLINEED, map, new ImageData("images/icons/perks/whip.png"));
            DominatrixTreeStart.create(Trait.SADIST, map, new ImageData("images/icons/perks/what_doesnt_kill_you.png"));
            DominatrixTreeStart.create(Trait.GOODMASTERSARETHEBESTSUBS, map, new ImageData("images/icons/perks/swap 2.png"));
            DominatrixTreeStart.create(Trait.MASOCHIST, map, new ImageData("images/icons/perks/manacles.png"));
            DominatrixTreeStart.create(Trait.LEATHERQUEEN, map, new ImageData("images/icons/perks/leather_queen.png"));
            DominatrixTreeStart.create(Trait.EVERYONEBEHAVE, map, new ImageData("images/icons/perks/shouting.png"));
            DominatrixTreeStart.create(Trait.AGGRESSIVEADVERTISEMENT, map, new ImageData("images/icons/perks/paranoia.png"));
            DominatrixTreeStart.create(Trait.SQUEALANDHEAL, map, new ImageData("images/icons/perks/level-four-advanced.png"));
            DominatrixTreeStart.create(Trait.LICKITUP, map, new ImageData("images/icons/perks/slurpyslurp.png"));
            DominatrixTreeStart.create(Trait.LEATHERMISTRESS, map, new ImageData("images/icons/perks/slavery-whip.png"));
            DominatrixTreeStart.create(Trait.WHATDOESNTKILLYOU, map, new ImageData("images/icons/perks/screaming.png"));
            DominatrixTreeStart.create(Trait.MASTERANDSLAVE, map, new ImageData("images/icons/perks/chained-heart.png"));
            DominatrixTreeStart.link(Trait.GIVEANDTAKE, Trait.CRUELMASTER, map);
            DominatrixTreeStart.link(Trait.GIVEANDTAKE, Trait.HITMEHARDER, map);
            DominatrixTreeStart.link(Trait.CRUELMASTER, Trait.PLEASURETHROUGHPAIN, map);
            DominatrixTreeStart.link(Trait.CRUELMASTER, Trait.PLEASUREANDPAIN, map);
            DominatrixTreeStart.link(Trait.HITMEHARDER, Trait.PLEASUREANDPAIN, map);
            DominatrixTreeStart.link(Trait.HITMEHARDER, Trait.PLEASUREINPAIN, map);
            DominatrixTreeStart.link(Trait.PLEASURETHROUGHPAIN, Trait.MYWHIPISALLINEED, map);
            DominatrixTreeStart.link(Trait.PLEASURETHROUGHPAIN, Trait.SADIST, map);
            DominatrixTreeStart.link(Trait.PLEASUREANDPAIN, Trait.SADIST, map);
            DominatrixTreeStart.link(Trait.PLEASUREANDPAIN, Trait.GOODMASTERSARETHEBESTSUBS, map);
            DominatrixTreeStart.link(Trait.PLEASUREANDPAIN, Trait.MASOCHIST, map);
            DominatrixTreeStart.link(Trait.PLEASUREINPAIN, Trait.MASOCHIST, map);
            DominatrixTreeStart.link(Trait.PLEASUREINPAIN, Trait.LEATHERQUEEN, map);
            DominatrixTreeStart.link(Trait.MYWHIPISALLINEED, Trait.EVERYONEBEHAVE, map);
            DominatrixTreeStart.link(Trait.SADIST, Trait.EVERYONEBEHAVE, map);
            DominatrixTreeStart.link(Trait.SADIST, Trait.AGGRESSIVEADVERTISEMENT, map);
            DominatrixTreeStart.link(Trait.GOODMASTERSARETHEBESTSUBS, Trait.AGGRESSIVEADVERTISEMENT, map);
            DominatrixTreeStart.link(Trait.GOODMASTERSARETHEBESTSUBS, Trait.SQUEALANDHEAL, map);
            DominatrixTreeStart.link(Trait.MASOCHIST, Trait.SQUEALANDHEAL, map);
            DominatrixTreeStart.link(Trait.MASOCHIST, Trait.LICKITUP, map);
            DominatrixTreeStart.link(Trait.LEATHERQUEEN, Trait.LICKITUP, map);
            DominatrixTreeStart.link(Trait.EVERYONEBEHAVE, Trait.LEATHERMISTRESS, map);
            DominatrixTreeStart.link(Trait.AGGRESSIVEADVERTISEMENT, Trait.LEATHERMISTRESS, map);
            DominatrixTreeStart.link(Trait.AGGRESSIVEADVERTISEMENT, Trait.MASTERANDSLAVE, map);
            DominatrixTreeStart.link(Trait.SQUEALANDHEAL, Trait.MASTERANDSLAVE, map);
            DominatrixTreeStart.link(Trait.SQUEALANDHEAL, Trait.WHATDOESNTKILLYOU, map);
            DominatrixTreeStart.link(Trait.LICKITUP, Trait.WHATDOESNTKILLYOU, map);
        }
    }

    public static class FurryTreeStart
    extends SkillTreeItem {
        public FurryTreeStart() {
            super(Trait.BESTIALFEATURES, new ImageData("images/icons/perks/bestialfeatures.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.BESTIALFEATURES, this);
            FurryTreeStart.create(Trait.FELINE, map, new ImageData("images/icons/perks/feline.png"));
            FurryTreeStart.create(Trait.LAGOMORPH, map, new ImageData("images/icons/perks/lagomorph.png"));
            ((SkillTreeItem)map.get(Trait.FELINE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LAGOMORPH));
            ((SkillTreeItem)map.get(Trait.LAGOMORPH)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.FELINE));
            FurryTreeStart.link(Trait.BESTIALFEATURES, Trait.FELINE, map);
            FurryTreeStart.link(Trait.BESTIALFEATURES, Trait.LAGOMORPH, map);
        }
    }

    public static class AlchemistTreeStart
    extends SkillTreeItem {
        public AlchemistTreeStart() {
            super(Trait.ERUDITE, new ImageData("images/icons/perks/pointy-hat.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.ERUDITE, this);
            AlchemistTreeStart.create(Trait.SELFTAUGHT, map, new ImageData("images/icons/perks/bookmark.png"));
            AlchemistTreeStart.create(Trait.GATEOFTRUTH, map, new ImageData("images/icons/perks/magic-gate.png"));
            AlchemistTreeStart.create(Trait.GREENTHUMB, map, new ImageData("images/icons/perks/pine-tree.png"));
            AlchemistTreeStart.create(Trait.APHRODISIACS, map, new ImageData("images/icons/perks/drink-me.png"));
            AlchemistTreeStart.create(Trait.FLOWERARRANGEMENT, map, new ImageData("images/icons/perks/beautician.png"));
            AlchemistTreeStart.create(Trait.CALMINGINCENCES, map, new ImageData("images/icons/perks/lotus-flower.png"));
            AlchemistTreeStart.create(Trait.DARKRITUAL, map, new ImageData("images/icons/perks/monsterpedia.png"));
            AlchemistTreeStart.create(Trait.PERSONNALOFFERING, map, new ImageData("images/icons/perks/eclipse.png"));
            AlchemistTreeStart.create(Trait.MANAFLOW, map, new ImageData("images/icons/perks/goo-explosion.png"));
            AlchemistTreeStart.create(Trait.ETHERWALKER, map, new ImageData("images/icons/perks/wave-crest.png"));
            AlchemistTreeStart.link(Trait.ERUDITE, Trait.SELFTAUGHT, map);
            AlchemistTreeStart.link(Trait.ERUDITE, Trait.GATEOFTRUTH, map);
            AlchemistTreeStart.link(Trait.SELFTAUGHT, Trait.GREENTHUMB, map);
            AlchemistTreeStart.link(Trait.SELFTAUGHT, Trait.FLOWERARRANGEMENT, map);
            AlchemistTreeStart.link(Trait.SELFTAUGHT, Trait.APHRODISIACS, map);
            AlchemistTreeStart.link(Trait.SELFTAUGHT, Trait.CALMINGINCENCES, map);
            AlchemistTreeStart.link(Trait.GATEOFTRUTH, Trait.DARKRITUAL, map);
            AlchemistTreeStart.link(Trait.GATEOFTRUTH, Trait.PERSONNALOFFERING, map);
            AlchemistTreeStart.link(Trait.GATEOFTRUTH, Trait.MANAFLOW, map);
            AlchemistTreeStart.link(Trait.GATEOFTRUTH, Trait.ETHERWALKER, map);
        }
    }

    public static class NurseTreeStart
    extends SkillTreeItem {
        public NurseTreeStart() {
            super(Trait.BENEVOLENT, new ImageData("images/icons/perks/first_aid.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.BENEVOLENT, this);
            NurseTreeStart.create(Trait.BREWER, map, new ImageData("images/icons/perks/potion-ball.png"));
            NurseTreeStart.create(Trait.TANTRIC, map, new ImageData("images/icons/perks/tantric.png"));
            NurseTreeStart.create(Trait.MAGICALHEALING, map, new ImageData("images/icons/perks/heart-bottle.png"));
            NurseTreeStart.create(Trait.LOVEANDCARE, map, new ImageData("images/icons/perks/love-song.png"));
            NurseTreeStart.create(Trait.ONSENPRINCESS, map, new ImageData("images/icons/perks/naiad.png"));
            NurseTreeStart.create(Trait.OILY, map, new ImageData("images/icons/perks/oily.png"));
            NurseTreeStart.create(Trait.CASTCURE, map, new ImageData("images/icons/perks/health-increase.png"));
            NurseTreeStart.create(Trait.FIRSTAID, map, new ImageData("images/icons/perks/first_aid.png"));
            NurseTreeStart.create(Trait.COSMETICS, map, new ImageData("images/icons/perks/cosmetics.png"));
            NurseTreeStart.create(Trait.SEXPERT, map, new ImageData("images/icons/perks/cheerful.png"));
            NurseTreeStart.create(Trait.BLESSEDAURA, map, new ImageData("images/icons/perks/beams-aura.png"));
            NurseTreeStart.link(Trait.BENEVOLENT, Trait.BREWER, map);
            NurseTreeStart.link(Trait.BREWER, Trait.TANTRIC, map);
            NurseTreeStart.link(Trait.BREWER, Trait.MAGICALHEALING, map);
            NurseTreeStart.link(Trait.BREWER, Trait.LOVEANDCARE, map);
            NurseTreeStart.link(Trait.TANTRIC, Trait.ONSENPRINCESS, map);
            NurseTreeStart.link(Trait.TANTRIC, Trait.OILY, map);
            NurseTreeStart.link(Trait.MAGICALHEALING, Trait.CASTCURE, map);
            NurseTreeStart.link(Trait.MAGICALHEALING, Trait.FIRSTAID, map);
            NurseTreeStart.link(Trait.LOVEANDCARE, Trait.COSMETICS, map);
            NurseTreeStart.link(Trait.ONSENPRINCESS, Trait.SEXPERT, map);
            NurseTreeStart.link(Trait.OILY, Trait.SEXPERT, map);
            NurseTreeStart.link(Trait.COSMETICS, Trait.SEXPERT, map);
            NurseTreeStart.link(Trait.CASTCURE, Trait.BLESSEDAURA, map);
            NurseTreeStart.link(Trait.FIRSTAID, Trait.BLESSEDAURA, map);
            NurseTreeStart.link(Trait.COSMETICS, Trait.BLESSEDAURA, map);
        }
    }

    public static class DancerTreeStart
    extends SkillTreeItem {
        public DancerTreeStart() {
            super(Trait.NICEHIPS, new ImageData("images/icons/perks/lotus.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.NICEHIPS, this);
            DancerTreeStart.create(Trait.LEWD, map, new ImageData("images/icons/perks/beams-aura.png"));
            DancerTreeStart.create(Trait.PURE, map, new ImageData("images/icons/perks/aura.png"));
            DancerTreeStart.create(Trait.TEASER, map, new ImageData("images/icons/perks/wrapped-heart.png"));
            DancerTreeStart.create(Trait.LASCIVIOUS, map, new ImageData("images/icons/perks/white-cat.png"));
            DancerTreeStart.create(Trait.REFINED, map, new ImageData("images/icons/perks/flowers.png"));
            DancerTreeStart.create(Trait.ORIENTALCHARMS, map, new ImageData("images/icons/perks/fragrance.png"));
            DancerTreeStart.create(Trait.SMELLSLIKEKITTEN, map, new ImageData("images/icons/perks/smellslikekitten.png"));
            DancerTreeStart.create(Trait.ACROBATICS, map, new ImageData("images/icons/perks/beanstalk.png"));
            DancerTreeStart.create(Trait.PERFECTCONDITION, map, new ImageData("images/icons/perks/barefoot.png"));
            DancerTreeStart.create(Trait.CROWDLOVER, map, new ImageData("images/icons/perks/dark-squad.png"));
            DancerTreeStart.create(Trait.HORNY, map, new ImageData("images/icons/perks/love-song.png"));
            DancerTreeStart.create(Trait.TRENDY, map, new ImageData("images/icons/perks/scale-mail.png"));
            DancerTreeStart.create(Trait.SKINCARE, map, new ImageData("images/icons/perks/pollen-dust.png"));
            DancerTreeStart.create(Trait.TARGETAUDIENCE, map, new ImageData("images/icons/perks/reticule.png"));
            DancerTreeStart.create(Trait.TANLINES, map, new ImageData("images/icons/perks/burn.png"));
            DancerTreeStart.create(Trait.THEUNTOUCHABLE, map, new ImageData("images/icons/perks/heart-tower.png"));
            DancerTreeStart.create(Trait.EXTRAS, map, new ImageData("images/icons/perks/rose.png"));
            DancerTreeStart.link(Trait.NICEHIPS, Trait.PURE, map);
            DancerTreeStart.link(Trait.NICEHIPS, Trait.LEWD, map);
            DancerTreeStart.link(Trait.PURE, Trait.TEASER, map);
            DancerTreeStart.link(Trait.PURE, Trait.LASCIVIOUS, map);
            DancerTreeStart.link(Trait.PURE, Trait.REFINED, map);
            DancerTreeStart.link(Trait.PURE, Trait.ORIENTALCHARMS, map);
            DancerTreeStart.link(Trait.ORIENTALCHARMS, Trait.SMELLSLIKEKITTEN, map);
            DancerTreeStart.link(Trait.ACROBATICS, Trait.SMELLSLIKEKITTEN, map);
            DancerTreeStart.link(Trait.SMELLSLIKEKITTEN, Trait.EXTRAS, map);
            DancerTreeStart.link(Trait.SMELLSLIKEKITTEN, Trait.THEUNTOUCHABLE, map);
            DancerTreeStart.link(Trait.LEWD, Trait.ACROBATICS, map);
            DancerTreeStart.link(Trait.LEWD, Trait.PERFECTCONDITION, map);
            DancerTreeStart.link(Trait.LEWD, Trait.CROWDLOVER, map);
            DancerTreeStart.link(Trait.LEWD, Trait.HORNY, map);
            DancerTreeStart.link(Trait.TEASER, Trait.TRENDY, map);
            DancerTreeStart.link(Trait.LASCIVIOUS, Trait.TRENDY, map);
            DancerTreeStart.link(Trait.REFINED, Trait.SKINCARE, map);
            DancerTreeStart.link(Trait.ORIENTALCHARMS, Trait.SKINCARE, map);
            DancerTreeStart.link(Trait.ACROBATICS, Trait.TARGETAUDIENCE, map);
            DancerTreeStart.link(Trait.PERFECTCONDITION, Trait.TARGETAUDIENCE, map);
            DancerTreeStart.link(Trait.CROWDLOVER, Trait.TANLINES, map);
            DancerTreeStart.link(Trait.HORNY, Trait.TANLINES, map);
            DancerTreeStart.link(Trait.TANLINES, Trait.EXTRAS, map);
            DancerTreeStart.link(Trait.TRENDY, Trait.EXTRAS, map);
            DancerTreeStart.link(Trait.TARGETAUDIENCE, Trait.THEUNTOUCHABLE, map);
            DancerTreeStart.link(Trait.SKINCARE, Trait.THEUNTOUCHABLE, map);
        }
    }

    public static class SlaveSkillTreeStart
    extends SkillTreeItem {
        public SlaveSkillTreeStart() {
            super(Trait.GOODSLAVE, new ImageData("images/icons/perks/spiked-collar.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.GOODSLAVE, this);
            SlaveSkillTreeStart.create(Trait.PERFECTSLAVE, map, new ImageData("images/icons/perks/spiked-collar.png"));
            SlaveSkillTreeStart.create(Trait.TRAINING, map, new ImageData("images/icons/perks/open-book.png"));
            SlaveSkillTreeStart.create(Trait.BASICTRAINING1, map, new ImageData("images/icons/perks/atomic-slashes.png"));
            SlaveSkillTreeStart.create(Trait.BASICTRAINING2, map, new ImageData("images/icons/perks/atomic-slashes.png"));
            SlaveSkillTreeStart.create(Trait.BASICTRAINING3, map, new ImageData("images/icons/perks/atomic-slashes.png"));
            SlaveSkillTreeStart.create(Trait.SEXTRAINING1, map, new ImageData("images/icons/perks/flame.png"));
            SlaveSkillTreeStart.create(Trait.SEXTRAINING2, map, new ImageData("images/icons/perks/flame.png"));
            SlaveSkillTreeStart.create(Trait.SEXTRAINING3, map, new ImageData("images/icons/perks/flame.png"));
            SlaveSkillTreeStart.create(Trait.SPECIALIZATIONTRAINING1, map, new ImageData("images/icons/perks/spectacles.png"));
            SlaveSkillTreeStart.create(Trait.SPECIALIZATIONTRAINING2, map, new ImageData("images/icons/perks/spectacles.png"));
            SlaveSkillTreeStart.create(Trait.SPECIALIZATIONTRAINING3, map, new ImageData("images/icons/perks/spectacles.png"));
            SlaveSkillTreeStart.create(Trait.CONTENTPERK, map, new ImageData("images/icons/perks/sleepy.png"));
            SlaveSkillTreeStart.create(Trait.TRUSTEDSLAVE, map, new ImageData("images/icons/perks/sleepy.png"));
            SlaveSkillTreeStart.link(Trait.GOODSLAVE, Trait.SEXTRAINING1, map);
            SlaveSkillTreeStart.link(Trait.GOODSLAVE, Trait.BASICTRAINING1, map);
            SlaveSkillTreeStart.link(Trait.GOODSLAVE, Trait.SPECIALIZATIONTRAINING1, map);
            SlaveSkillTreeStart.link(Trait.SEXTRAINING1, Trait.SEXTRAINING2, map);
            SlaveSkillTreeStart.link(Trait.SEXTRAINING1, Trait.BASICTRAINING2, map);
            SlaveSkillTreeStart.link(Trait.BASICTRAINING1, Trait.BASICTRAINING2, map);
            SlaveSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING1, Trait.BASICTRAINING2, map);
            SlaveSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING1, Trait.SPECIALIZATIONTRAINING2, map);
            SlaveSkillTreeStart.link(Trait.SEXTRAINING2, Trait.TRAINING, map);
            SlaveSkillTreeStart.link(Trait.BASICTRAINING2, Trait.TRAINING, map);
            SlaveSkillTreeStart.link(Trait.BASICTRAINING2, Trait.CONTENTPERK, map);
            SlaveSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING2, Trait.CONTENTPERK, map);
            SlaveSkillTreeStart.link(Trait.TRAINING, Trait.SEXTRAINING3, map);
            SlaveSkillTreeStart.link(Trait.TRAINING, Trait.BASICTRAINING3, map);
            SlaveSkillTreeStart.link(Trait.CONTENTPERK, Trait.BASICTRAINING3, map);
            SlaveSkillTreeStart.link(Trait.CONTENTPERK, Trait.SPECIALIZATIONTRAINING3, map);
            SlaveSkillTreeStart.link(Trait.SEXTRAINING3, Trait.PERFECTSLAVE, map);
            SlaveSkillTreeStart.link(Trait.BASICTRAINING3, Trait.PERFECTSLAVE, map);
            SlaveSkillTreeStart.link(Trait.BASICTRAINING3, Trait.TRUSTEDSLAVE, map);
            SlaveSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING3, Trait.TRUSTEDSLAVE, map);
        }
    }

    public static class BartenderTreeStart
    extends SkillTreeItem {
        public BartenderTreeStart() {
            super(Trait.CLASSY, new ImageData("images/icons/perks/classy.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.CLASSY, this);
            BartenderTreeStart.create(Trait.NIGHTSHIFT, map, new ImageData("images/icons/perks/night-sky.png"));
            BartenderTreeStart.create(Trait.MULTITASKING, map, new ImageData("images/icons/perks/multitasking.png"));
            BartenderTreeStart.create(Trait.OUTGOING, map, new ImageData("images/icons/perks/outgoing.png"));
            BartenderTreeStart.create(Trait.THECONFIDENT, map, new ImageData("images/icons/perks/theconfident.png"));
            BartenderTreeStart.create(Trait.CATMAID, map, new ImageData("images/icons/perks/cat_maid.png"));
            BartenderTreeStart.create(Trait.DATASS, map, new ImageData("images/icons/perks/dat_ass.png"));
            BartenderTreeStart.create(Trait.FLIRTY, map, new ImageData("images/icons/perks/flirty.png"));
            BartenderTreeStart.create(Trait.LIQUORMASTER, map, new ImageData("images/icons/perks/liquor_master.png"));
            BartenderTreeStart.create(Trait.UNDERTHETABLE, map, new ImageData("images/icons/perks/under_the_table.png"));
            BartenderTreeStart.link(Trait.CLASSY, Trait.NIGHTSHIFT, map);
            BartenderTreeStart.link(Trait.NIGHTSHIFT, Trait.OUTGOING, map);
            BartenderTreeStart.link(Trait.NIGHTSHIFT, Trait.MULTITASKING, map);
            BartenderTreeStart.link(Trait.OUTGOING, Trait.DATASS, map);
            BartenderTreeStart.link(Trait.OUTGOING, Trait.FLIRTY, map);
            BartenderTreeStart.link(Trait.MULTITASKING, Trait.THECONFIDENT, map);
            BartenderTreeStart.link(Trait.MULTITASKING, Trait.CATMAID, map);
            BartenderTreeStart.link(Trait.FLIRTY, Trait.UNDERTHETABLE, map);
            BartenderTreeStart.link(Trait.DATASS, Trait.UNDERTHETABLE, map);
            BartenderTreeStart.link(Trait.CATMAID, Trait.LIQUORMASTER, map);
            BartenderTreeStart.link(Trait.THECONFIDENT, Trait.LIQUORMASTER, map);
        }
    }

    public static class LegacySkillTreeStart
    extends SkillTreeItem {
        public LegacySkillTreeStart() {
            super(Trait.GENIUS, new ImageData("images/icons/perks/brain.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.GENIUS, this);
            LegacySkillTreeStart.create(Trait.LEGACYWHORE, map, new ImageData("images/icons/perks/charm.png"));
            LegacySkillTreeStart.create(Trait.LEGACYSTRIPPER, map, new ImageData("images/icons/perks/spirited.png"));
            LegacySkillTreeStart.create(Trait.LEGACYMASSEUR, map, new ImageData("images/icons/perks/sleight_of_hands.png"));
            LegacySkillTreeStart.create(Trait.LEGACYNONE, map, new ImageData("images/icons/perks/stone-throne.png"));
            LegacySkillTreeStart.create(Trait.LEGACYADVENTURER, map, new ImageData("images/icons/perks/lost_arts.png"));
            LegacySkillTreeStart.create(Trait.LEGACYBARTENDER, map, new ImageData("images/icons/perks/liquor_master.png"));
            LegacySkillTreeStart.create(Trait.LEGACYMAID, map, new ImageData("images/icons/perks/salt-shaker.png"));
            LegacySkillTreeStart.create(Trait.LEGACYWHORE2, map, new ImageData("images/icons/perks/pyromaniac.png"));
            LegacySkillTreeStart.create(Trait.LEGACYSTRIPPER2, map, new ImageData("images/icons/perks/sing.png"));
            LegacySkillTreeStart.create(Trait.LEGACYMASSEUR2, map, new ImageData("images/icons/perks/open-book.png"));
            LegacySkillTreeStart.create(Trait.LEGACYNONE2, map, new ImageData("images/icons/perks/stone-throne.png"));
            LegacySkillTreeStart.create(Trait.LEGACYADVENTURER2, map, new ImageData("images/icons/perks/stone-throne.png"));
            LegacySkillTreeStart.create(Trait.LEGACYBARTENDER2, map, new ImageData("images/icons/perks/stone-throne.png"));
            LegacySkillTreeStart.create(Trait.LEGACYMAID2, map, new ImageData("images/icons/perks/stone-throne.png"));
            LegacySkillTreeStart.create(Trait.HIDDENLIBRARY, map, new ImageData("images/icons/perks/book-aura.png"));
            LegacySkillTreeStart.create(Trait.TOUGHERMISSIONS1, map, new ImageData("images/icons/perks/diamond-hard.png"));
            LegacySkillTreeStart.create(Trait.TOUGHERMISSIONS2, map, new ImageData("images/icons/perks/diamond-hard.png"));
            LegacySkillTreeStart.create(Trait.TOUGHERMISSIONS3, map, new ImageData("images/icons/perks/diamond-hard.png"));
            LegacySkillTreeStart.create(Trait.TOUGHERMISSIONS4, map, new ImageData("images/icons/perks/diamond-hard.png"));
            LegacySkillTreeStart.create(Trait.DISCOUNTLIBRARY, map, new ImageData("images/icons/perks/cash.png"));
            LegacySkillTreeStart.create(Trait.DISCOUNTSCHOOL, map, new ImageData("images/icons/perks/cash.png"));
            LegacySkillTreeStart.create(Trait.DISCOUNTSHOPS, map, new ImageData("images/icons/perks/cash.png"));
            LegacySkillTreeStart.create(Trait.DISCOUNTSLAVES, map, new ImageData("images/icons/perks/cash.png"));
            LegacySkillTreeStart.create(Trait.BENEFACTORSTREETS, map, new ImageData("images/icons/perks/top-hat.png"));
            LegacySkillTreeStart.create(Trait.BENEFACTORSHOPS, map, new ImageData("images/icons/perks/top-hat.png"));
            LegacySkillTreeStart.create(Trait.BENEFACTORCARPENTERS, map, new ImageData("images/icons/perks/top-hat.png"));
            LegacySkillTreeStart.create(Trait.BENEFACTORSLAVEMARKET, map, new ImageData("images/icons/perks/top-hat.png"));
            ((SkillTreeItem)map.get(Trait.LEGACYWHORE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
            ((SkillTreeItem)map.get(Trait.LEGACYWHORE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
            ((SkillTreeItem)map.get(Trait.LEGACYWHORE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
            ((SkillTreeItem)map.get(Trait.LEGACYWHORE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
            ((SkillTreeItem)map.get(Trait.LEGACYWHORE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
            ((SkillTreeItem)map.get(Trait.LEGACYWHORE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
            ((SkillTreeItem)map.get(Trait.LEGACYSTRIPPER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
            ((SkillTreeItem)map.get(Trait.LEGACYSTRIPPER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
            ((SkillTreeItem)map.get(Trait.LEGACYSTRIPPER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
            ((SkillTreeItem)map.get(Trait.LEGACYSTRIPPER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
            ((SkillTreeItem)map.get(Trait.LEGACYSTRIPPER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
            ((SkillTreeItem)map.get(Trait.LEGACYSTRIPPER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
            ((SkillTreeItem)map.get(Trait.LEGACYNONE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
            ((SkillTreeItem)map.get(Trait.LEGACYNONE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
            ((SkillTreeItem)map.get(Trait.LEGACYNONE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
            ((SkillTreeItem)map.get(Trait.LEGACYNONE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
            ((SkillTreeItem)map.get(Trait.LEGACYNONE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
            ((SkillTreeItem)map.get(Trait.LEGACYNONE)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
            ((SkillTreeItem)map.get(Trait.LEGACYADVENTURER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
            ((SkillTreeItem)map.get(Trait.LEGACYADVENTURER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
            ((SkillTreeItem)map.get(Trait.LEGACYADVENTURER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
            ((SkillTreeItem)map.get(Trait.LEGACYADVENTURER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
            ((SkillTreeItem)map.get(Trait.LEGACYADVENTURER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
            ((SkillTreeItem)map.get(Trait.LEGACYADVENTURER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
            ((SkillTreeItem)map.get(Trait.LEGACYBARTENDER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
            ((SkillTreeItem)map.get(Trait.LEGACYBARTENDER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
            ((SkillTreeItem)map.get(Trait.LEGACYBARTENDER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
            ((SkillTreeItem)map.get(Trait.LEGACYBARTENDER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
            ((SkillTreeItem)map.get(Trait.LEGACYBARTENDER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
            ((SkillTreeItem)map.get(Trait.LEGACYBARTENDER)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMAID));
            ((SkillTreeItem)map.get(Trait.LEGACYMAID)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYSTRIPPER));
            ((SkillTreeItem)map.get(Trait.LEGACYMAID)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYMASSEUR));
            ((SkillTreeItem)map.get(Trait.LEGACYMAID)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYNONE));
            ((SkillTreeItem)map.get(Trait.LEGACYMAID)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYWHORE));
            ((SkillTreeItem)map.get(Trait.LEGACYMAID)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYBARTENDER));
            ((SkillTreeItem)map.get(Trait.LEGACYMAID)).getPerkUnRequirements().add(new RequiresTraitRequirement(Trait.LEGACYADVENTURER));
            LegacySkillTreeStart.link(Trait.GENIUS, Trait.LEGACYWHORE, map);
            LegacySkillTreeStart.link(Trait.GENIUS, Trait.LEGACYSTRIPPER, map);
            LegacySkillTreeStart.link(Trait.GENIUS, Trait.LEGACYMASSEUR, map);
            LegacySkillTreeStart.link(Trait.GENIUS, Trait.LEGACYADVENTURER, map);
            LegacySkillTreeStart.link(Trait.GENIUS, Trait.LEGACYBARTENDER, map);
            LegacySkillTreeStart.link(Trait.GENIUS, Trait.LEGACYMAID, map);
            LegacySkillTreeStart.link(Trait.LEGACYWHORE, Trait.HIDDENLIBRARY, map);
            LegacySkillTreeStart.link(Trait.LEGACYSTRIPPER, Trait.HIDDENLIBRARY, map);
            LegacySkillTreeStart.link(Trait.LEGACYMASSEUR, Trait.HIDDENLIBRARY, map);
            LegacySkillTreeStart.link(Trait.LEGACYADVENTURER, Trait.HIDDENLIBRARY, map);
            LegacySkillTreeStart.link(Trait.LEGACYBARTENDER, Trait.HIDDENLIBRARY, map);
            LegacySkillTreeStart.link(Trait.LEGACYMAID, Trait.HIDDENLIBRARY, map);
            LegacySkillTreeStart.link(Trait.HIDDENLIBRARY, Trait.TOUGHERMISSIONS1, map);
            LegacySkillTreeStart.link(Trait.HIDDENLIBRARY, Trait.DISCOUNTLIBRARY, map);
            LegacySkillTreeStart.link(Trait.HIDDENLIBRARY, Trait.BENEFACTORSTREETS, map);
            LegacySkillTreeStart.link(Trait.TOUGHERMISSIONS1, Trait.TOUGHERMISSIONS2, map);
            LegacySkillTreeStart.link(Trait.TOUGHERMISSIONS2, Trait.TOUGHERMISSIONS3, map);
            LegacySkillTreeStart.link(Trait.TOUGHERMISSIONS3, Trait.TOUGHERMISSIONS4, map);
            LegacySkillTreeStart.link(Trait.DISCOUNTLIBRARY, Trait.DISCOUNTSCHOOL, map);
            LegacySkillTreeStart.link(Trait.DISCOUNTSCHOOL, Trait.DISCOUNTSHOPS, map);
            LegacySkillTreeStart.link(Trait.DISCOUNTSHOPS, Trait.DISCOUNTSLAVES, map);
            LegacySkillTreeStart.link(Trait.BENEFACTORSTREETS, Trait.BENEFACTORSHOPS, map);
            LegacySkillTreeStart.link(Trait.BENEFACTORSHOPS, Trait.BENEFACTORSLAVEMARKET, map);
            LegacySkillTreeStart.link(Trait.BENEFACTORSLAVEMARKET, Trait.BENEFACTORCARPENTERS, map);
        }
    }

    public static class TrainerSkillTreeStart
    extends SkillTreeItem {
        public TrainerSkillTreeStart() {
            super(Trait.CERTIFIEDTRAINER, new ImageData("images/icons/perks/scroll-unfurled.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.CERTIFIEDTRAINER, this);
            TrainerSkillTreeStart.create(Trait.EFFECTIVETRAINER, map, new ImageData("images/icons/perks/pyromaniac.png"));
            TrainerSkillTreeStart.create(Trait.MOTIVATOR, map, new ImageData("images/icons/perks/sing.png"));
            TrainerSkillTreeStart.create(Trait.TRAINING, map, new ImageData("images/icons/perks/open-book.png"));
            TrainerSkillTreeStart.create(Trait.PERFECTTRAINER, map, new ImageData("images/icons/perks/stone-throne.png"));
            TrainerSkillTreeStart.create(Trait.BASICTRAINING1, map, new ImageData("images/icons/perks/atomic-slashes.png"));
            TrainerSkillTreeStart.create(Trait.BASICTRAINING2, map, new ImageData("images/icons/perks/atomic-slashes.png"));
            TrainerSkillTreeStart.create(Trait.BASICTRAINING3, map, new ImageData("images/icons/perks/atomic-slashes.png"));
            TrainerSkillTreeStart.create(Trait.SEXTRAINING1, map, new ImageData("images/icons/perks/flame.png"));
            TrainerSkillTreeStart.create(Trait.SEXTRAINING2, map, new ImageData("images/icons/perks/flame.png"));
            TrainerSkillTreeStart.create(Trait.SEXTRAINING3, map, new ImageData("images/icons/perks/flame.png"));
            TrainerSkillTreeStart.create(Trait.SPECIALIZATIONTRAINING1, map, new ImageData("images/icons/perks/spectacles.png"));
            TrainerSkillTreeStart.create(Trait.SPECIALIZATIONTRAINING2, map, new ImageData("images/icons/perks/spectacles.png"));
            TrainerSkillTreeStart.create(Trait.SPECIALIZATIONTRAINING3, map, new ImageData("images/icons/perks/spectacles.png"));
            TrainerSkillTreeStart.create(Trait.RESPECTED, map, new ImageData("images/icons/perks/back-forth.png"));
            TrainerSkillTreeStart.create(Trait.ONEOFUS, map, new ImageData("images/icons/perks/back-forth.png"));
            TrainerSkillTreeStart.create(Trait.STRONGPRESENCE, map, new ImageData("images/icons/perks/six-eyes.png"));
            TrainerSkillTreeStart.create(Trait.EYESEVERYWHERE, map, new ImageData("images/icons/perks/six-eyes.png"));
            TrainerSkillTreeStart.create(Trait.SPYNETWORK, map, new ImageData("images/icons/perks/six-eyes.png"));
            ((SkillTreeItem)map.get(Trait.ONEOFUS)).getPerkRequirements().add(new RequiresTraitRequirement(Trait.RESPECTED));
            TrainerSkillTreeStart.link(Trait.CERTIFIEDTRAINER, Trait.RESPECTED, map);
            TrainerSkillTreeStart.link(Trait.CERTIFIEDTRAINER, Trait.SEXTRAINING1, map);
            TrainerSkillTreeStart.link(Trait.CERTIFIEDTRAINER, Trait.BASICTRAINING1, map);
            TrainerSkillTreeStart.link(Trait.CERTIFIEDTRAINER, Trait.SPECIALIZATIONTRAINING1, map);
            TrainerSkillTreeStart.link(Trait.CERTIFIEDTRAINER, Trait.STRONGPRESENCE, map);
            TrainerSkillTreeStart.link(Trait.RESPECTED, Trait.SEXTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.RESPECTED, Trait.BASICTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.SEXTRAINING1, Trait.SEXTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.SEXTRAINING1, Trait.BASICTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.BASICTRAINING1, Trait.BASICTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING1, Trait.BASICTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING1, Trait.SPECIALIZATIONTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.STRONGPRESENCE, Trait.BASICTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.STRONGPRESENCE, Trait.SPECIALIZATIONTRAINING2, map);
            TrainerSkillTreeStart.link(Trait.SEXTRAINING2, Trait.ONEOFUS, map);
            TrainerSkillTreeStart.link(Trait.SEXTRAINING2, Trait.TRAINING, map);
            TrainerSkillTreeStart.link(Trait.BASICTRAINING2, Trait.TRAINING, map);
            TrainerSkillTreeStart.link(Trait.BASICTRAINING2, Trait.MOTIVATOR, map);
            TrainerSkillTreeStart.link(Trait.BASICTRAINING2, Trait.EFFECTIVETRAINER, map);
            TrainerSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING2, Trait.EFFECTIVETRAINER, map);
            TrainerSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING2, Trait.EYESEVERYWHERE, map);
            TrainerSkillTreeStart.link(Trait.ONEOFUS, Trait.SEXTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.ONEOFUS, Trait.BASICTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.TRAINING, Trait.SEXTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.TRAINING, Trait.BASICTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.MOTIVATOR, Trait.BASICTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.EFFECTIVETRAINER, Trait.BASICTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.EFFECTIVETRAINER, Trait.SPECIALIZATIONTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.EYESEVERYWHERE, Trait.SPECIALIZATIONTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.EYESEVERYWHERE, Trait.BASICTRAINING3, map);
            TrainerSkillTreeStart.link(Trait.SEXTRAINING3, Trait.PERFECTTRAINER, map);
            TrainerSkillTreeStart.link(Trait.BASICTRAINING3, Trait.PERFECTTRAINER, map);
            TrainerSkillTreeStart.link(Trait.BASICTRAINING3, Trait.SPYNETWORK, map);
            TrainerSkillTreeStart.link(Trait.SPECIALIZATIONTRAINING3, Trait.SPYNETWORK, map);
        }
    }

    public static class CatgirlTreeStart
    extends SkillTreeItem {
        public CatgirlTreeStart() {
            super(Trait.CATSAREASSHOLES, new ImageData("images/icons/perks/beast-eye.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.CATSAREASSHOLES, this);
            CatgirlTreeStart.create(Trait.DOMESTICATED, map, new ImageData("images/icons/perks/meat-cleaver.png"));
            CatgirlTreeStart.create(Trait.STRAYCAT, map, new ImageData("images/icons/perks/powder.png"));
            CatgirlTreeStart.create(Trait.CATWALK, map, new ImageData("images/icons/perks/aubergine.png"));
            CatgirlTreeStart.create(Trait.WETPUSSY, map, new ImageData("images/icons/perks/salt-shaker.png"));
            CatgirlTreeStart.create(Trait.NOCTURNAL, map, new ImageData("images/icons/perks/pizza-cutter.png"));
            CatgirlTreeStart.create(Trait.CATSANDRATS, map, new ImageData("images/icons/perks/shining-heart.png"));
            CatgirlTreeStart.create(Trait.CUNNING, map, new ImageData("images/icons/perks/gem-pendant.png"));
            CatgirlTreeStart.create(Trait.PAWERFUL, map, new ImageData("images/icons/perks/water-drop.png"));
            CatgirlTreeStart.create(Trait.GROOMING, map, new ImageData("images/icons/perks/hourglass.png"));
            CatgirlTreeStart.create(Trait.CATNAP, map, new ImageData("images/icons/perks/divergence.png"));
            CatgirlTreeStart.create(Trait.ALLPURRPOSE, map, new ImageData("images/icons/perks/divergence.png"));
            CatgirlTreeStart.create(Trait.CATTRACTIVE, map, new ImageData("images/icons/perks/divergence.png"));
            CatgirlTreeStart.create(Trait.CATBURGLAR, map, new ImageData("images/icons/perks/divergence.png"));
            CatgirlTreeStart.create(Trait.HIGHCATNESSRATING, map, new ImageData("images/icons/perks/divergence.png"));
            CatgirlTreeStart.link(Trait.CATSAREASSHOLES, Trait.DOMESTICATED, map);
            CatgirlTreeStart.link(Trait.CATSAREASSHOLES, Trait.STRAYCAT, map);
            CatgirlTreeStart.link(Trait.DOMESTICATED, Trait.CATWALK, map);
            CatgirlTreeStart.link(Trait.DOMESTICATED, Trait.WETPUSSY, map);
            CatgirlTreeStart.link(Trait.STRAYCAT, Trait.NOCTURNAL, map);
            CatgirlTreeStart.link(Trait.WETPUSSY, Trait.CATSANDRATS, map);
            CatgirlTreeStart.link(Trait.NOCTURNAL, Trait.CATSANDRATS, map);
            CatgirlTreeStart.link(Trait.CATWALK, Trait.CUNNING, map);
            CatgirlTreeStart.link(Trait.NOCTURNAL, Trait.PAWERFUL, map);
            CatgirlTreeStart.link(Trait.CATWALK, Trait.GROOMING, map);
            CatgirlTreeStart.link(Trait.WETPUSSY, Trait.GROOMING, map);
            CatgirlTreeStart.link(Trait.CUNNING, Trait.CATNAP, map);
            CatgirlTreeStart.link(Trait.PAWERFUL, Trait.CATNAP, map);
            CatgirlTreeStart.link(Trait.GROOMING, Trait.CATNAP, map);
            CatgirlTreeStart.link(Trait.CATSANDRATS, Trait.CATNAP, map);
            CatgirlTreeStart.link(Trait.CATNAP, Trait.ALLPURRPOSE, map);
            CatgirlTreeStart.link(Trait.CATNAP, Trait.CATTRACTIVE, map);
            CatgirlTreeStart.link(Trait.CATNAP, Trait.CATBURGLAR, map);
            CatgirlTreeStart.link(Trait.ALLPURRPOSE, Trait.HIGHCATNESSRATING, map);
            CatgirlTreeStart.link(Trait.CATTRACTIVE, Trait.HIGHCATNESSRATING, map);
            CatgirlTreeStart.link(Trait.CATBURGLAR, Trait.HIGHCATNESSRATING, map);
        }
    }

    public static class KinkySexTreeStart
    extends SkillTreeItem {
        public KinkySexTreeStart() {
            super(Trait.KINKY, new ImageData("images/icons/perks/two-shadows.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.KINKY, this);
            KinkySexTreeStart.create(Trait.PERVERT, map, new ImageData("images/icons/perks/mad-scientist.png"));
            KinkySexTreeStart.create(Trait.SUBMISSIVE, map, new ImageData("images/icons/perks/oppression.png"));
            KinkySexTreeStart.create(Trait.EXHIBITIONIST, map, new ImageData("images/icons/perks/shouting.png"));
            KinkySexTreeStart.create(Trait.SEXFREAK, map, new ImageData("images/icons/perks/imp-laugh.png"));
            KinkySexTreeStart.create(Trait.SEXADDICT, map, new ImageData("images/icons/perks/sex_addict.png"));
            KinkySexTreeStart.create(Trait.SEXMANIAC, map, new ImageData("images/icons/perks/mouth-watering.png"));
            KinkySexTreeStart.create(Trait.CUMSLUT, map, new ImageData("images/icons/perks/spill.png"));
            KinkySexTreeStart.create(Trait.FLESHTOY, map, new ImageData("images/icons/perks/spill.png"));
            KinkySexTreeStart.create(Trait.MEATTOILET, map, new ImageData("images/icons/perks/meat.png"));
            KinkySexTreeStart.create(Trait.PUBLICUSE, map, new ImageData("images/icons/perks/backup.png"));
            KinkySexTreeStart.create(Trait.GANGBANGQUEEN, map, new ImageData("images/icons/perks/dozen.png"));
            KinkySexTreeStart.create(Trait.BREEDER, map, new ImageData("images/icons/perks/shining-heart.png"));
            KinkySexTreeStart.create(Trait.INSATIABLE, map, new ImageData("images/icons/perks/swallow.png"));
            KinkySexTreeStart.create(Trait.ANYPLACE, map, new ImageData("images/icons/perks/swallow.png"));
            KinkySexTreeStart.create(Trait.MONSTERSOW, map, new ImageData("images/icons/perks/brain-freeze.png"));
            KinkySexTreeStart.link(Trait.KINKY, Trait.PERVERT, map);
            KinkySexTreeStart.link(Trait.KINKY, Trait.SUBMISSIVE, map);
            KinkySexTreeStart.link(Trait.KINKY, Trait.EXHIBITIONIST, map);
            KinkySexTreeStart.link(Trait.PERVERT, Trait.SEXFREAK, map);
            KinkySexTreeStart.link(Trait.SUBMISSIVE, Trait.SEXFREAK, map);
            KinkySexTreeStart.link(Trait.EXHIBITIONIST, Trait.SEXFREAK, map);
            KinkySexTreeStart.link(Trait.SEXFREAK, Trait.SEXADDICT, map);
            KinkySexTreeStart.link(Trait.SEXFREAK, Trait.SEXMANIAC, map);
            KinkySexTreeStart.link(Trait.SEXADDICT, Trait.CUMSLUT, map);
            KinkySexTreeStart.link(Trait.SEXMANIAC, Trait.FLESHTOY, map);
            KinkySexTreeStart.link(Trait.SEXMANIAC, Trait.ANYPLACE, map);
            KinkySexTreeStart.link(Trait.SEXADDICT, Trait.ANYPLACE, map);
            KinkySexTreeStart.link(Trait.CUMSLUT, Trait.GANGBANGQUEEN, map);
            KinkySexTreeStart.link(Trait.CUMSLUT, Trait.PUBLICUSE, map);
            KinkySexTreeStart.link(Trait.FLESHTOY, Trait.MEATTOILET, map);
            KinkySexTreeStart.link(Trait.FLESHTOY, Trait.BREEDER, map);
            KinkySexTreeStart.link(Trait.ANYPLACE, Trait.PUBLICUSE, map);
            KinkySexTreeStart.link(Trait.ANYPLACE, Trait.MEATTOILET, map);
            KinkySexTreeStart.link(Trait.PUBLICUSE, Trait.INSATIABLE, map);
            KinkySexTreeStart.link(Trait.GANGBANGQUEEN, Trait.INSATIABLE, map);
            KinkySexTreeStart.link(Trait.MEATTOILET, Trait.MONSTERSOW, map);
            KinkySexTreeStart.link(Trait.BREEDER, Trait.MONSTERSOW, map);
        }
    }

    public static class ThiefTreeStart
    extends SkillTreeItem {
        public ThiefTreeStart() {
            super(Trait.GREEDY, new ImageData("images/icons/perks/greedy.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.GREEDY, this);
            ThiefTreeStart.create(Trait.NIMBLEHANDS, map, new ImageData("images/icons/perks/nimble_hands.png"));
            ThiefTreeStart.create(Trait.PICKPOCKET, map, new ImageData("images/icons/perks/nimblehands.png"));
            ThiefTreeStart.create(Trait.ROGUE, map, new ImageData("images/icons/perks/rogue.png"));
            ThiefTreeStart.create(Trait.LUPIN, map, new ImageData("images/icons/perks/lupin.png"));
            ThiefTreeStart.create(Trait.CONARTIST, map, new ImageData("images/icons/perks/con_artist.png"));
            ThiefTreeStart.create(Trait.RESELLER, map, new ImageData("images/icons/perks/reseller.png"));
            ThiefTreeStart.create(Trait.BANDIT, map, new ImageData("images/icons/perks/bandit.png"));
            ThiefTreeStart.create(Trait.LIAISONSDANGEREUSES, map, new ImageData("images/icons/perks/liaisons_dangereuses.png"));
            ThiefTreeStart.create(Trait.NIGHTSHADE, map, new ImageData("images/icons/perks/nightshade.png"));
            ThiefTreeStart.create(Trait.ASSASSIN, map, new ImageData("images/icons/perks/assassin.png"));
            ThiefTreeStart.create(Trait.SHADOWBODY, map, new ImageData("images/icons/perks/shadow_body.png"));
            ThiefTreeStart.create(Trait.PHANTOMTHIEF, map, new ImageData("images/icons/perks/phantom_theif.png"));
            ThiefTreeStart.create(Trait.DOUBLEVIE, map, new ImageData("images/icons/perks/doublevie.png"));
            ThiefTreeStart.link(Trait.GREEDY, Trait.NIMBLEHANDS, map);
            ThiefTreeStart.link(Trait.GREEDY, Trait.PICKPOCKET, map);
            ThiefTreeStart.link(Trait.GREEDY, Trait.ROGUE, map);
            ThiefTreeStart.link(Trait.NIMBLEHANDS, Trait.LUPIN, map);
            ThiefTreeStart.link(Trait.PICKPOCKET, Trait.LUPIN, map);
            ThiefTreeStart.link(Trait.ROGUE, Trait.LUPIN, map);
            ThiefTreeStart.link(Trait.LUPIN, Trait.CONARTIST, map);
            ThiefTreeStart.link(Trait.LUPIN, Trait.RESELLER, map);
            ThiefTreeStart.link(Trait.LUPIN, Trait.BANDIT, map);
            ThiefTreeStart.link(Trait.CONARTIST, Trait.LIAISONSDANGEREUSES, map);
            ThiefTreeStart.link(Trait.RESELLER, Trait.LIAISONSDANGEREUSES, map);
            ThiefTreeStart.link(Trait.BANDIT, Trait.LIAISONSDANGEREUSES, map);
            ThiefTreeStart.link(Trait.LIAISONSDANGEREUSES, Trait.NIGHTSHADE, map);
            ThiefTreeStart.link(Trait.LIAISONSDANGEREUSES, Trait.ASSASSIN, map);
            ThiefTreeStart.link(Trait.NIGHTSHADE, Trait.PHANTOMTHIEF, map);
            ThiefTreeStart.link(Trait.NIGHTSHADE, Trait.DOUBLEVIE, map);
            ThiefTreeStart.link(Trait.ASSASSIN, Trait.DOUBLEVIE, map);
            ThiefTreeStart.link(Trait.ASSASSIN, Trait.SHADOWBODY, map);
        }
    }

    public static class MaidTreeStart
    extends SkillTreeItem {
        public MaidTreeStart() {
            super(Trait.PROFESSIONAL, new ImageData("images/icons/perks/fairy-wand.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.PROFESSIONAL, this);
            MaidTreeStart.create(Trait.CORDONBLEU, map, new ImageData("images/icons/perks/meat-cleaver.png"));
            MaidTreeStart.create(Trait.COMPULSIVECLEANER, map, new ImageData("images/icons/perks/powder.png"));
            MaidTreeStart.create(Trait.DIETEXPERT, map, new ImageData("images/icons/perks/aubergine.png"));
            MaidTreeStart.create(Trait.CHEF, map, new ImageData("images/icons/perks/salt-shaker.png"));
            MaidTreeStart.create(Trait.PRACTICAL, map, new ImageData("images/icons/perks/pizza-cutter.png"));
            MaidTreeStart.create(Trait.HOUSEFAIRY, map, new ImageData("images/icons/perks/fairy-wand.png"));
            MaidTreeStart.create(Trait.MOTHERLYCARE, map, new ImageData("images/icons/perks/shining-heart.png"));
            MaidTreeStart.create(Trait.ELEGANT, map, new ImageData("images/icons/perks/gem-pendant.png"));
            MaidTreeStart.create(Trait.WASHINGANDIRONING, map, new ImageData("images/icons/perks/water-drop.png"));
            MaidTreeStart.create(Trait.TIMEMANIPULATION, map, new ImageData("images/icons/perks/hourglass.png"));
            MaidTreeStart.create(Trait.ALWAYSIMPROVE, map, new ImageData("images/icons/perks/divergence.png"));
            MaidTreeStart.create(Trait.CULINARYDELIGHTS, map, new ImageData("images/icons/perks/culinarydelights.png"));
            MaidTreeStart.link(Trait.PROFESSIONAL, Trait.CORDONBLEU, map);
            MaidTreeStart.link(Trait.PROFESSIONAL, Trait.COMPULSIVECLEANER, map);
            MaidTreeStart.link(Trait.CORDONBLEU, Trait.DIETEXPERT, map);
            MaidTreeStart.link(Trait.CORDONBLEU, Trait.CHEF, map);
            MaidTreeStart.link(Trait.COMPULSIVECLEANER, Trait.PRACTICAL, map);
            MaidTreeStart.link(Trait.COMPULSIVECLEANER, Trait.HOUSEFAIRY, map);
            MaidTreeStart.link(Trait.DIETEXPERT, Trait.MOTHERLYCARE, map);
            MaidTreeStart.link(Trait.CHEF, Trait.MOTHERLYCARE, map);
            MaidTreeStart.link(Trait.PRACTICAL, Trait.WASHINGANDIRONING, map);
            MaidTreeStart.link(Trait.HOUSEFAIRY, Trait.WASHINGANDIRONING, map);
            MaidTreeStart.link(Trait.WASHINGANDIRONING, Trait.ELEGANT, map);
            MaidTreeStart.link(Trait.MOTHERLYCARE, Trait.ELEGANT, map);
            MaidTreeStart.link(Trait.ELEGANT, Trait.CULINARYDELIGHTS, map);
            MaidTreeStart.link(Trait.ELEGANT, Trait.ALWAYSIMPROVE, map);
            MaidTreeStart.link(Trait.ELEGANT, Trait.TIMEMANIPULATION, map);
        }
    }

    public static class WhoreTreeStart
    extends SkillTreeItem {
        public WhoreTreeStart() {
            super(Trait.SEDUCTRESS, new ImageData("images/icons/perks/charm.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.SEDUCTRESS, this);
            WhoreTreeStart.create(Trait.ENDURANCE, map, new ImageData("images/icons/perks/burning-passion.png"));
            WhoreTreeStart.create(Trait.CHATTY, map, new ImageData("images/icons/perks/conversation.png"));
            WhoreTreeStart.create(Trait.FIRST, map, new ImageData("images/icons/perks/love-song.png"));
            WhoreTreeStart.create(Trait.COMPETITIVE, map, new ImageData("images/icons/perks/love-howl.png"));
            WhoreTreeStart.create(Trait.STYLISH, map, new ImageData("images/icons/perks/gems.png"));
            WhoreTreeStart.create(Trait.COUPLEMORE, map, new ImageData("images/icons/perks/backup.png"));
            WhoreTreeStart.create(Trait.WENCH, map, new ImageData("images/icons/perks/pretty-fangs.png"));
            WhoreTreeStart.create(Trait.JUSTYOUANDME, map, new ImageData("images/icons/perks/lips.png"));
            WhoreTreeStart.create(Trait.NUTBUSTER, map, new ImageData("images/icons/perks/terror.png"));
            WhoreTreeStart.create(Trait.QUICKIE, map, new ImageData("images/icons/perks/sands-of-time.png"));
            WhoreTreeStart.create(Trait.SITBACK, map, new ImageData("images/icons/perks/chemical-bolt.png"));
            WhoreTreeStart.create(Trait.NONNEGOCIABLE, map, new ImageData("images/icons/perks/cash.png"));
            WhoreTreeStart.create(Trait.TAKEOURTIME, map, new ImageData("images/icons/perks/time-trap.png"));
            WhoreTreeStart.create(Trait.STREETSMARTS, map, new ImageData("images/icons/perks/seated-mouse.png"));
            WhoreTreeStart.create(Trait.KEEPEMCOMING, map, new ImageData("images/icons/perks/minions.png"));
            WhoreTreeStart.create(Trait.THENIGHTISSTILLYOUNG, map, new ImageData("images/icons/perks/moon.png"));
            WhoreTreeStart.create(Trait.THATGIRL, map, new ImageData("images/icons/perks/paper-lantern.png"));
            WhoreTreeStart.create(Trait.SLOPPY, map, new ImageData("images/icons/perks/dozen.png"));
            WhoreTreeStart.create(Trait.ONENIGHT, map, new ImageData("images/icons/perks/star-swirl.png"));
            WhoreTreeStart.link(Trait.SEDUCTRESS, Trait.ENDURANCE, map);
            WhoreTreeStart.link(Trait.SEDUCTRESS, Trait.CHATTY, map);
            WhoreTreeStart.link(Trait.SEDUCTRESS, Trait.FIRST, map);
            WhoreTreeStart.link(Trait.ENDURANCE, Trait.COMPETITIVE, map);
            WhoreTreeStart.link(Trait.CHATTY, Trait.COMPETITIVE, map);
            WhoreTreeStart.link(Trait.CHATTY, Trait.STYLISH, map);
            WhoreTreeStart.link(Trait.FIRST, Trait.STYLISH, map);
            WhoreTreeStart.link(Trait.COMPETITIVE, Trait.COUPLEMORE, map);
            WhoreTreeStart.link(Trait.COMPETITIVE, Trait.WENCH, map);
            WhoreTreeStart.link(Trait.STYLISH, Trait.WENCH, map);
            WhoreTreeStart.link(Trait.STYLISH, Trait.JUSTYOUANDME, map);
            WhoreTreeStart.link(Trait.COUPLEMORE, Trait.NUTBUSTER, map);
            WhoreTreeStart.link(Trait.COUPLEMORE, Trait.QUICKIE, map);
            WhoreTreeStart.link(Trait.COUPLEMORE, Trait.SITBACK, map);
            WhoreTreeStart.link(Trait.WENCH, Trait.SITBACK, map);
            WhoreTreeStart.link(Trait.JUSTYOUANDME, Trait.SITBACK, map);
            WhoreTreeStart.link(Trait.JUSTYOUANDME, Trait.NONNEGOCIABLE, map);
            WhoreTreeStart.link(Trait.JUSTYOUANDME, Trait.TAKEOURTIME, map);
            WhoreTreeStart.link(Trait.NUTBUSTER, Trait.STREETSMARTS, map);
            WhoreTreeStart.link(Trait.QUICKIE, Trait.STREETSMARTS, map);
            WhoreTreeStart.link(Trait.SITBACK, Trait.KEEPEMCOMING, map);
            WhoreTreeStart.link(Trait.TAKEOURTIME, Trait.THENIGHTISSTILLYOUNG, map);
            WhoreTreeStart.link(Trait.NONNEGOCIABLE, Trait.THENIGHTISSTILLYOUNG, map);
            WhoreTreeStart.link(Trait.STREETSMARTS, Trait.THATGIRL, map);
            WhoreTreeStart.link(Trait.KEEPEMCOMING, Trait.SLOPPY, map);
            WhoreTreeStart.link(Trait.THENIGHTISSTILLYOUNG, Trait.ONENIGHT, map);
        }
    }

    public static class WarriorSkillTreeStart
    extends SkillTreeItem {
        public WarriorSkillTreeStart() {
            super(Trait.SHARPSENSES, new ImageData("images/icons/perks/sharp_senses.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.SHARPSENSES, this);
            WarriorSkillTreeStart.create(Trait.WEAPONMASTERY, map, new ImageData("images/icons/perks/weapon_mastery.png"));
            WarriorSkillTreeStart.create(Trait.TOUGH, map, new ImageData("images/icons/perks/tough.png"));
            WarriorSkillTreeStart.create(Trait.ELEMENTALSTUDY, map, new ImageData("images/icons/perks/battlemage.png"));
            WarriorSkillTreeStart.create(Trait.MINDOFTHEFIGHTER, map, new ImageData("images/icons/perks/mind_of_the_fighter.png"));
            WarriorSkillTreeStart.create(Trait.IRONBODY, map, new ImageData("images/icons/perks/iron_body.png"));
            WarriorSkillTreeStart.create(Trait.ETHERSHIELD, map, new ImageData("images/icons/perks/ether_sheild.png"));
            WarriorSkillTreeStart.create(Trait.SHOWTIME, map, new ImageData("images/icons/perks/showtime.png"));
            WarriorSkillTreeStart.create(Trait.PLUNDERER, map, new ImageData("images/icons/perks/plunderer.png"));
            WarriorSkillTreeStart.create(Trait.VITALPOINTSPIERCING, map, new ImageData("images/icons/perks/vital_points_piercing.png"));
            WarriorSkillTreeStart.create(Trait.MKIIWALKER, map, new ImageData("images/icons/perks/mxii_walker.png"));
            WarriorSkillTreeStart.create(Trait.CASTTIME, map, new ImageData("images/icons/perks/long_cast_time.png"));
            WarriorSkillTreeStart.create(Trait.DISTRACTION, map, new ImageData("images/icons/perks/distraction.png"));
            WarriorSkillTreeStart.create(Trait.LOSTARTS, map, new ImageData("images/icons/perks/lost_arts.png"));
            WarriorSkillTreeStart.link(Trait.SHARPSENSES, Trait.WEAPONMASTERY, map);
            WarriorSkillTreeStart.link(Trait.SHARPSENSES, Trait.TOUGH, map);
            WarriorSkillTreeStart.link(Trait.SHARPSENSES, Trait.ELEMENTALSTUDY, map);
            WarriorSkillTreeStart.link(Trait.WEAPONMASTERY, Trait.MINDOFTHEFIGHTER, map);
            WarriorSkillTreeStart.link(Trait.TOUGH, Trait.IRONBODY, map);
            WarriorSkillTreeStart.link(Trait.ELEMENTALSTUDY, Trait.ETHERSHIELD, map);
            WarriorSkillTreeStart.link(Trait.MINDOFTHEFIGHTER, Trait.SHOWTIME, map);
            WarriorSkillTreeStart.link(Trait.IRONBODY, Trait.SHOWTIME, map);
            WarriorSkillTreeStart.link(Trait.ETHERSHIELD, Trait.SHOWTIME, map);
            WarriorSkillTreeStart.link(Trait.MINDOFTHEFIGHTER, Trait.PLUNDERER, map);
            WarriorSkillTreeStart.link(Trait.IRONBODY, Trait.PLUNDERER, map);
            WarriorSkillTreeStart.link(Trait.ETHERSHIELD, Trait.PLUNDERER, map);
            WarriorSkillTreeStart.link(Trait.SHOWTIME, Trait.VITALPOINTSPIERCING, map);
            WarriorSkillTreeStart.link(Trait.SHOWTIME, Trait.MKIIWALKER, map);
            WarriorSkillTreeStart.link(Trait.SHOWTIME, Trait.CASTTIME, map);
            WarriorSkillTreeStart.link(Trait.SHOWTIME, Trait.DISTRACTION, map);
            WarriorSkillTreeStart.link(Trait.PLUNDERER, Trait.VITALPOINTSPIERCING, map);
            WarriorSkillTreeStart.link(Trait.PLUNDERER, Trait.MKIIWALKER, map);
            WarriorSkillTreeStart.link(Trait.PLUNDERER, Trait.CASTTIME, map);
            WarriorSkillTreeStart.link(Trait.PLUNDERER, Trait.DISTRACTION, map);
            WarriorSkillTreeStart.link(Trait.VITALPOINTSPIERCING, Trait.LOSTARTS, map);
            WarriorSkillTreeStart.link(Trait.MKIIWALKER, Trait.LOSTARTS, map);
            WarriorSkillTreeStart.link(Trait.CASTTIME, Trait.LOSTARTS, map);
            WarriorSkillTreeStart.link(Trait.DISTRACTION, Trait.LOSTARTS, map);
        }
    }

    public static class AdvertisingTreeStart
    extends SkillTreeItem {
        public AdvertisingTreeStart() {
            super(Trait.INITIATE, new ImageData("images/icons/perks/beast-eye.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.INITIATE, this);
            AdvertisingTreeStart.create(Trait.SAMPLINGTHEGOODS, map, new ImageData("images/icons/perks/samplingthegoods.png"));
            AdvertisingTreeStart.create(Trait.SHOWINGTHEGOODS, map, new ImageData("images/icons/perks/showingthegoods.png"));
            AdvertisingTreeStart.create(Trait.SHOWOFF, map, new ImageData("images/icons/perks/showoff.png"));
            AdvertisingTreeStart.create(Trait.CATCHY, map, new ImageData("images/icons/perks/catchy.png"));
            AdvertisingTreeStart.create(Trait.HEYGUYSBOOSE, map, new ImageData("images/icons/perks/heyguysboose.png"));
            AdvertisingTreeStart.create(Trait.SPIRITED, map, new ImageData("images/icons/perks/spirited.png"));
            AdvertisingTreeStart.create(Trait.TARGETBUM, map, new ImageData("images/icons/perks/targetbum.png"));
            AdvertisingTreeStart.create(Trait.TARGETPEASANT, map, new ImageData("images/icons/perks/targetpeasant.png"));
            AdvertisingTreeStart.create(Trait.TARGETSOLDIER, map, new ImageData("images/icons/perks/targetsoldier.png"));
            AdvertisingTreeStart.create(Trait.TARGETBUSINESSMEN, map, new ImageData("images/icons/perks/targetbusinessmen.png"));
            AdvertisingTreeStart.create(Trait.CONFIRMEDSALESPERSON, map, new ImageData("images/icons/perks/confirmedsalesperson.png"));
            AdvertisingTreeStart.create(Trait.SALESPROMOTION, map, new ImageData("images/icons/perks/salespromotion.png"));
            AdvertisingTreeStart.create(Trait.RECOGNIZED, map, new ImageData("images/icons/perks/recognised.png"));
            AdvertisingTreeStart.create(Trait.NICHEMARKETINGNOBLE, map, new ImageData("images/icons/perks/nichemarketingnoble.png"));
            AdvertisingTreeStart.create(Trait.NICHEMARKETINGLORD, map, new ImageData("images/icons/perks/nichemarketinglord.png"));
            AdvertisingTreeStart.create(Trait.NICHEMARKETINGCELEBRITY, map, new ImageData("images/icons/perks/nichemarketingcelebrity.png"));
            AdvertisingTreeStart.create(Trait.NICHEMARKETINGGROUPS, map, new ImageData("images/icons/perks/nichemarketinggroup.png"));
            AdvertisingTreeStart.create(Trait.BUSINESSRELATIONS, map, new ImageData("images/icons/perks/businessrelations.png"));
            AdvertisingTreeStart.link(Trait.INITIATE, Trait.SAMPLINGTHEGOODS, map);
            AdvertisingTreeStart.link(Trait.INITIATE, Trait.SHOWINGTHEGOODS, map);
            AdvertisingTreeStart.link(Trait.INITIATE, Trait.SHOWOFF, map);
            AdvertisingTreeStart.link(Trait.INITIATE, Trait.CATCHY, map);
            AdvertisingTreeStart.link(Trait.INITIATE, Trait.HEYGUYSBOOSE, map);
            AdvertisingTreeStart.link(Trait.SAMPLINGTHEGOODS, Trait.SPIRITED, map);
            AdvertisingTreeStart.link(Trait.SHOWINGTHEGOODS, Trait.SPIRITED, map);
            AdvertisingTreeStart.link(Trait.SHOWOFF, Trait.SPIRITED, map);
            AdvertisingTreeStart.link(Trait.CATCHY, Trait.SPIRITED, map);
            AdvertisingTreeStart.link(Trait.HEYGUYSBOOSE, Trait.SPIRITED, map);
            AdvertisingTreeStart.link(Trait.SPIRITED, Trait.TARGETBUM, map);
            AdvertisingTreeStart.link(Trait.SPIRITED, Trait.TARGETPEASANT, map);
            AdvertisingTreeStart.link(Trait.SPIRITED, Trait.TARGETSOLDIER, map);
            AdvertisingTreeStart.link(Trait.SPIRITED, Trait.TARGETBUSINESSMEN, map);
            AdvertisingTreeStart.link(Trait.SPIRITED, Trait.CONFIRMEDSALESPERSON, map);
            AdvertisingTreeStart.link(Trait.TARGETBUM, Trait.SALESPROMOTION, map);
            AdvertisingTreeStart.link(Trait.TARGETPEASANT, Trait.SALESPROMOTION, map);
            AdvertisingTreeStart.link(Trait.TARGETSOLDIER, Trait.SALESPROMOTION, map);
            AdvertisingTreeStart.link(Trait.TARGETBUSINESSMEN, Trait.SALESPROMOTION, map);
            AdvertisingTreeStart.link(Trait.CONFIRMEDSALESPERSON, Trait.SALESPROMOTION, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.SALESPROMOTION, Trait.RECOGNIZED, map);
            AdvertisingTreeStart.link(Trait.RECOGNIZED, Trait.NICHEMARKETINGNOBLE, map);
            AdvertisingTreeStart.link(Trait.RECOGNIZED, Trait.NICHEMARKETINGLORD, map);
            AdvertisingTreeStart.link(Trait.RECOGNIZED, Trait.NICHEMARKETINGCELEBRITY, map);
            AdvertisingTreeStart.link(Trait.RECOGNIZED, Trait.NICHEMARKETINGGROUPS, map);
            AdvertisingTreeStart.link(Trait.RECOGNIZED, Trait.BUSINESSRELATIONS, map);
        }
    }

    public static class SexTreeStart
    extends SkillTreeItem {
        public SexTreeStart() {
            super(Trait.DEBUTANTE, new ImageData("images/icons/perks/debutante.png"), new SkillTreeItem[0]);
            HashMap<Trait, SkillTreeItem> map = new HashMap<Trait, SkillTreeItem>();
            map.put(Trait.DEBUTANTE, this);
            SexTreeStart.create(Trait.SENSITIVECLIT, map, new ImageData("images/icons/perks/sensitiveclit.png"));
            SexTreeStart.create(Trait.DEEPLOVE, map, new ImageData("images/icons/perks/deeplove.png"));
            SexTreeStart.create(Trait.LOVETHETASTE, map, new ImageData("images/icons/perks/lovethetaste.png"));
            SexTreeStart.create(Trait.AFLEURDEPEAU, map, new ImageData("images/icons/perks/afleurdepeau.png"));
            SexTreeStart.create(Trait.MEATBUNS, map, new ImageData("images/icons/perks/meatbuns.png"));
            SexTreeStart.create(Trait.COZYCUNT, map, new ImageData("images/icons/perks/cozycunt.png"));
            SexTreeStart.create(Trait.ROWDYRUMP, map, new ImageData("images/icons/perks/rowdyrump.png"));
            SexTreeStart.create(Trait.SLURPYSLURP, map, new ImageData("images/icons/perks/slurpyslurp.png"));
            SexTreeStart.create(Trait.TOUCHYFEELY, map, new ImageData("images/icons/perks/touchyfeely.png"));
            SexTreeStart.create(Trait.PUFFPUFF, map, new ImageData("images/icons/perks/puffpuff.png"));
            SexTreeStart.create(Trait.BEDROOMPRINCESS, map, new ImageData("images/icons/perks/bedroomprincess.png"));
            SexTreeStart.create(Trait.WETFORYOU, map, new ImageData("images/icons/perks/wetforyou.png"));
            SexTreeStart.create(Trait.BACKDOOROPEN, map, new ImageData("images/icons/perks/backdooropen.png"));
            SexTreeStart.create(Trait.THIRSTY, map, new ImageData("images/icons/perks/thirsty.png"));
            SexTreeStart.create(Trait.FEELMEUP, map, new ImageData("images/icons/perks/feelmeup.png"));
            SexTreeStart.create(Trait.COMETOMOMMY, map, new ImageData("images/icons/perks/cometomommy.png"));
            SexTreeStart.create(Trait.STEAMY, map, new ImageData("images/icons/perks/steamy.png"));
            SexTreeStart.create(Trait.BEDGODDESS, map, new ImageData("images/icons/perks/bedgoddess.png"));
            SexTreeStart.link(Trait.DEBUTANTE, Trait.SENSITIVECLIT, map);
            SexTreeStart.link(Trait.DEBUTANTE, Trait.DEEPLOVE, map);
            SexTreeStart.link(Trait.DEBUTANTE, Trait.LOVETHETASTE, map);
            SexTreeStart.link(Trait.DEBUTANTE, Trait.AFLEURDEPEAU, map);
            SexTreeStart.link(Trait.DEBUTANTE, Trait.MEATBUNS, map);
            SexTreeStart.link(Trait.SENSITIVECLIT, Trait.COZYCUNT, map);
            SexTreeStart.link(Trait.DEEPLOVE, Trait.ROWDYRUMP, map);
            SexTreeStart.link(Trait.LOVETHETASTE, Trait.SLURPYSLURP, map);
            SexTreeStart.link(Trait.AFLEURDEPEAU, Trait.TOUCHYFEELY, map);
            SexTreeStart.link(Trait.MEATBUNS, Trait.PUFFPUFF, map);
            SexTreeStart.link(Trait.COZYCUNT, Trait.BEDROOMPRINCESS, map);
            SexTreeStart.link(Trait.ROWDYRUMP, Trait.BEDROOMPRINCESS, map);
            SexTreeStart.link(Trait.SLURPYSLURP, Trait.BEDROOMPRINCESS, map);
            SexTreeStart.link(Trait.TOUCHYFEELY, Trait.BEDROOMPRINCESS, map);
            SexTreeStart.link(Trait.PUFFPUFF, Trait.BEDROOMPRINCESS, map);
            SexTreeStart.link(Trait.BEDROOMPRINCESS, Trait.WETFORYOU, map);
            SexTreeStart.link(Trait.BEDROOMPRINCESS, Trait.BACKDOOROPEN, map);
            SexTreeStart.link(Trait.BEDROOMPRINCESS, Trait.THIRSTY, map);
            SexTreeStart.link(Trait.BEDROOMPRINCESS, Trait.FEELMEUP, map);
            SexTreeStart.link(Trait.BEDROOMPRINCESS, Trait.COMETOMOMMY, map);
            SexTreeStart.link(Trait.WETFORYOU, Trait.STEAMY, map);
            SexTreeStart.link(Trait.WETFORYOU, Trait.BEDGODDESS, map);
            SexTreeStart.link(Trait.BACKDOOROPEN, Trait.STEAMY, map);
            SexTreeStart.link(Trait.BACKDOOROPEN, Trait.BEDGODDESS, map);
            SexTreeStart.link(Trait.THIRSTY, Trait.STEAMY, map);
            SexTreeStart.link(Trait.THIRSTY, Trait.BEDGODDESS, map);
            SexTreeStart.link(Trait.FEELMEUP, Trait.STEAMY, map);
            SexTreeStart.link(Trait.FEELMEUP, Trait.BEDGODDESS, map);
            SexTreeStart.link(Trait.COMETOMOMMY, Trait.STEAMY, map);
            SexTreeStart.link(Trait.COMETOMOMMY, Trait.BEDGODDESS, map);
        }
    }

    public class RequiresTraitRequirement
    implements PerkRequirement {
        private Trait perk;

        public RequiresTraitRequirement(Trait perk) {
            this.perk = perk;
        }

        @Override
        public boolean isRequirementMet(Charakter character) {
            return character.getTraits().contains(this.perk);
        }

        @Override
        public String getRequirementDescription() {
            return "Requires: " + this.perk.getText();
        }
    }

    public class PerkNotPresentRequirement
    implements PerkRequirement {
        private Trait perk;

        public PerkNotPresentRequirement(Trait perk) {
            this.perk = perk;
        }

        @Override
        public boolean isRequirementMet(Charakter character) {
            return !character.getTraits().contains(this.perk);
        }

        @Override
        public String getRequirementDescription() {
            return "Can not have: " + this.perk.getText();
        }
    }

    public class AttributeRequirement
    implements PerkRequirement {
        private AttributeType attributeType;
        private int amount;

        public AttributeRequirement(AttributeType attributeType, int amount) {
            this.attributeType = attributeType;
            this.amount = amount;
        }

        @Override
        public boolean isRequirementMet(Charakter character) {
            return character.getAttribute(this.attributeType).getInternValue() >= (float)this.amount;
        }

        @Override
        public String getRequirementDescription() {
            return this.attributeType.getText() + ": " + this.amount;
        }
    }

    public static interface PerkRequirement
    extends Serializable {
        public boolean isRequirementMet(Charakter var1);

        public String getRequirementDescription();
    }
}

