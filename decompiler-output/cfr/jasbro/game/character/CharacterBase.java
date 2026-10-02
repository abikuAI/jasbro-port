/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.java.truevfs.access.TFile
 */
package jasbro.game.character;

import jasbro.game.character.AgeProgressionData;
import jasbro.game.character.CharacterType;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.texts.TextUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.java.truevfs.access.TFile;

public class CharacterBase
implements Serializable,
HasImagesInterface {
    private String id;
    private String name;
    private List<ImageData> images = new ArrayList<ImageData>();
    private CharacterType type = null;
    private Map<BaseAttributeTypes, Integer> attributes = new EnumMap<BaseAttributeTypes, Integer>(BaseAttributeTypes.class);
    private Gender gender = Gender.FEMALE;
    private Set<Trait> traits = EnumSet.noneOf(Trait.class);
    private TFile folder;
    private boolean changed = false;
    private String description;
    private String youngerBase;
    private String olderBase;
    private SpecializationType initialSpecialization;
    private AgeProgressionData ageProgressionData;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public List<ImageData> getImages() {
        return this.images;
    }

    public void setImages(List<ImageData> images) {
        this.images = images;
    }

    public CharacterType getType() {
        return this.type;
    }

    public void setType(CharacterType type) {
        this.type = type;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAttribute(BaseAttributeTypes key) {
        if (key == null) {
            return 1;
        }
        if (!this.attributes.containsKey(key)) {
            return 6;
        }
        return this.attributes.get(key);
    }

    public Integer setAttribute(BaseAttributeTypes key, Integer value) {
        if (key == null) {
            return null;
        }
        if (this.attributes.containsKey(key)) {
            this.attributes.remove(key);
        }
        return this.attributes.put(key, value);
    }

    public Set<Map.Entry<BaseAttributeTypes, Integer>> getAttributes() {
        return this.attributes.entrySet();
    }

    public String toString() {
        return this.getName();
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());
        return result;
    }

    public void addTrait(Trait trait) {
        if (!this.traits.contains(trait)) {
            this.traits.add(trait);
        }
    }

    public void removeTrait(Trait trait) {
        this.traits.remove(trait);
    }

    public Set<Trait> getTraits() {
        return this.traits;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        CharacterBase other = (CharacterBase)obj;
        return !(this.id == null ? other.id != null : !this.id.equals(other.id));
    }

    public TFile getFolder() {
        return this.folder;
    }

    public void setFolder(TFile folder) {
        this.folder = folder;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getYoungerBase() {
        return this.youngerBase;
    }

    public void setYoungerBase(String previousBase) {
        this.youngerBase = previousBase;
    }

    public String getOlderBase() {
        return this.olderBase;
    }

    public void setOlderBase(String nextBase) {
        this.olderBase = nextBase;
    }

    public SpecializationType getInitialSpecialization() {
        return this.initialSpecialization;
    }

    public void setInitialSpecialization(SpecializationType initialSpecialization) {
        this.initialSpecialization = initialSpecialization;
    }

    public AgeProgressionData getAgeProgressionData() {
        if (this.ageProgressionData == null) {
            this.ageProgressionData = new AgeProgressionData();
        }
        return this.ageProgressionData;
    }

    public String getFullDescription() {
        String description = "";
        if (this.description != null && !this.description.equals("")) {
            description = description + TextUtil.t("ui.description") + "\n" + this.getDescription();
        }
        description = description + "\n\n";
        description = description + TextUtil.t("ui.baseId", this.getId()) + "\n";
        description = description + TextUtil.t("ui.imageAmount", this.getImages().size()) + "\n\n";
        description = description + TextUtil.t("ui.startAttributes", this.getAttribute(BaseAttributeTypes.CHARISMA), this.getAttribute(BaseAttributeTypes.OBEDIENCE), this.getAttribute(BaseAttributeTypes.STAMINA), this.getAttribute(BaseAttributeTypes.INTELLIGENCE), this.getAttribute(BaseAttributeTypes.STRENGTH)) + "\n\n";
        if (this.getInitialSpecialization() != null) {
            description = description + TextUtil.t("ui.startSpecialization", this.getInitialSpecialization().getText()) + "\n\n";
        }
        return description;
    }

    @Override
    public List<ImageTag> getBaseTags() {
        return new ArrayList<ImageTag>();
    }
}

