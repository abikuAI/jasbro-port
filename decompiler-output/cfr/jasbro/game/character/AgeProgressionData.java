/*
 * Decompiled with CFR 0.152.
 */
package jasbro.game.character;

import jasbro.game.character.Charakter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AgeProgressionData {
    private String infantBase;
    private String childBase;
    private String teenagerBase;
    private String sonDaughterBase;
    private List<String> adultBases = new ArrayList<String>();
    private String nameMother;
    private String nameFather;
    private WeakReference<Charakter> mother;
    private WeakReference<Charakter> father;

    public AgeProgressionData() {
    }

    public AgeProgressionData(AgeProgressionData ageProgressionData) {
        this.infantBase = ageProgressionData.infantBase;
        this.childBase = ageProgressionData.childBase;
        this.teenagerBase = ageProgressionData.teenagerBase;
        this.sonDaughterBase = ageProgressionData.sonDaughterBase;
        this.adultBases.addAll(ageProgressionData.adultBases);
    }

    public String getInfantBase() {
        return this.infantBase;
    }

    public void setInfantBase(String infantBase) {
        this.infantBase = infantBase;
    }

    public String getChildBase() {
        return this.childBase;
    }

    public void setChildBase(String childBase) {
        this.childBase = childBase;
    }

    public String getTeenagerBase() {
        return this.teenagerBase;
    }

    public void setTeenagerBase(String teenBase) {
        this.teenagerBase = teenBase;
    }

    public List<String> getAdultBases() {
        return this.adultBases;
    }

    public void setAdultBases(List<String> adultBases) {
        this.adultBases = adultBases;
    }

    public String getSonDaughterBase() {
        return this.sonDaughterBase;
    }

    public void setSonDaughterBase(String sunDaughterBase) {
        this.sonDaughterBase = sunDaughterBase;
    }

    public String getNameMother() {
        return this.nameMother;
    }

    public void setNameMother(String nameMother) {
        this.nameMother = nameMother;
    }

    public String getNameFather() {
        return this.nameFather;
    }

    public void setNameFather(String nameFather) {
        this.nameFather = nameFather;
    }

    public WeakReference<Charakter> getMother() {
        return this.mother;
    }

    public void setMother(WeakReference<Charakter> mother) {
        this.mother = mother;
    }

    public WeakReference<Charakter> getFather() {
        return this.father;
    }

    public void setFather(WeakReference<Charakter> father) {
        this.father = father;
    }
}

