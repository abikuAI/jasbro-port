package jasbro.game.character.activities;

import jasbro.Jasbro;
import jasbro.game.character.Charakter;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import jasbro.game.world.CharacterLocation;
import jasbro.game.world.customContent.CustomQuest;
import jasbro.gui.pages.SelectionData;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlannedActivity implements Serializable {
   private ActivityType type;
   private List<Charakter> characters = new ArrayList<>();
   private CharacterLocation source;
   private SelectionData<?> selectedOption = null;
   private String eventId;
   private CustomQuest quest;

   public PlannedActivity() {
   }

   public PlannedActivity(CharacterLocation location, ActivityType activity) {
      this.source = location;
      this.type = activity;
   }

   public PlannedActivity(PlannedActivity plannedActivity) {
      this.source = plannedActivity.source;
      this.type = plannedActivity.type;
      this.characters.addAll(plannedActivity.getCharacters());
      this.selectedOption = plannedActivity.selectedOption;
   }

   public PlannedActivity(ActivityType newType, PlannedActivity plannedActivity, Charakter... charakters) {
      this.source = plannedActivity.source;
      this.type = newType;
      this.selectedOption = plannedActivity.selectedOption;

      for (Charakter character : charakters) {
         this.characters.add(character);
      }
   }

   public PlannedActivity(PlannedActivity plannedActivity, Charakter... charakters) {
      this.source = plannedActivity.source;
      this.type = plannedActivity.type;
      this.selectedOption = plannedActivity.selectedOption;

      for (Charakter character : charakters) {
         this.characters.add(character);
      }
   }

   public ActivityType getType() {
      if (this.type == null) {
         this.checkPossibleActivities();
      }

      return this.type;
   }

   public void setType(ActivityType type) {
      if (type != this.type && type != ActivityType.CUSTOMEVENT) {
         this.type = type;
         this.eventId = null;
         this.quest = null;
         if (this.source != null) {
            MyEvent e = new MyEvent(EventType.ACTIVITYCHANGE, this);

            for (Charakter character : this.getCharacters()) {
               character.handleEvent(e);
            }

            this.source.fireEvent(e);
         }
      }
   }

   public List<Charakter> getCharacters() {
      return this.characters;
   }

   public void setCharacters(List<Charakter> characters) {
      this.characters = characters;
   }

   public CharacterLocation getSource() {
      return this.source;
   }

   public void setSource(CharacterLocation location) {
      this.source = location;
   }

   public void removeCharacter(Charakter charakter) {
      this.characters.remove(charakter);
      charakter.removeActivity(this);
      this.checkPossibleActivities();
      MyEvent e = new MyEvent(EventType.ACTIVITYCHANGE, this);
      charakter.handleEvent(e);
      this.source.fireEvent(e);
   }

   public void removeAllCharacters() {
      while (this.getCharacters().size() > 0) {
         this.removeCharacter(this.getCharacters().get(0));
      }
   }

   public boolean add(Charakter character) {
      if (this.getCharacters().size() < this.source.getMaxPeople()) {
         this.characters.add(character);
         character.setActivity(this);
         this.checkPossibleActivities();
         return true;
      } else {
         return false;
      }
   }

   public RunningActivity getRunningActivity() {
      RunningActivity runningActivity = this.type.getActivity();
      runningActivity.setPlannedActivity(this);
      return runningActivity;
   }

   public void checkPossibleActivities() {
      if (this.source != null && this.source.getCurrentUsage() == this && !Jasbro.getInstance().getData().getEventManager().isShiftInProgress()) {
         List<ActivityDetails> possibleActivities = this.source.getPossibleActivities();
         if (!possibleActivities.contains(this.getActivityDetails()) && this.type != ActivityType.EVENT) {
            this.setActivityDetails(possibleActivities.get(0));
         }
      }
   }

   public void setActivityDetails(ActivityDetails activityDetails) {
      if (activityDetails == null) {
         this.eventId = null;
         this.quest = null;
         this.type = null;
      } else if (!activityDetails.equals(this.getActivityDetails())) {
         this.eventId = activityDetails.getEventId();
         this.quest = activityDetails.getQuest();
         this.type = activityDetails.getActivityType();
         if (this.source != null) {
            MyEvent e = new MyEvent(EventType.ACTIVITYCHANGE, this);

            for (Charakter character : this.getCharacters()) {
               character.handleEvent(e);
            }

            this.source.fireEvent(e);
         }
      }
   }

   public ActivityDetails getActivityDetails() {
      return new ActivityDetails(this.type, this.eventId);
   }

   public SelectionData<?> getSelectedOption() {
      List<SelectionData<?>> selectionDataList = this.getType().getSelectionOptions(this);
      if (this.selectedOption != null && selectionDataList != null && selectionDataList.contains(this.selectedOption)) {
         return this.selectedOption;
      }

      this.selectedOption = null;
      return null;
   }

   public void setSelectedOption(SelectionData<?> selectedOption) {
      this.selectedOption = selectedOption;
   }

   public String getEventId() {
      return this.eventId;
   }

   public void setEventId(String eventId) {
      this.eventId = eventId;
   }

   public CustomQuest getQuest() {
      return this.quest;
   }

   public void setQuest(CustomQuest quest) {
      this.quest = quest;
   }
}
