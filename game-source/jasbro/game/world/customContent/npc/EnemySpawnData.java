package jasbro.game.world.customContent.npc;

public class EnemySpawnData {
   private EnemySpawnLocation enemySpawnLocation = EnemySpawnLocation.DUNGEON1;
   private int encounterChanceModifier = 100;

   public EnemySpawnLocation getEnemySpawnLocation() {
      return this.enemySpawnLocation;
   }

   public void setEnemySpawnLocation(EnemySpawnLocation enemySpawnLocation) {
      this.enemySpawnLocation = enemySpawnLocation;
   }

   public int getEncounterChanceModifier() {
      return this.encounterChanceModifier;
   }

   public void setEncounterChanceModifier(int encounterChanceModifier) {
      this.encounterChanceModifier = encounterChanceModifier;
   }
}
