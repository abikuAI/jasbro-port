package jasbro.gui.pages;

public class SelectionData<T> {
   private T selectionObject;
   private String buttonText;
   private String tooltipText;
   private String shortText;
   private boolean enabled = true;

   public SelectionData() {
   }

   public SelectionData(T selectionObject, String buttonText) {
      this.selectionObject = selectionObject;
      this.buttonText = buttonText;
   }

   public SelectionData(T selectionObject, String buttonText, String tooltipText) {
      this.selectionObject = selectionObject;
      this.buttonText = buttonText;
      this.tooltipText = tooltipText;
   }

   public SelectionData(T selectionObject, String buttonText, boolean enabled) {
      this.selectionObject = selectionObject;
      this.buttonText = buttonText;
      this.enabled = enabled;
   }

   public SelectionData(T selectionObject, String buttonText, String tooltipText, boolean enabled) {
      this.selectionObject = selectionObject;
      this.buttonText = buttonText;
      this.tooltipText = tooltipText;
      this.enabled = enabled;
   }

   public T getSelectionObject() {
      return this.selectionObject;
   }

   public void setSelectionObject(T selectionObject) {
      this.selectionObject = selectionObject;
   }

   public String getButtonText() {
      return this.buttonText;
   }

   public void setButtonText(String buttonText) {
      this.buttonText = buttonText;
   }

   public String getTooltipText() {
      return this.tooltipText;
   }

   public void setTooltipText(String tooltipText) {
      this.tooltipText = tooltipText;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   @Override
   public int hashCode() {
      int prime = 31;
      int result = 1;
      return 31 * result + (this.selectionObject == null ? 0 : this.selectionObject.hashCode());
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

      SelectionData<?> other = (SelectionData<?>)obj;
      if (this.selectionObject == null) {
         if (other.selectionObject != null) {
            return false;
         }
      } else if (!this.selectionObject.equals(other.selectionObject)) {
         return false;
      }

      return true;
   }

   public String getShortText() {
      return this.shortText == null ? this.buttonText : this.shortText;
   }

   public void setShortText(String shortText) {
      this.shortText = shortText;
   }
}
