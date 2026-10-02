package jasbro.game;

import jasbro.game.character.Gender;
import jasbro.game.events.business.AllowedServices;

public class DefaultPreferences {
   private AllowedServices allowedServicesMale;
   private AllowedServices allowedServicesFemale = new AllowedServices();
   private AllowedServices allowedServicesFuta;

   public DefaultPreferences() {
      this.allowedServicesMale = new AllowedServices();
      this.allowedServicesMale.setServiceMales(false);
      this.allowedServicesMale.setServiceFutas(false);
      this.allowedServicesFuta = new AllowedServices();
   }

   public AllowedServices getAllowedServicesMale() {
      return this.allowedServicesMale;
   }

   public void setAllowedServicesMale(AllowedServices allowedServicesMale) {
      this.allowedServicesMale = allowedServicesMale;
   }

   public AllowedServices getAllowedServicesFemale() {
      return this.allowedServicesFemale;
   }

   public void setAllowedServicesFemale(AllowedServices allowedServicesFemale) {
      this.allowedServicesFemale = allowedServicesFemale;
   }

   public AllowedServices getAllowedServicesFuta() {
      return this.allowedServicesFuta;
   }

   public void setAllowedServicesFuta(AllowedServices allowedServicesFuta) {
      this.allowedServicesFuta = allowedServicesFuta;
   }

   public AllowedServices getAllowedServices(Gender gender) {
      if (gender == Gender.FUTA) {
         return this.allowedServicesFuta;
      } else {
         return gender == Gender.MALE ? this.allowedServicesMale : this.allowedServicesFemale;
      }
   }

   public void setAllowedServices(Gender gender, AllowedServices allowedServices) {
      if (gender == Gender.FUTA) {
         this.allowedServicesFuta = new AllowedServices(allowedServices);
      } else if (gender == Gender.MALE) {
         this.allowedServicesMale = new AllowedServices(allowedServices);
      } else {
         this.allowedServicesFemale = new AllowedServices(allowedServices);
      }
   }
}
