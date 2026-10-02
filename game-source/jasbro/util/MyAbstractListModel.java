package jasbro.util;

import javax.swing.AbstractListModel;

public abstract class MyAbstractListModel extends AbstractListModel {
   public void update(Object source, int index0, int index1) {
      this.fireContentsChanged(source, index0, index1);
   }
}
