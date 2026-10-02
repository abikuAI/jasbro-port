/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.cache.Cache
 *  com.google.common.cache.CacheBuilder
 *  net.java.truevfs.access.TFile
 *  net.java.truevfs.access.TFileInputStream
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.imgscalr.Scalr
 *  org.imgscalr.Scalr$Method
 *  org.imgscalr.Scalr$Mode
 */
package jasbro.gui.pictures;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jasbro.Jasbro;
import jasbro.Util;
import jasbro.game.character.Charakter;
import jasbro.game.character.Gender;
import jasbro.game.character.attributes.Sextype;
import jasbro.game.events.MessageData;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.game.interfaces.Person;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageFrame;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageTagGroup;
import jasbro.gui.pictures.MyGifImageObject;
import jasbro.texts.TextUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ImageUtil
implements ImageObserver {
    private static final Logger log = LogManager.getLogger(ImageUtil.class);
    private static ImageUtil instance;
    private Cache<String, Image> images;
    private Cache<String, Image> resizedImages;

    public ImageUtil() {
        long memoryMB = Runtime.getRuntime().maxMemory() / 0x100000L;
        long cacheSize = memoryMB / 15L;
        log.info("Max-memory: {} Cache size: {}", new Object[]{memoryMB, cacheSize});
        this.images = CacheBuilder.newBuilder().maximumSize(cacheSize).expireAfterAccess(1L, TimeUnit.MINUTES).build();
        this.resizedImages = CacheBuilder.newBuilder().maximumSize(cacheSize / 4L).expireAfterAccess(30L, TimeUnit.SECONDS).build();
    }

    public static ImageUtil getInstance() {
        if (instance == null) {
            instance = new ImageUtil();
        }
        return instance;
    }

    public Image getImage(final ImageData image) {
        Image img = (Image)this.images.getIfPresent((Object)image.getKey());
        if (img == null && (img = AccessController.doPrivileged(new PrivilegedAction<Image>(){

            @Override
            public Image run() {
                try {
                    return ImageUtil.this.loadImage(image);
                }
                catch (IOException e) {
                    return null;
                }
            }
        })) == null) {
            log.error("Failed to load image");
        }
        return img;
    }

    private Image loadImage(ImageData imageData) throws IOException {
        InputStream input = null;
        try {
            Image image = null;
            image = (Image)this.images.getIfPresent((Object)imageData.getKey());
            if (image != null) {
                Image image2 = image;
                return image2;
            }
            TFile imageFile = new TFile(imageData.getKey());
            if (imageFile.exists()) {
                input = new TFileInputStream((File)imageFile);
                if (imageData.getKey().endsWith(".gif")) {
                    ImageInputStream imageInputStream = ImageIO.createImageInputStream(input);
                    ImageReader imageReader = ImageIO.getImageReaders(imageInputStream).next();
                    imageReader.setInput(imageInputStream);
                    image = new MyGifImageObject(this.readGIF(imageReader));
                    this.clearCache();
                } else {
                    image = ImageIO.read(input);
                }
                this.images.put((Object)imageData.getKey(), (Object)image);
                Image image3 = image;
                return image3;
            }
            log.error("Image not found: " + imageData.getFilename());
            Image image4 = null;
            return image4;
        }
        catch (IOException e) {
            log.error("Failed to load image for file '{}'", new Object[]{imageData.getFilename()});
            throw (IOException)log.throwing((Throwable)e);
        }
        finally {
            try {
                if (input != null) {
                    input.close();
                }
            }
            catch (IOException e) {
                log.error("Error on closing input", (Throwable)e);
            }
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return (infoflags & 0xA0) == 0;
    }

    public ImageData getImageDataByTag(ImageTag tag, HasImagesInterface character) {
        List<ImageTag> tags = character.getBaseTags();
        if (tags.size() == 0) {
            return this.getImageDataByTag(tag, character.getImages());
        }
        if (tag != ImageTag.STANDARD) {
            tags.add(0, tag);
        }
        return this.getImageDataByTags(tags, character.getImages());
    }

    public ImageData getImageDataByTag(ImageTag tag, List<ImageData> imageList) {
        List<ImageData> imagesTag;
        List<ImageData> imagePool = this.removeStrongTags(tag, imageList);
        do {
            imagesTag = this.getImageDataByTagOnly(tag, imagePool);
            tag = tag.getReplacementTag();
            if (imagesTag.size() > 0 && imagesTag.size() < 6 && Util.getInt(0, 100) < 30 - imagesTag.size() * 5 && tag == null) continue;
        } while (imagesTag.size() == 0 && tag != null);
        if (imagesTag.size() == 0) {
            if (imagePool.size() == 0) {
                imagePool = imageList;
            }
            ImageData image = imagePool.get(Util.getRnd().nextInt(imagePool.size()));
            return image;
        }
        ImageData image = imagesTag.get(Util.getRnd().nextInt(imagesTag.size()));
        return image;
    }

    public Image getImageResized(ImageData imageData, int width, int height, Scalr.Mode mode) {
        Image image = (Image)this.resizedImages.getIfPresent((Object)imageData.getKey());
        if (!this.fitsWell(image, width, height, mode)) {
            image = this.getImage(imageData);
            if (image == null) {
                return null;
            }
            image = image.getWidth(this) > width + 100 && image.getHeight(this) > height + 100 ? Scalr.resize((BufferedImage)((BufferedImage)image), (Scalr.Method)Scalr.Method.AUTOMATIC, (Scalr.Mode)mode, (int)width, (int)height, (BufferedImageOp[])new BufferedImageOp[]{Scalr.OP_ANTIALIAS}) : Scalr.resize((BufferedImage)((BufferedImage)image), (Scalr.Method)Scalr.Method.AUTOMATIC, (Scalr.Mode)mode, (int)width, (int)height, (BufferedImageOp[])new BufferedImageOp[0]);
            this.resizedImages.put((Object)imageData.getKey(), (Object)image);
        }
        return image;
    }

    public boolean fitsWell(Image image, int width, int height, Scalr.Mode mode) {
        return !(image == null || mode == Scalr.Mode.FIT_TO_WIDTH && width != image.getWidth(this) || mode == Scalr.Mode.FIT_TO_HEIGHT && height != image.getWidth(this) || mode == Scalr.Mode.FIT_EXACT && (height != image.getWidth(this) || width != image.getHeight(this))) && (mode != Scalr.Mode.AUTOMATIC || height == image.getWidth(this) || width == image.getHeight(this));
    }

    public Image getImageResizedSpeed(ImageData imageData, int width, int height, Scalr.Mode mode) {
        Image image = this.getImage(imageData);
        if (image == null) {
            return null;
        }
        image = Scalr.resize((BufferedImage)((BufferedImage)image), (Scalr.Method)Scalr.Method.SPEED, (Scalr.Mode)mode, (int)width, (int)height, (BufferedImageOp[])new BufferedImageOp[0]);
        return image;
    }

    public ImageData getImageDataByTags(final List<ImageTag> tags, final List<ImageData> imageList) {
        if (tags.size() <= 1) {
            return this.getImageDataByTag(tags.get(0), imageList);
        }
        final ImageData imageDataEmpty = new ImageData();
        final Callable<Boolean> callable = new Callable<Boolean>(){

            @Override
            public Boolean call() {
                List imageListTmp = ImageUtil.this.removeStrongTags(tags, (List<ImageData>)imageList);
                int highestValue = -10000;
                ImageData bestMatch = null;
                HashMap<ImageData, Integer> imageValueMap = new HashMap<ImageData, Integer>();
                for (ImageData imageData : imageListTmp) {
                    int randomFactor;
                    int sumValue = 0;
                    int tagValue = 0;
                    block1: for (int i = 0; i < tags.size(); ++i) {
                        ImageTag tag = (ImageTag)((Object)tags.get(i));
                        tagValue = tag.getImageTagGroup().getGroupValue();
                        tagValue = i > 2 ? (tagValue -= i) : (i == 0 ? (tagValue += 120) : (i == 1 ? (tagValue += 20) : (tagValue += 10)));
                        if (i > 0) {
                            if (tag.getImageTagGroup() == ImageTagGroup.CLOTHING && (((ImageTag)((Object)tags.get(0))).getImageTagGroup() == ImageTagGroup.SEX || tags.get(0) == ImageTag.SWIM || tags.get(0) == ImageTag.BATHE)) {
                                tagValue /= 10;
                            } else if (tags.get(0) == ImageTag.CLOTHED && tag.getImageTagGroup() == ImageTagGroup.CLOTHING) {
                                tagValue += 35;
                            }
                        }
                        if (tagValue <= 0 || tag == null) continue;
                        if (imageData.getTags().contains((Object)tag)) {
                            sumValue += tagValue;
                            continue;
                        }
                        if (tag == ImageTag.CLOTHED) {
                            if (!imageData.getTags().contains((Object)ImageTag.NAKED)) continue;
                            sumValue -= tagValue / 2;
                            continue;
                        }
                        if (tag == ImageTag.NAKED) {
                            if (!imageData.getTags().contains((Object)ImageTag.CLOTHED)) continue;
                            sumValue -= tagValue / 2;
                            continue;
                        }
                        if (tag.getImageTagGroup() == ImageTagGroup.CLOTHING) continue;
                        while (tag.getReplacementTag() != null && tagValue > 15) {
                            tagValue /= 2;
                            tagValue -= 2;
                            tag = tag.getReplacementTag();
                            if (!imageData.getTags().contains((Object)tag)) continue;
                            sumValue += tagValue;
                            continue block1;
                        }
                    }
                    if (sumValue <= 0) continue;
                    if (ImageUtil.this.images.getIfPresent((Object)imageData.getKey()) != null) {
                        sumValue -= 25;
                    }
                    if ((sumValue += (randomFactor = Util.getInt(0, 100))) > highestValue) {
                        bestMatch = imageData;
                        highestValue = sumValue;
                    }
                    if (!log.isDebugEnabled()) continue;
                    imageValueMap.put(imageData, sumValue);
                }
                if (log.isDebugEnabled()) {
                    log.debug("Image values for tags {}: {}", new Object[]{tags, imageValueMap});
                }
                if (bestMatch == null) {
                    log.debug("No fitting image found by searching for tags, use getByTag method {}", new Object[]{tags.get(0)});
                    bestMatch = ImageUtil.this.getImageDataByTag((ImageTag)((Object)tags.get(0)), imageList);
                }
                imageDataEmpty.setCurAccuracy(highestValue);
                imageDataEmpty.init(bestMatch);
                return true;
            }
        };
        AccessController.doPrivileged(new PrivilegedAction<Void>(){

            @Override
            public Void run() {
                imageDataEmpty.setFuture(Jasbro.getThreadpool().submit(callable));
                return null;
            }
        });
        return imageDataEmpty;
    }

    private List<ImageData> removeStrongTags(ImageTag allowedTag, List<ImageData> imageList) {
        ArrayList<ImageTag> allowedTags = new ArrayList<ImageTag>();
        allowedTags.add(allowedTag);
        return this.removeStrongTags(allowedTags, imageList);
    }

    private List<ImageData> removeStrongTags(List<ImageTag> allowedTags, List<ImageData> imageList) {
        ArrayList<ImageData> images = new ArrayList<ImageData>();
        for (ImageData imageData : imageList) {
            boolean accepted = true;
            for (ImageTag imageTag : imageData.getTags()) {
                try {
                    if (!imageTag.isExcludeTag() || allowedTags.contains((Object)imageTag)) continue;
                    accepted = false;
                    break;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (!accepted) continue;
            images.add(imageData);
        }
        return images;
    }

    private List<ImageData> getImageDataByTagOnly(ImageTag tag, List<ImageData> imageList) {
        ArrayList<ImageData> images = new ArrayList<ImageData>();
        for (ImageData data : imageList) {
            if (!data.hasTag(tag)) continue;
            images.add(data);
        }
        return images;
    }

    public boolean isImage(TFile file) {
        MimetypesFileTypeMap typeMap = new MimetypesFileTypeMap();
        typeMap.addMimeTypes("image png tif jpg jpeg bmp gif");
        String mimeType = typeMap.getContentType((File)file);
        return mimeType.substring(0, 5).equalsIgnoreCase("image");
    }

    public boolean isImage(Path path) {
        String contentType = "asdfdsfafsda";
        try {
            contentType = Files.probeContentType(path);
        }
        catch (IOException e) {
            log.error("Error on checking content type", (Throwable)e);
        }
        if (contentType == null) {
            return false;
        }
        return contentType.substring(0, 5).equalsIgnoreCase("image");
    }

    private List<ImageFrame> readGIF(ImageReader reader) throws IOException {
        IIOMetadataNode screenDescriptor;
        IIOMetadataNode globalRoot;
        NodeList globalScreenDescriptor;
        ArrayList<ImageFrame> frames = new ArrayList<ImageFrame>(2);
        int width = -1;
        int height = -1;
        IIOMetadata metadata = reader.getStreamMetadata();
        if (metadata != null && (globalScreenDescriptor = (globalRoot = (IIOMetadataNode)metadata.getAsTree(metadata.getNativeMetadataFormatName())).getElementsByTagName("LogicalScreenDescriptor")) != null && globalScreenDescriptor.getLength() > 0 && (screenDescriptor = (IIOMetadataNode)globalScreenDescriptor.item(0)) != null) {
            width = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenWidth"));
            height = Integer.parseInt(screenDescriptor.getAttribute("logicalScreenHeight"));
        }
        BufferedImage master = null;
        Graphics2D masterGraphics = null;
        int frameIndex = 0;
        while (true) {
            BufferedImage image;
            try {
                image = reader.read(frameIndex);
            }
            catch (IndexOutOfBoundsException io) {
                break;
            }
            if (width == -1 || height == -1) {
                width = image.getWidth();
                height = image.getHeight();
            }
            IIOMetadataNode root = (IIOMetadataNode)reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0");
            IIOMetadataNode gce = (IIOMetadataNode)root.getElementsByTagName("GraphicControlExtension").item(0);
            int delay = Integer.valueOf(gce.getAttribute("delayTime"));
            String disposal = gce.getAttribute("disposalMethod");
            int x = 0;
            int y = 0;
            if (master == null) {
                master = new BufferedImage(width, height, 2);
                masterGraphics = master.createGraphics();
                masterGraphics.setBackground(new Color(0, 0, 0, 0));
            } else {
                NodeList children = root.getChildNodes();
                for (int nodeIndex = 0; nodeIndex < children.getLength(); ++nodeIndex) {
                    Node nodeItem = children.item(nodeIndex);
                    if (!nodeItem.getNodeName().equals("ImageDescriptor")) continue;
                    NamedNodeMap map = nodeItem.getAttributes();
                    x = Integer.valueOf(map.getNamedItem("imageLeftPosition").getNodeValue());
                    y = Integer.valueOf(map.getNamedItem("imageTopPosition").getNodeValue());
                }
            }
            masterGraphics.drawImage((Image)image, x, y, null);
            BufferedImage copy = new BufferedImage(master.getColorModel(), master.copyData(null), master.isAlphaPremultiplied(), null);
            frames.add(new ImageFrame(copy, delay, disposal));
            if (disposal.equals("restoreToPrevious")) {
                BufferedImage from = null;
                for (int i = frameIndex - 1; i >= 0; --i) {
                    if (frames.get(i).getDisposal().equals("restoreToPrevious") && frameIndex != 0) continue;
                    from = frames.get(i).getImage();
                    break;
                }
                master = new BufferedImage(from.getColorModel(), from.copyData(null), from.isAlphaPremultiplied(), null);
                masterGraphics = master.createGraphics();
                masterGraphics.setBackground(new Color(0, 0, 0, 0));
            } else if (disposal.equals("restoreToBackgroundColor")) {
                masterGraphics.clearRect(x, y, image.getWidth(), image.getHeight());
            }
            ++frameIndex;
        }
        reader.dispose();
        return frames;
    }

    public boolean exists(final ImageData image) {
        if (image != null) {
            boolean result = AccessController.doPrivileged(new PrivilegedAction<Boolean>(){

                @Override
                public Boolean run() {
                    return new TFile(image.getKey()).exists();
                }
            });
            return result;
        }
        return false;
    }

    public void clearCache() {
        this.images.invalidateAll();
        this.resizedImages.invalidateAll();
    }

    public boolean tagExists(ImageTag imageTag, List<ImageData> images) {
        for (ImageData image : images) {
            if (!image.getTags().contains((Object)imageTag)) continue;
            return true;
        }
        return false;
    }

    public BufferedImage convertToGrayScale(BufferedImage original) {
        return Scalr.apply((BufferedImage)original, (BufferedImageOp[])new BufferedImageOp[]{Scalr.OP_GRAYSCALE});
    }

    public static class BestImageSelection
    implements Callable<MessageData> {
        private Charakter[] characters;
        private List<ImageTag> tags;
        private Sextype sextype;

        public BestImageSelection(List<ImageTag> tags, Charakter ... characters) {
            this.tags = tags;
            this.characters = characters;
        }

        public BestImageSelection(Sextype sextype, Charakter ... characters) {
            this.sextype = sextype;
            this.tags = new ArrayList<ImageTag>();
            this.tags.add(0, sextype.getAssociatedImageTag());
            this.characters = characters;
        }

        public BestImageSelection(Sextype sextype, List<ImageTag> tags, Charakter ... characters) {
            this.tags = tags;
            this.characters = characters;
            this.sextype = sextype;
            tags.add(sextype.getAssociatedImageTag());
        }

        @Override
        public MessageData call() throws Exception {
            MessageData messageData = new MessageData();
            ArrayList<ImageData> possibleImages = new ArrayList<ImageData>();
            for (Charakter character : this.characters) {
                ArrayList<ImageTag> tags = new ArrayList<ImageTag>(this.tags);
                ArrayList<Charakter> characters = new ArrayList<Charakter>(Arrays.asList(this.characters));
                characters.remove(character);
                characters.add(0, character);
                tags.addAll(ImageTag.getAssociatedImageTags(characters.toArray(new Charakter[characters.size()])));
                tags.addAll(character.getBaseTags());
                if (tags.contains((Object)ImageTag.FUTA)) {
                    tags.remove((Object)ImageTag.FUTA);
                    tags.add(0, ImageTag.FUTA);
                }
                if (tags.contains((Object)ImageTag.LESBIAN)) {
                    tags.remove((Object)ImageTag.LESBIAN);
                    tags.add(0, ImageTag.LESBIAN);
                }
                possibleImages.add(ImageUtil.getInstance().getImageDataByTags(tags, character.getImages()));
            }
            ImageData bestMatch = (ImageData)possibleImages.get(0);
            possibleImages.remove(bestMatch);
            for (ImageData imageData : possibleImages) {
                if (imageData.getCurAccuracy() <= bestMatch.getCurAccuracy()) continue;
                bestMatch = imageData;
            }
            messageData.setImage(bestMatch);
            messageData.setBackground(this.characters[0].getBackground());
            if (this.sextype != null && this.characters.length == 2) {
                String message;
                Charakter character1 = null;
                Charakter character2 = null;
                for (Charakter character : this.characters) {
                    if (!character.getImages().contains(bestMatch)) continue;
                    character1 = character;
                    break;
                }
                for (Charakter character : this.characters) {
                    if (character1 == character) continue;
                    character2 = character;
                }
                if (character1.getGender() != Gender.MALE && character2.getGender() == Gender.MALE) {
                    Charakter charTmp = character1;
                    character1 = character2;
                    character2 = charTmp;
                }
                messageData.addToMessage(TextUtil.t("sex.basic." + this.sextype.toString(), (Person)character1, character2));
                ImageTag specificTag = ImageTag.getSpecificTag(this.sextype.getAssociatedImageTag(), bestMatch.getTags());
                if (specificTag != this.sextype.getAssociatedImageTag() && !(message = TextUtil.t("sex.specific." + (Object)((Object)specificTag), (Person)character1, character2)).equals("sex.specific." + (Object)((Object)specificTag))) {
                    messageData.addToMessage(message);
                }
            }
            return messageData;
        }
    }
}

