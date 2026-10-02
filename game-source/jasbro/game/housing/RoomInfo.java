package jasbro.game.housing;

import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.activities.ActivityType;
import jasbro.game.character.activities.requirements.ActivityRequirement;
import jasbro.game.events.rooms.RoomEventHandler;
import jasbro.gui.pictures.ImageData;
import jasbro.texts.TextUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RoomInfo {
   private final int maxOccupancy;
   private final int cost;
   private final String id;
   private final ImageData image;
   private RoomEventHandler eventHandler;
   private Set<RoomSlotType> slotTypes = EnumSet.noneOf(RoomSlotType.class);
   private Map<ActivityType, ActivityRequirement> activityRequirements;
   private List<ActivityType> activities;
   private Map<ActivityType, ActivityRequirement> childCareActivityRequirements;

   public RoomInfo(int maxOccupancy, int cost, String id, String imageLocation) {
      this.maxOccupancy = maxOccupancy;
      this.cost = cost;
      this.id = id;
      this.image = new ImageData(imageLocation);
      this.activityRequirements = new EnumMap<>(ActivityType.class);
      this.activities = new ArrayList<>();
      this.childCareActivityRequirements = new EnumMap<>(ActivityType.class);
   }

   public void addActivity(ActivityType activity, ActivityRequirement requirement) {
      this.activityRequirements.put(activity, requirement);
      if (!this.activities.contains(activity)) {
         this.activities.add(activity);
      }
   }

   public void addChildCareActivity(ActivityType activity, ActivityRequirement requirement) {
      this.childCareActivityRequirements.put(activity, requirement);
   }

   public void addSlotType(RoomSlotType slotType) {
      this.slotTypes.add(slotType);
   }

   public Set<RoomSlotType> getSlotTypes() {
      return this.slotTypes;
   }

   public boolean fitsInSlot(RoomSlotType slot) {
      return this.slotTypes.contains(slot);
   }

   public void setEventHandler(RoomEventHandler eventHandler) {
      this.eventHandler = eventHandler;
   }

   public int getMaxOccupancy() {
      return this.maxOccupancy;
   }

   public int getCost() {
      return this.cost;
   }

   public String getId() {
      return this.id;
   }

   public ImageData getImage() {
      return this.image;
   }

   public List<ActivityType> getActivities() {
      return this.activities;
   }

   public Collection<ActivityType> getChildCareActivities() {
      return this.childCareActivityRequirements.keySet();
   }

   public boolean isActivityValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      return this.activityRequirements.get(activity).isValid(activity, characters, typeAmounts);
   }

   public boolean isChildCareActivityValid(ActivityType activity, List<Charakter> characters, Util.TypeAmounts typeAmounts) {
      return this.childCareActivityRequirements.get(activity).isValid(activity, characters, typeAmounts);
   }

   public boolean hasEventHandler() {
      return this.eventHandler != null;
   }

   public RoomEventHandler getEventHandler() {
      return this.eventHandler;
   }

   public String getText() {
      return TextUtil.t(this.id);
   }

   public String getDescription() {
      return TextUtil.tNoCheck(this.id + ".description");
   }
}
