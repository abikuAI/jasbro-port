/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character.traits;

import jasbro.game.character.traits.SkillTreeItem;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;

public enum SkillTree {
    LEGACY(new SkillTreeItem.LegacySkillTreeStart(), new ImageData("images/icons/perks/trainer.png")),
    TRAINER(new SkillTreeItem.TrainerSkillTreeStart(), new ImageData("images/icons/perks/trainer.png")),
    SLAVE(new SkillTreeItem.SlaveSkillTreeStart(), new ImageData("images/icons/perks/slave.png")),
    FIGHTER(new SkillTreeItem.WarriorSkillTreeStart(), new ImageData("images/icons/perks/warrior.png")),
    MAID(new SkillTreeItem.MaidTreeStart(), new ImageData("images/icons/perks/maid.png")),
    THIEF(new SkillTreeItem.ThiefTreeStart(), new ImageData("images/icons/perks/thief.png")),
    KINKYSEX(new SkillTreeItem.KinkySexTreeStart(), new ImageData("images/icons/perks/kinky.png")),
    FURRY(new SkillTreeItem.FurryTreeStart(), new ImageData("images/icons/perks/mutant.png")),
    WHORE(new SkillTreeItem.WhoreTreeStart(), new ImageData("images/icons/perks/whore.png")),
    BARTENDER(new SkillTreeItem.BartenderTreeStart(), new ImageData("images/icons/perks/bartender.png")),
    SEX(new SkillTreeItem.SexTreeStart(), new ImageData("images/icons/perks/sex.png")),
    MARKETINGEXPERT(new SkillTreeItem.AdvertisingTreeStart(), new ImageData("images/icons/perks/marketing.png")),
    DANCER(new SkillTreeItem.DancerTreeStart(), new ImageData("images/icons/perks/dancer.png")),
    CATGIRL(new SkillTreeItem.CatgirlTreeStart(), new ImageData("images/icons/perks/catgirl.png")),
    ALCHEMIST(new SkillTreeItem.AlchemistTreeStart(), new ImageData("images/icons/perks/alchemy.png")),
    NURSE(new SkillTreeItem.NurseTreeStart(), new ImageData("images/icons/perks/nurse.png")),
    DOMINATRIX(new SkillTreeItem.DominatrixTreeStart(), new ImageData("images/icons/perks/bondage.png"));

    private SkillTreeItem firstItem;
    private ImageData icon;

    private SkillTree(SkillTreeItem firstItem, ImageData icon) {
        this.firstItem = firstItem;
        this.icon = icon;
    }

    public SkillTreeItem getFirstItem() {
        return this.firstItem;
    }

    public ImageData getIcon() {
        return this.icon;
    }

    public String getText() {
        return TextUtil.t(this.toString());
    }
}

