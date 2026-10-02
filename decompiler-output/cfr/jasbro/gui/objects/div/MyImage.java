/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.imgscalr.Scalr$Mode
 */
package jasbro.gui.objects.div;

import jasbro.Jasbro;
import jasbro.gui.GuiUtil;
import jasbro.gui.MyPanel;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageUtil;
import jasbro.gui.pictures.MyGifImageObject;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

public class MyImage
extends MyPanel {
    private static final long serialVersionUID = -7099838193972833604L;
    private static final Logger log = LogManager.getLogger(MyImage.class);
    private ImageData image = null;
    private boolean resize = false;
    private boolean centered = false;
    private ImageData backgroundImage = null;
    private int insetX = 0;
    private int insetY = 0;
    private boolean grayscale = false;
    private Future<Image> backGroundImageObject;
    private Future<Image> imageObject;

    public MyImage() {
        this.addMouseListener(GuiUtil.DELEGATEMOUSELISTENER);
        this.setOpaque(false);
        this.setFocusTraversalKeysEnabled(false);
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent e) {
                MyImage.this.backGroundImageObject = null;
                MyImage.this.imageObject = null;
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                MyImage.this.backGroundImageObject = null;
                MyImage.this.imageObject = null;
            }
        });
    }

    public MyImage(ImageData image) {
        this();
        this.setImage(image);
    }

    @Override
    public void paintComponent(Graphics g) {
        Image image;
        super.paintComponent(g);
        if (this.backgroundImage != null) {
            if (this.backGroundImageObject == null || !this.backGroundImageObject.isDone()) {
                image = ImageUtil.getInstance().getImage(this.backgroundImage);
                if (this.backGroundImageObject == null && !(image instanceof MyGifImageObject)) {
                    this.queryResizeBackground();
                }
                if (image != null) {
                    if (this.grayscale && !(image instanceof MyGifImageObject)) {
                        image = ImageUtil.getInstance().convertToGrayScale((BufferedImage)image);
                    }
                    g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null, this);
                }
            } else {
                try {
                    g.drawImage(this.backGroundImageObject.get(), 0, 0, this.getWidth(), this.getHeight(), null, this);
                }
                catch (InterruptedException | ExecutionException e) {
                    log.error("Error when accessing background image object", (Throwable)e);
                }
            }
        }
        if (this.image != null) {
            image = null;
            if (this.imageObject == null || !this.imageObject.isDone()) {
                image = ImageUtil.getInstance().getImage(this.image);
                if (image != null && this.grayscale && !(image instanceof MyGifImageObject)) {
                    image = ImageUtil.getInstance().convertToGrayScale((BufferedImage)image);
                }
            } else {
                try {
                    image = this.imageObject.get();
                }
                catch (InterruptedException | ExecutionException e) {
                    log.error("Error when accessing background image object", (Throwable)e);
                }
            }
            if (image != null && this.getModifiedWidth() > 0 && this.getModifiedHeight() > 0) {
                int imageWidth = image.getWidth(this);
                int imageHeight = image.getHeight(this);
                float percw = (float)this.getModifiedWidth() / (float)imageWidth;
                float perch = (float)this.getModifiedHeight() / (float)imageHeight;
                if (!this.resize) {
                    if (percw > perch) {
                        percw = perch;
                    } else {
                        perch = percw;
                    }
                }
                int targetWidth = (int)(percw * (float)imageWidth);
                int targetHeight = (int)(perch * (float)imageHeight);
                int offsetX = (this.getWidth() - targetWidth) / 2;
                int offsetY = this.centered ? (this.getHeight() - targetHeight) / 2 : this.getHeight() - targetHeight;
                if (this.imageObject == null && !(image instanceof MyGifImageObject)) {
                    this.queryResizeImage(targetWidth, targetHeight);
                }
                g = (Graphics2D)g.create();
                g.drawImage(image, offsetX, offsetY, targetWidth, targetHeight, this);
                g.dispose();
            }
        }
    }

    public int getModifiedWidth() {
        return this.getWidth() - this.insetX * 2;
    }

    public int getModifiedHeight() {
        return this.getHeight() - this.insetY * 2;
    }

    public ImageData getImage() {
        return this.image;
    }

    public void setImage(ImageData image) {
        this.image = image;
        this.imageObject = null;
    }

    public boolean isResize() {
        return this.resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isCentered() {
        return this.centered;
    }

    public void setCentered(boolean centred) {
        this.centered = centred;
    }

    public ImageData getBackgroundImage() {
        return this.backgroundImage;
    }

    public void setBackgroundImage(ImageData backgroundImage) {
        this.backgroundImage = backgroundImage;
        this.backGroundImageObject = null;
    }

    public int getInsetX() {
        return this.insetX;
    }

    public void setInsetX(int insetX) {
        this.insetX = insetX;
    }

    public int getInsetY() {
        return this.insetY;
    }

    public void setInsetY(int insetY) {
        this.insetY = insetY;
    }

    public boolean isGrayscale() {
        return this.grayscale;
    }

    public void setGrayscale(boolean grayscale) {
        this.grayscale = grayscale;
    }

    public void queryResizeBackground() {
        final ImageData backgroundImage = this.backgroundImage;
        if (backgroundImage != null) {
            try {
                AccessController.doPrivileged(new PrivilegedAction<Void>(){

                    @Override
                    public Void run() {
                        Callable<Image> callable = new Callable<Image>(){

                            @Override
                            public Image call() throws Exception {
                                Image resizedImageObject = ImageUtil.getInstance().getImageResized(backgroundImage, MyImage.this.getWidth(), MyImage.this.getHeight(), Scalr.Mode.FIT_EXACT);
                                if (MyImage.this.grayscale) {
                                    resizedImageObject = ImageUtil.getInstance().convertToGrayScale((BufferedImage)resizedImageObject);
                                }
                                MyImage.this.repaint(20L);
                                return resizedImageObject;
                            }
                        };
                        MyImage.this.backGroundImageObject = Jasbro.getThreadpool().submit(callable);
                        return null;
                    }
                });
            }
            catch (Exception e) {
                log.error("Error when submitting background resize request", (Throwable)e);
            }
        }
    }

    public void queryResizeImage(final int width, final int height) {
        final ImageData image = this.image;
        if (image != null) {
            try {
                AccessController.doPrivileged(new PrivilegedAction<Void>(){

                    @Override
                    public Void run() {
                        Callable<Image> callable = new Callable<Image>(){

                            @Override
                            public Image call() throws Exception {
                                Image resizedImageObject = ImageUtil.getInstance().getImageResized(image, width, height, Scalr.Mode.FIT_TO_WIDTH);
                                if (MyImage.this.grayscale) {
                                    resizedImageObject = ImageUtil.getInstance().convertToGrayScale((BufferedImage)resizedImageObject);
                                }
                                MyImage.this.repaint(20L);
                                return resizedImageObject;
                            }
                        };
                        MyImage.this.imageObject = Jasbro.getThreadpool().submit(callable);
                        return null;
                    }
                });
            }
            catch (Exception e) {
                log.error("Error when submitting background resize request", (Throwable)e);
            }
        }
    }
}

