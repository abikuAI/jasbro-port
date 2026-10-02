package jasbro.gui.pictures;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.SwingUtilities;

public class MyGifImageObject extends BufferedImage {
   private List<ImageFrame> imageFrames = new ArrayList<>();
   private int currentImage = 0;
   private Set<ImageObserver> observers = new HashSet<>();
   private boolean active = false;
   private long lastActive = 0L;
   private MyGifImageObject.RenderThread renderThread;

   public MyGifImageObject(List<ImageFrame> imageFrames) {
      super(imageFrames.get(0).getImage().getWidth(), imageFrames.get(0).getImage().getHeight(), 3);
      this.imageFrames = imageFrames;
      this.update();
   }

   public synchronized void update() {
      Graphics g = access$501(this);
      g.clearRect(0, 0, this.getCurrentImage().getWidth(), this.getCurrentImage().getHeight());
      g.drawImage(this.getCurrentImage(), 0, 0, this.getCurrentImage().getWidth(), this.getCurrentImage().getHeight(), null);
      g.dispose();
   }

   public void setActive() {
      this.active = true;
      this.lastActive = System.currentTimeMillis();
      if (this.renderThread == null || !this.renderThread.isAlive()) {
         AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
               MyGifImageObject.this.renderThread = MyGifImageObject.this.new RenderThread();
               MyGifImageObject.this.renderThread.start();
               return null;
            }
         });
      }
   }

   public BufferedImage getCurrentImage() {
      return this.imageFrames.get(this.currentImage).getImage();
   }

   @Override
   public int getWidth(ImageObserver observer) {
      if (observer != null) {
         this.observers.add(observer);
      }

      this.setActive();
      return super.getWidth(observer);
   }

   @Override
   public int getHeight(ImageObserver observer) {
      if (observer != null) {
         this.observers.add(observer);
      }

      this.setActive();
      return super.getHeight(observer);
   }

   public List<ImageFrame> getImageFrames() {
      return this.imageFrames;
   }

   private class RenderThread extends Thread {
      private RenderThread() {
      }

      @Override
      public void run() {
         MyGifImageObject.this.lastActive = System.currentTimeMillis();
         long timeDiff = 0L;

         do {
            try {
               if (!MyGifImageObject.this.observers.isEmpty() && MyGifImageObject.this.active && MyGifImageObject.this.imageFrames.size() > 1) {
                  int delay = MyGifImageObject.this.imageFrames.get(MyGifImageObject.this.currentImage).getDelay();
                  if (delay <= 0) {
                     delay = 5;
                  }

                  Thread.sleep(delay * 10);
                  MyGifImageObject.this.currentImage++;
                  if (MyGifImageObject.this.currentImage >= MyGifImageObject.this.imageFrames.size()) {
                     MyGifImageObject.this.currentImage = 0;
                  }

                  SwingUtilities.invokeLater(
                     new Runnable() {
                        @Override
                        public void run() {
                           MyGifImageObject.this.update();

                           for (ImageObserver observer : MyGifImageObject.this.observers) {
                              observer.imageUpdate(
                                 MyGifImageObject.this.getCurrentImage(),
                                 32,
                                 0,
                                 0,
                                 MyGifImageObject.this.getCurrentImage().getWidth(),
                                 MyGifImageObject.this.getCurrentImage().getHeight()
                              );
                           }
                        }
                     }
                  );
               } else {
                  Thread.sleep(500L);
               }
            } catch (Exception e) {
               e.printStackTrace();
            }

            timeDiff = System.currentTimeMillis() - MyGifImageObject.this.lastActive;
            if (timeDiff > 1000L) {
               MyGifImageObject.this.active = false;
            }
         } while (timeDiff < 10000L);
      }
   }
}
