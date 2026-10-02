package jasbro.game.world.customContent;

import bsh.EvalError;
import jasbro.game.events.EventType;
import jasbro.game.world.customContent.requirements.AndRequirement;
import jasbro.game.world.customContent.requirements.TriggerRequirement;

public class Trigger {
   private TriggerRequirement requirement = new AndRequirement();
   private Trigger.TriggerType triggerType = Trigger.TriggerType.ACTIVITYPERFORMED;
   private String activityLabel;
   private String activityDescription;

   public boolean isTriggered(TriggerParent triggerParent) throws EvalError {
      if (this.triggerType == Trigger.TriggerType.CUSTOMACTIVITY && triggerParent.getEvent() == null) {
         return this.requirement == null ? true : this.requirement.isValid(triggerParent);
      } else if (triggerParent.getEvent() == null || this.triggerType.getEventType() != triggerParent.getEvent().getType()) {
         return false;
      } else {
         return this.requirement == null ? true : this.requirement.isValid(triggerParent);
      }
   }

   public Trigger.TriggerType getTriggerType() {
      return this.triggerType;
   }

   public void setTriggerType(Trigger.TriggerType triggerType) {
      this.triggerType = triggerType;
   }

   public TriggerRequirement getRequirement() {
      return this.requirement;
   }

   public void setRequirement(TriggerRequirement requirement) {
      this.requirement = requirement;
   }

   public String getActivityLabel() {
      return this.activityLabel;
   }

   public void setActivityLabel(String activityLabel) {
      this.activityLabel = activityLabel;
   }

   public String getActivityDescription() {
      return this.activityDescription;
   }

   public void setActivityDescription(String activityDescription) {
      this.activityDescription = activityDescription;
   }

   public enum TriggerType {
      NEXTSHIFT(EventType.NEXTSHIFT),
      NEXTDAY(EventType.NEXTDAY),
      ACTIVITY(EventType.ACTIVITY),
      ACTIVITYPERFORMED(EventType.ACTIVITYPERFORMED),
      ACTIVITYFINISHED(EventType.ACTIVITYFINISHED),
      ACTIVITYCREATED(EventType.ACTIVITYCREATED),
      SHIFTSTART(EventType.SHIFTSTART),
      CUSTOMERSARRIVE(EventType.CUSTOMERSARRIVE),
      GAMESTART(EventType.GAMESTART),
      CHARACTERGAINED(EventType.CHARACTERGAINED),
      CHARACTERLOST(EventType.CHARACTERLOST),
      CUSTOMACTIVITY;

      private EventType eventType;

      TriggerType() {
      }

      TriggerType(EventType eventType) {
         this.eventType = eventType;
      }

      public EventType getEventType() {
         return this.eventType;
      }
   }
}
