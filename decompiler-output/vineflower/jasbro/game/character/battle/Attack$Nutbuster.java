package jasbro.game.character.battle;

import jasbro.game.character.Gender;

public class Attack$Nutbuster extends Attack {
   public Attack$Nutbuster(Unit unit) {
      super(unit);
      if (unit.getGender() != Gender.MALE && unit.getGender() != Gender.FUTA) {
         this.getDamageMap().put(DamageType.REGULAR, this.getDamageMap().get(DamageType.REGULAR) * 1.05F);
         this.setAttackMessageKey("NUTBUSTER.attack.female");
      } else {
         this.getDamageMap().put(DamageType.REGULAR, this.getDamageMap().get(DamageType.REGULAR) * 5.0F);
         this.setAttackMessageKey("NUTBUSTER.attack.male");
      }
   }
}
