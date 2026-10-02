package jasbro.game.world.customContent.npc;

import jasbro.game.character.Gender;
import jasbro.game.character.attributes.CalculatedAttribute;
import jasbro.game.character.battle.Attack;
import java.util.ArrayList;
import java.util.List;
import net.java.truevfs.access.TFile;

public class ComplexEnemyTemplate extends ComplexEnemy {
   private transient String id;
   private transient TFile file;
   private String rape;
   private String femSubmit;
   private String femRape;
   private String reverseFemRape;
   private String reverseFemRapeCapture;
   private String maleSubmit;
   private String maleRape;
   private String reverseMaleRape;
   private String reverseMaleRapeCapture;
   private String encounter;
   private String summoning;
   private String capture;
   private boolean customerMonster;
   private List<EnemySpawnData> spawnDataList = new ArrayList<>();
   private List<Attack> attackDataList = new ArrayList<>();

   public ComplexEnemyTemplate() {
   }

   public ComplexEnemyTemplate(String id) {
      this.id = id;
      this.setHitpoints(100);
      this.setGender(Gender.MALE);
      this.setAttribute(CalculatedAttribute.DAMAGE, 1.0);
      this.setAttribute(CalculatedAttribute.ARMORPERCENT, 5.0);
      this.setAttribute(CalculatedAttribute.DODGE, 5.0);
      this.setAttribute(CalculatedAttribute.CRITCHANCE, 5.0);
      this.setAttribute(CalculatedAttribute.CRITDAMAGEAMOUNT, 25.0);
   }

   public ComplexEnemy generateEnemy() {
      ComplexEnemy complexEnemy = new ComplexEnemy();
      complexEnemy.setName(this.getName());
      complexEnemy.setGender(this.getGender());
      complexEnemy.setDick(this.getDick());
      complexEnemy.setHitpoints(this.getHitpoints());
      complexEnemy.setImages(this.getImages());
      complexEnemy.setDescription(this.getDescription());
      complexEnemy.setCharacterBaseId(this.getCharacterBaseId());
      complexEnemy.setFemaleRape(this.getFemaleRape());
      complexEnemy.setMaleRape(this.getMaleRape());
      complexEnemy.setFemaleSubmit(this.getFemaleSubmit());
      complexEnemy.setMaleSubmit(this.getMaleSubmit());
      complexEnemy.setReverseFemaleRape(this.getReverseFemaleRape());
      complexEnemy.setReverseMaleRape(this.getReverseMaleRape());

      for (CalculatedAttribute attribute : CalculatedAttribute.values()) {
         double value = this.getAttribute(attribute);
         if (value != 0.0) {
            complexEnemy.setAttribute(attribute, value);
         }
      }

      return complexEnemy;
   }

   public String getId() {
      return this.id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public TFile getFile() {
      return this.file;
   }

   public void setFile(TFile file) {
      this.file = file;
   }

   public String getTextRape() {
      return this.rape;
   }

   public void setTextRape(String text) {
      this.rape = text;
   }

   @Override
   public String getFemaleSubmit() {
      return this.femSubmit;
   }

   @Override
   public void setFemaleSubmit(String text) {
      this.femSubmit = text;
   }

   @Override
   public String getFemaleRape() {
      return this.femRape;
   }

   @Override
   public void setFemaleRape(String text) {
      this.femRape = text;
   }

   @Override
   public String getReverseFemaleRape() {
      return this.reverseFemRape;
   }

   @Override
   public void setReverseFemaleRape(String text) {
      this.reverseFemRape = text;
   }

   public String getReverseFemaleRapeCapture() {
      return this.reverseFemRapeCapture;
   }

   public void setReverseFemaleRapeCapture(String text) {
      this.reverseFemRapeCapture = text;
   }

   @Override
   public String getMaleSubmit() {
      return this.maleSubmit;
   }

   @Override
   public void setMaleSubmit(String text) {
      this.maleSubmit = text;
   }

   @Override
   public String getMaleRape() {
      return this.maleRape;
   }

   @Override
   public void setMaleRape(String text) {
      this.maleRape = text;
   }

   @Override
   public String getReverseMaleRape() {
      return this.reverseMaleRape;
   }

   @Override
   public void setReverseMaleRape(String text) {
      this.reverseMaleRape = text;
   }

   public String getReverseMaleRapeCapture() {
      return this.reverseMaleRapeCapture;
   }

   public void setReverseMaleRapeCapture(String text) {
      this.reverseMaleRapeCapture = text;
   }

   public String getTextCapture() {
      return this.capture;
   }

   public void setTextCapture(String text) {
      this.capture = text;
   }

   public boolean setCustomerMonster(boolean customerMonster) {
      this.customerMonster = customerMonster;
      return customerMonster;
   }

   public boolean isCustomerMonster() {
      return this.customerMonster;
   }

   @Override
   public String toString() {
      return this.id;
   }

   public List<EnemySpawnData> getSpawnDataList() {
      return this.spawnDataList;
   }

   public void setSpawnDataList(List<EnemySpawnData> spawnDataList) {
      this.spawnDataList = spawnDataList;
   }

   public List<Attack> getAttackDataList() {
      return this.attackDataList;
   }

   public void setAttackDataList(List<Attack> attackDataList) {
      this.attackDataList = attackDataList;
   }

   public String getTextEncounter() {
      return this.encounter;
   }

   public void setTextEncounter(String encounter) {
      this.encounter = encounter;
   }

   public String getTextSummoning() {
      return this.summoning;
   }

   public void setTextSummoning(String summoning) {
      this.summoning = summoning;
   }
}
