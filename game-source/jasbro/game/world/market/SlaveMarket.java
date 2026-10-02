package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import java.util.ArrayList;
import java.util.List;

public class SlaveMarket extends GameObject implements CentralEventlistener {
   private List<Charakter> slaves;
   private Charakter slave;

   public SlaveMarket() {
      Jasbro.getInstance().addCentralListener(this);
      Jasbro.getInstance().getData().setSlaveMarket(this);
      this.slaves = this.getSlaves();
   }

   @Override
   public void handleCentralEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY) {
         this.slaves = null;
         this.getSlaves();
      }
   }

   public List<Charakter> getSlaves() {
      if (this.slaves == null) {
         this.slaves = new ArrayList<>();
         int amountSlaves = 3;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORSLAVEMARKET)) {
            amountSlaves = 6;
         }

         for (int i = 0; i < amountSlaves; i++) {
            this.slave = Jasbro.getInstance().generateBasicSlave();
            if (!this.slave.getTraits().contains(Trait.UNSELLABLE)
               && !this.slave.getTraits().contains(Trait.FORMERNOBLE)
               && !this.slave.getTraits().contains(Trait.RARESLAVE)
               && !this.slave.getTraits().contains(Trait.EXTREMELYRARESLAVE)
               && !this.slave.getTraits().contains(Trait.EXTREMELYRARESLAVE2)) {
               this.slave.getAttribute(BaseAttributeTypes.OBEDIENCE).addToValue(-this.slave.getObedience() + Util.getInt(0, 5));
               this.slaves.add(this.slave);
            } else {
               i--;
            }
         }
      }

      return this.slaves;
   }

   public void setSlaves(List<Charakter> slaves) {
      this.slaves = slaves;
   }
}
