package jasbro.game.character.activities;

import jasbro.game.world.customContent.CustomQuest;

public class ActivityDetails {
   private ActivityType activityType;
   private CustomQuest quest;
   private String eventId;
   private String label;
   private String title;

   public ActivityDetails() {
   }

   public ActivityDetails(ActivityType activityType) {
      this.activityType = activityType;
   }

   public ActivityDetails(ActivityType activityType, String eventId) {
      this.activityType = activityType;
      this.eventId = eventId;
   }

   public ActivityDetails(ActivityType activityType, String eventId, String label) {
      this.activityType = activityType;
      this.eventId = eventId;
      this.label = label;
   }

   public ActivityDetails(ActivityType activityType, String eventId, String label, String title) {
      this.activityType = activityType;
      this.eventId = eventId;
      this.label = label;
      this.title = title;
   }

   public ActivityDetails(ActivityType activityType, CustomQuest quest, String eventId, String label, String title) {
      this.activityType = activityType;
      this.quest = quest;
      this.eventId = eventId;
      this.label = label;
      this.title = title;
   }

   public String getText() {
      return this.label != null ? this.label : this.activityType.getText();
   }

   public String getDescription() {
      return this.title != null ? this.title : this.activityType.getDescription();
   }

   public ActivityType getActivityType() {
      return this.activityType;
   }

   public void setActivityType(ActivityType activityType) {
      this.activityType = activityType;
   }

   public String getEventId() {
      return this.eventId;
   }

   public void setEventId(String eventId) {
      this.eventId = eventId;
   }

   public String getLabel() {
      return this.label;
   }

   public void setLabel(String label) {
      this.label = label;
   }

   public String getTitle() {
      return this.title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public CustomQuest getQuest() {
      return this.quest;
   }

   public void setQuest(CustomQuest quest) {
      this.quest = quest;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      result = 31 * result + (this.activityType == null ? 0 : this.activityType.hashCode());
      return 31 * result + (this.eventId == null ? 0 : this.eventId.hashCode());
   }

   @Override
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

      ActivityDetails other = (ActivityDetails)obj;
      if (this.activityType != other.activityType) {
         return false;
      }

      if (this.eventId == null) {
         if (other.eventId != null) {
            return false;
         }
      } else if (!this.eventId.equals(other.eventId)) {
         return false;
      }

      return true;
   }
}
