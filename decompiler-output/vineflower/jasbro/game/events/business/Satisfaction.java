package jasbro.game.events.business;

import jasbro.texts.TextUtil;

public enum Satisfaction {
   OUTRAGED(0, 1, -3.0F),
   FURIOUS(3, 2, -2.5F),
   BITTER(6, 5, -2.0F),
   ANGRY(9, 7, -1.5F),
   FRUSTRATED(12, 10, -1.0F),
   DISGRUNTLED(15, 12, -0.5F),
   GRUMPY(18, 20, -0.1F),
   DISAPPOINTED(21, 30, -0.05F),
   BORED(24, 60, 0.05F),
   UNSATISFIED(27, 80, 0.1F),
   INDIFFERENT(30, 100, 0.2F),
   SATISFIED(35, 120, 0.4F),
   CONTENT(40, 140, 0.5F),
   PLEASED(45, 160, 0.6F),
   CHEERFUL(50, 180, 0.8F),
   HAPPY(55, 200, 1.0F),
   EXCITED(60, 250, 2.0F),
   ECSTATIC(70, 400, 3.0F),
   EXHILARATED(80, 500, 4.0F),
   OVERJOYED(90, 1000, 10.0F),
   ENTHRALLED(100, 2000, 15.0F);

   private int moneyModifierPercent;
   private float fameModifier;
   private int minSatisfaction;

   Satisfaction(int minSatisfaction, int moneyModifierPercent, float fameModifier) {
      this.minSatisfaction = minSatisfaction;
      this.moneyModifierPercent = moneyModifierPercent;
      this.fameModifier = fameModifier;
   }

   public String getText() {
      return TextUtil.t(this.toString());
   }

   public int getMoneyModifierPercent() {
      return this.moneyModifierPercent;
   }

   public static Satisfaction getSatisfaction(int satisfactionValue) {
      Satisfaction previousValue = null;

      for (Satisfaction satisfaction : values()) {
         if (previousValue != null && satisfaction.getMinSatisfaction() > satisfactionValue) {
            return previousValue;
         }

         previousValue = satisfaction;
      }

      return ENTHRALLED;
   }

   public float getFameModifier() {
      return this.fameModifier;
   }

   public int getMinSatisfaction() {
      return this.minSatisfaction;
   }
}
