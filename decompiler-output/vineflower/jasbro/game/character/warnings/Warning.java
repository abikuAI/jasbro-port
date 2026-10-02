package jasbro.game.character.warnings;

import jasbro.gui.pictures.ImageData;

public class Warning implements Comparable<Warning> {
   private Severity severity;
   private String message;

   public Warning(Severity severity, String message) {
      this.severity = severity;
      this.message = message;
   }

   public Severity getSeverity() {
      return this.severity;
   }

   public String getMessage() {
      return this.message;
   }

   public void setMessage(String message) {
      this.message = message;
   }

   public int compareTo(Warning o) {
      return this.severity.compareTo(o.severity);
   }

   public ImageData getIcon() {
      return this.severity.getIcon();
   }
}
