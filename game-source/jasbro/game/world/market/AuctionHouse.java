package jasbro.game.world.market;

import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.GameObject;
import jasbro.game.character.Charakter;
import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.character.specialization.SpecializationAttribute;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.game.events.CentralEventlistener;
import jasbro.game.events.EventType;
import jasbro.game.events.MyEvent;
import java.util.ArrayList;
import java.util.List;

public class AuctionHouse extends GameObject implements CentralEventlistener {
   private List<Charakter> slaves;
   private List<Charakter> trainers;
   private Charakter slave;
   private static String slaveJob;

   public AuctionHouse() {
      Jasbro.getInstance().addCentralListener(this);
      Jasbro.getInstance().getData().setAuctionHouse(this);
      this.slaves = this.getSlaves();
      this.trainers = this.getTrainers();
   }

   @Override
   public void handleCentralEvent(MyEvent e) {
      if (e.getType() == EventType.NEXTDAY) {
         this.slaves = null;
         this.trainers = null;
         this.getSlaves();
         this.getTrainers();
      }
   }

   public List<Charakter> getSlaves() {
      if (this.slaves == null) {
         this.slaves = new ArrayList<>();
         int amountSlaves = 4;
         if (Jasbro.getInstance().getData().getProtagonist().getTraits().contains(Trait.BENEFACTORSLAVEMARKET)) {
            amountSlaves = 8;
         }

         for (int i = 0; i < amountSlaves; i++) {
            this.slave = Jasbro.getInstance().generateBasicSlave();
            if (this.slave.getTraits().contains(Trait.UNSELLABLE)) {
               i--;
            } else {
               int day = Jasbro.getInstance().getData().getDay() + Util.getInt(0, 30);
               day /= 3;
               day++;
               int random = Util.getInt(0, 2);
               int specialization = day;
               if (specialization > 75) {
                  specialization = 75;
               }

               int basevalue = 30;
               if (day > 100 && day < 200) {
                  basevalue = 60;
               } else if (day >= 200) {
                  basevalue = 90;
               }

               int specvalue = 30;
               if (day > 100 && day < 200) {
                  specvalue = 60;
               } else if (day >= 200) {
                  specvalue = 90;
               }

               this.slave.getAttribute(BaseAttributeTypes.CHARISMA).setMaxValue(basevalue);
               this.slave.getAttribute(BaseAttributeTypes.STRENGTH).setMaxValue(basevalue);
               this.slave.getAttribute(BaseAttributeTypes.OBEDIENCE).setMaxValue(basevalue);
               this.slave.getAttribute(BaseAttributeTypes.STAMINA).setMaxValue(basevalue);
               this.slave.getAttribute(BaseAttributeTypes.INTELLIGENCE).setMaxValue(basevalue);
               this.slave.getAttribute(Sextype.VAGINAL).setMaxValue(specvalue);
               this.slave.getAttribute(Sextype.ANAL).setMaxValue(specvalue);
               this.slave.getAttribute(Sextype.ORAL).setMaxValue(specvalue);
               this.slave.getAttribute(Sextype.FOREPLAY).setMaxValue(specvalue);
               this.slave.getAttribute(Sextype.TITFUCK).setMaxValue(specvalue);
               if (this.slave.getSpecializations().contains(SpecializationType.WHORE)) {
                  this.slave.getAttribute(SpecializationAttribute.SEDUCTION).setMaxValue(specvalue);
                  this.slave.getAttribute(SpecializationAttribute.SEDUCTION).addToValue(specialization);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 2, 5, 5, 5, 7);
                     this.slave.setSlaveJob("ClassyWhore");
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization * 1.2F);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization * 1.2F);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization * 1.2F);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization * 1.2F);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization * 1.2F);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSITIVE)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.NUMB)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  } else {
                     this.changeSlaveBase(this.slave, day, 3, 5, 3, 3, 9);
                     this.slave.setSlaveJob("CumBucket");
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization * 1.4F);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization * 1.4F);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization * 1.2F);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization * 0.8F);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization * 0.8F);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 4);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 4);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 4);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 4);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 4);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSITIVE)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 4);
                     }

                     if (this.slave.getTraits().contains(Trait.NUMB)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 4);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 4);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 4);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  }
               }

               if (this.slave.getSpecializations().contains(SpecializationType.BARTENDER)) {
                  this.slave.getAttribute(SpecializationAttribute.BARTENDING).setMaxValue(specvalue);
                  this.slave.getAttribute(SpecializationAttribute.BARTENDING).addToValue(specialization);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 3, 5, 5, 6, 6);
                     this.slave.setSlaveJob("ServesDrunkards");
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 2);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 2);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 2);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 8);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 8);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 8);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 8);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 8);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 8);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  } else {
                     this.changeSlaveBase(this.slave, day, 2, 5, 5, 7, 5);
                     this.slave.setSlaveJob("ClassyBartender");
                  }
               } else if (this.slave.getSpecializations().contains(SpecializationType.DANCER)) {
                  this.slave.getAttribute(SpecializationAttribute.STRIP).setMaxValue(specvalue);
                  this.slave.getAttribute(SpecializationAttribute.STRIP).addToValue(specialization);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 3, 5, 4, 4, 7);
                     this.slave.setSlaveJob("GivesExtras");
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 2);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 2);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 3);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 2);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 2);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 7);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 7);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 7);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 7);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 7);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSITIVE)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 7);
                     }

                     if (this.slave.getTraits().contains(Trait.NUMB)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 7);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 7);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 7);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  } else {
                     this.changeSlaveBase(this.slave, day, 2, 5, 6, 5, 6);
                     this.slave.setSlaveJob("Performer");
                  }
               } else if (this.slave.getSpecializations().contains(SpecializationType.FIGHTER)) {
                  this.slave.getAttribute(SpecializationAttribute.VETERAN).setMaxValue(specvalue);
                  this.slave.getAttribute(SpecializationAttribute.VETERAN).addToValue(specialization);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 7, 2, 8, 2, 8);
                     this.slave.setSlaveJob("Brawler");
                  } else {
                     this.changeSlaveBase(this.slave, day, 7, 7, 7, 7, 2);
                     this.slave.setSlaveJob("Mage");
                  }
               } else if (this.slave.getSpecializations().contains(SpecializationType.NURSE)
                  || this.slave.getSpecializations().contains(SpecializationType.ALCHEMIST)) {
                  this.slave.getAttribute(SpecializationAttribute.MEDICALKNOWLEDGE).setMaxValue(specvalue);
                  this.slave.getAttribute(SpecializationAttribute.MAGIC).setMaxValue(specvalue);
                  this.slave.getAttribute(SpecializationAttribute.MEDICALKNOWLEDGE).addToValue(specialization);
                  this.slave.getAttribute(SpecializationAttribute.MAGIC).addToValue(specialization);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 5, 6, 4, 7, 2);
                     this.slave.setSlaveJob("BookWorm");
                  } else {
                     this.changeSlaveBase(this.slave, day, 3, 6, 2, 6, 4);
                     this.slave.setSlaveJob("DoesWhatShesAskedTo");
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization * 1.1F);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization * 1.1F);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization * 1.1F);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization * 1.1F);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization * 1.1F);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 6);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 6);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 6);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 6);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 6);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSITIVE)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 6);
                     }

                     if (this.slave.getTraits().contains(Trait.NUMB)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 6);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 6);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 6);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  }
               } else if (this.slave.getSpecializations().contains(SpecializationType.KINKYSEX)) {
                  this.slave.getAttribute(Sextype.GROUP).setMaxValue(specvalue);
                  this.slave.getAttribute(Sextype.MONSTER).setMaxValue(specvalue);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 5, 3, 1, 2, 9);
                     this.slave.setSlaveJob("FuckedByMonsters");
                     this.slave.getAttribute(Sextype.GROUP).addToValue(day / 4);
                     this.slave.getAttribute(Sextype.MONSTER).addToValue(day / 2);
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization * 1.5F);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization * 1.5F);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization * 1.5F);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  } else {
                     this.changeSlaveBase(this.slave, day, 4, 3, 2, 1, 4);
                     this.slave.setSlaveJob("FuckedByGroup");
                     this.slave.getAttribute(Sextype.GROUP).addToValue(day / 2);
                     this.slave.getAttribute(Sextype.MONSTER).addToValue(day / 4);
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization * 1.5F);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization * 1.5F);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization * 1.5F);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization * 0.8F);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization * 0.8F);
                     if (this.slave.getTraits().contains(Trait.NATURAL)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.FRIGID)) {
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSITIVE)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.NUMB)) {
                        this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.BITER)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                        this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 5);
                     }

                     if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                        this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                        this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     }
                  }
               } else if (this.slave.getSpecializations().contains(SpecializationType.MAID)) {
                  this.slave.getAttribute(SpecializationAttribute.CLEANING).setMaxValue(specialization);
                  this.slave.getAttribute(SpecializationAttribute.COOKING).setMaxValue(specialization);
                  this.slave.getAttribute(SpecializationAttribute.CLEANING).addToValue(specialization);
                  this.slave.getAttribute(SpecializationAttribute.COOKING).addToValue(specialization);
                  if (random == 1) {
                     this.changeSlaveBase(this.slave, day, 5, 3, 2, 5, 6);
                     this.slave.setSlaveJob("Cosette");
                  } else {
                     this.changeSlaveBase(this.slave, day, 3, 3, 4, 7, 3);
                     this.slave.setSlaveJob("HeadMaid");
                  }
               } else if (this.slave.getSpecializations().size() < 3
                  || this.slave.getSpecializations().contains(SpecializationType.FURRY)
                  || this.slave.getSpecializations().contains(SpecializationType.MARKETINGEXPERT)) {
                  this.changeSlaveBase(this.slave, day, 5, 5, 5, 5, 5);
                  this.slave.setSlaveJob("None");
                  this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 1.5F);
                  this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 1.5F);
                  this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 1.5F);
                  this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 1.5F);
                  this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 1.5F);
                  if (this.slave.getTraits().contains(Trait.NATURAL)) {
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 6.5F);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 6.5F);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 6.5F);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 6.5F);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(specialization / 6.5F);
                  }

                  if (this.slave.getTraits().contains(Trait.FRIGID)) {
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                     this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                     this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                     this.slave.getAttribute(Sextype.TITFUCK).addToValue(-specialization / 5);
                  }

                  if (this.slave.getTraits().contains(Trait.SENSITIVE)) {
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(specialization / 6.5F);
                  }

                  if (this.slave.getTraits().contains(Trait.NUMB)) {
                     this.slave.getAttribute(Sextype.FOREPLAY).addToValue(-specialization / 5);
                  }

                  if (this.slave.getTraits().contains(Trait.BITER)) {
                     this.slave.getAttribute(Sextype.ORAL).addToValue(-specialization / 5);
                  }

                  if (this.slave.getTraits().contains(Trait.SENSUALTONGUE)) {
                     this.slave.getAttribute(Sextype.ORAL).addToValue(specialization / 6.5F);
                  }

                  if (this.slave.getTraits().contains(Trait.AMBITOUSLOVER)) {
                     this.slave.getAttribute(Sextype.ANAL).addToValue(specialization / 6.5F);
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(specialization / 6.5F);
                  }

                  if (this.slave.getTraits().contains(Trait.DEADFISH)) {
                     this.slave.getAttribute(Sextype.ANAL).addToValue(-specialization / 5);
                     this.slave.getAttribute(Sextype.VAGINAL).addToValue(-specialization / 5);
                  }
               }

               this.slaves.add(this.slave);
            }
         }
      }

      return this.slaves;
   }

   public void setSlaves(List<Charakter> slaves) {
      this.slaves = slaves;
   }

   public List<Charakter> getTrainers() {
      if (this.trainers == null) {
         this.trainers = new ArrayList<>();
         int amountTrainers = Math.min(10, Math.max(3, (int)Math.sqrt(Jasbro.getInstance().getData().getProtagonist().getFame().getFame()) / 50));

         for (int i = 0; i < amountTrainers; i++) {
            this.trainers.add(Jasbro.getInstance().generateBasicTrainer());
         }
      }

      return this.trainers;
   }

   public void setTrainers(List<Charakter> trainers) {
      this.trainers = trainers;
   }

   private void changeSlaveBase(Charakter slave, int day, int charisma, int strength, int obedience, int stamina, int intelligence) {
      if (slave.getTraits().contains(Trait.UGLY)) {
         charisma++;
      }

      if (slave.getTraits().contains(Trait.LOVELY)) {
         charisma--;
      }

      if (slave.getTraits().contains(Trait.OBEDIENT)) {
         obedience--;
      }

      if (slave.getTraits().contains(Trait.FEISTY)) {
         obedience++;
      }

      if (slave.getTraits().contains(Trait.FIT)) {
         stamina--;
      }

      if (slave.getTraits().contains(Trait.UNFIT)) {
         stamina++;
      }

      if (slave.getTraits().contains(Trait.CLEVER)) {
         intelligence--;
      }

      if (slave.getTraits().contains(Trait.STUPID)) {
         intelligence++;
      }

      if (slave.getTraits().contains(Trait.STRONG)) {
         strength--;
      }

      if (slave.getTraits().contains(Trait.WEAK)) {
         strength++;
      }

      if (charisma == 0) {
         charisma = 1;
      }

      if (strength == 0) {
         strength = 1;
      }

      if (stamina == 0) {
         stamina = 1;
      }

      if (obedience == 0) {
         obedience = 1;
      }

      if (intelligence == 0) {
         intelligence = 1;
      }

      slave.getAttribute(BaseAttributeTypes.CHARISMA).addToValue(day / charisma);
      slave.getAttribute(BaseAttributeTypes.STRENGTH).addToValue(day / strength);
      slave.getAttribute(BaseAttributeTypes.OBEDIENCE).addToValue(day / obedience);
      slave.getAttribute(BaseAttributeTypes.STAMINA).addToValue(day / stamina);
      slave.getAttribute(BaseAttributeTypes.INTELLIGENCE).addToValue(day / intelligence);
   }

   public static String getSlaveJob() {
      return slaveJob;
   }
}
