package jasbro.game.character;

import jasbro.game.character.attributes.BaseAttributeTypes;
import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.character.traits.Trait;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageTag;
import jasbro.gui.pictures.ImageUtil;
import jasbro.util.Comparators;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;
import net.java.truevfs.access.TPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CharacterFileLoader {
   private static final Logger log = LogManager.getLogger(CharacterFileLoader.class);
   public static final String CHARACTERPATH = "characters";
   public static final String PROPERTYFILENAME = "properties.xml";
   public static final String IMAGENODENAME = "image";
   public static final String TRAITNODENAME = "trait";
   private static CharacterFileLoader instance;

   private CharacterFileLoader() {
   }

   public static synchronized CharacterFileLoader getInstance() {
      if (instance == null) {
         instance = new CharacterFileLoader();
      }

      return instance;
   }

   public synchronized List<CharacterBase> loadAllCharacters() {
      return this.loadAllCharacters(false);
   }

   public synchronized List<CharacterBase> loadAllCharacters(boolean optimized) {
      TFile characterFolder = new TFile("characters");
      List<CharacterBase> characters = new ArrayList<>();
      this.addCharacters(characterFolder, characters, 5, optimized);
      return characters;
   }

   private void addCharacters(TFile characterFolder, List<CharacterBase> characters, int depth, boolean optimized) {
      if (characterFolder.isDirectory()) {
         for (TFile file : characterFolder.listFiles()) {
            if ((file.isFile() && file.getName().endsWith(".zip") || file.isDirectory())
               && !file.getName().equals("template.zip")
               && !file.getName().equals("template")) {
               String[] files = file.list();
               if (files != null) {
                  if (Arrays.asList(files).contains("properties.xml")) {
                     try {
                        CharacterBase base;
                        if (optimized) {
                           base = this.loadCharacterOptimized(file);
                           if (base.getImages().size() == 0) {
                              log.error("Error: character has no Images: {}", new Object[]{base.getFolder().getNormalizedPath()});
                              base = null;
                           }
                        } else {
                           base = this.loadCharacter(file);
                        }

                        if (base != null) {
                           characters.add(base);
                        }
                     } catch (Exception e) {
                        log.error("Error: character could not be loaded: {}", new Object[]{file.getName()});
                        log.throwing(e);
                     }
                  } else if (depth > 0) {
                     this.addCharacters(file, characters, depth - 1, optimized);
                  }
               }
            }
         }
      }
   }

   public CharacterBase loadCharacter(TFile file) throws IOException {
      TFile properties = null;
      if (file.exists()) {
         TFile[] files = file.listFiles();
         List<TFile> imageList = new ArrayList<>();

         for (TFile curFile : files) {
            if (curFile.getName().equals("properties.xml")) {
               properties = curFile;
            } else if (ImageUtil.getInstance().isImage(curFile)) {
               imageList.add(curFile);
            } else if (curFile.isDirectory()) {
               this.addImagesFromFolder(curFile, imageList, 4);
            }
         }

         if (properties != null) {
            try (InputStream tFileInputStream = new TFileInputStream(properties)) {
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(tFileInputStream);
               doc.getDocumentElement().normalize();
               CharacterBase character = new CharacterBase();
               this.load(character, doc, file);
               Map<String, Element> imageDataMap = new HashMap<>();
               NodeList elements = doc.getElementsByTagName("image");

               for (int i = 0; i < elements.getLength(); i++) {
                  Element element = (Element)elements.item(i);
                  String name = element.getAttribute("name");
                  name = name.replace('\\', '/');
                  imageDataMap.put(name, element);
               }

               for (TFile imageFile : imageList) {
                  String relativePath = new TPath(character.getFolder()).relativize(new TPath(imageFile)).toString();
                  String imageId = character.getFolder().getPath() + File.separator + relativePath;
                  ImageData imageData = new ImageData();
                  imageData.setKey(imageId);
                  relativePath = relativePath.replace('\\', '/');
                  imageData.setFilename(relativePath);
                  character.getImages().add(imageData);
                  if (imageDataMap.containsKey(imageData.getFilename())) {
                     Element element = imageDataMap.get(imageData.getFilename());
                     NodeList attributes = element.getElementsByTagName("tag");

                     for (int i = 0; i < attributes.getLength(); i++) {
                        try {
                           if (attributes.item(i).getTextContent().equals("ASS")) {
                              imageData.getTags().add(ImageTag.ANAL);
                           } else if (attributes.item(i).getTextContent().equals("CLOTHING")) {
                              imageData.getTags().add(ImageTag.CLOTHED);
                           } else if (attributes.item(i).getTextContent().equals("STRAIGHT")) {
                              imageData.getTags().add(ImageTag.VAGINAL);
                           } else if (attributes.item(i).getTextContent().equals("EATOUT")) {
                              imageData.getTags().add(ImageTag.CUNNILINGUS);
                           } else {
                              imageData.getTags().add(ImageTag.valueOf(attributes.item(i).getTextContent()));
                           }
                        } catch (Exception e) {
                           log.error("Error on loading image data", e);
                        }
                     }

                     elements = element.getElementsByTagName("customtext");
                     if (elements != null && elements.getLength() > 0) {
                        imageData.setCustomText(elements.item(0).getTextContent());
                     }
                  }
               }

               Collections.sort(character.getImages(), new Comparators.ImageDataComparator());
               return character;
            } catch (ParserConfigurationException | IOException | SAXException e) {
               IOException ioe = new IOException("Failed to load or parse file '" + properties.getPath() + "'", e);
               throw (IOException)log.throwing(ioe);
            }
         }
      }

      return null;
   }

   private void addImagesFromFolder(TFile folder, List<TFile> imageList, int depth) {
      TFile[] files = folder.listFiles();

      for (TFile curFile : files) {
         if (ImageUtil.getInstance().isImage(curFile)) {
            imageList.add(curFile);
         } else if (curFile.isDirectory() && depth > 0) {
            this.addImagesFromFolder(curFile, imageList, depth - 1);
         }
      }
   }

   private CharacterBase loadCharacterOptimized(TFile file) {
      TFile properties = null;
      if (file.exists()) {
         TFile[] files = file.listFiles();

         for (TFile curFile : files) {
            if (curFile.getName().equals("properties.xml")) {
               properties = curFile;
            }
         }

         if (properties != null) {
            try (InputStream tFileInputStream = new TFileInputStream(properties)) {
               DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
               DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
               Document doc = dBuilder.parse(tFileInputStream);
               doc.getDocumentElement().normalize();
               CharacterBase character = new CharacterBase();
               this.load(character, doc, file);
               NodeList elements = doc.getElementsByTagName("image");

               for (int i = 0; i < elements.getLength(); i++) {
                  Element element = (Element)elements.item(i);
                  String name = element.getAttribute("name");
                  name = name.replace('\\', '/');
                  String imageId = character.getFolder().getPath() + File.separator + name;
                  ImageData imageData = new ImageData();
                  imageData.setKey(imageId);
                  imageData.setFilename(name);
                  character.getImages().add(imageData);
                  NodeList attributes = element.getElementsByTagName("tag");

                  for (int j = 0; j < attributes.getLength(); j++) {
                     try {
                        if (attributes.item(j).getTextContent().equals("ASS")) {
                           imageData.getTags().add(ImageTag.ANAL);
                        } else if (attributes.item(j).getTextContent().equals("CLOTHING")) {
                           imageData.getTags().add(ImageTag.CLOTHED);
                        } else if (attributes.item(j).getTextContent().equals("STRAIGHT")) {
                           imageData.getTags().add(ImageTag.VAGINAL);
                        } else if (attributes.item(j).getTextContent().equals("EATOUT")) {
                           imageData.getTags().add(ImageTag.CUNNILINGUS);
                        } else {
                           imageData.getTags().add(ImageTag.valueOf(attributes.item(j).getTextContent()));
                        }
                     } catch (Exception e) {
                        log.error("Error on loading image data", e);
                     }

                     NodeList elements2 = element.getElementsByTagName("customtext");
                     if (elements2 != null && elements2.getLength() > 0) {
                        imageData.setCustomText(elements2.item(0).getTextContent());
                     }
                  }
               }

               Collections.sort(character.getImages(), new Comparators.ImageDataComparator());
               return character;
            } catch (ArrayIndexOutOfBoundsException e) {
            } catch (IOException e1) {
               e1.printStackTrace();
            } catch (ParserConfigurationException e1) {
               e1.printStackTrace();
            } catch (SAXException e1) {
               e1.printStackTrace();
            }
         }
      }

      return null;
   }

   private void load(CharacterBase character, Document doc, TFile file) {
      if (doc.getElementsByTagName("type").getLength() != 0 && !doc.getElementsByTagName("type").item(0).getTextContent().equals("")) {
         String type = doc.getElementsByTagName("type").item(0).getTextContent();
         character.setType(CharacterType.valueOf(type.toUpperCase()));
      } else {
         character.setType(null);
      }

      character.setName(doc.getElementsByTagName("name").item(0).getTextContent());
      character.setId(file.getName().split("\\.zip")[0]);
      character.setFolder(file);
      NodeList nodes = doc.getElementsByTagName("gender");
      if (nodes.getLength() > 0) {
         String gender = nodes.item(0).getTextContent();
         if (gender != null && !gender.equals("")) {
            character.setGender(Gender.valueOf(gender.toUpperCase()));
         }
      }

      for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
         if (attribute != BaseAttributeTypes.COMMAND) {
            NodeList elements = doc.getElementsByTagName(attribute.toString());
            if (elements == null || elements.getLength() == 0) {
               elements = doc.getElementsByTagName(attribute.getText());
            }

            if (elements != null && elements.getLength() > 0) {
               String content = elements.item(0).getTextContent();
               if (content != null) {
                  try {
                     int value = Integer.parseInt(content);
                     if (value > 0) {
                        character.setAttribute(attribute, value);
                     }
                  } catch (Exception e) {
                  }
               }
            }
         }
      }

      NodeList elements = doc.getElementsByTagName("trait");

      for (int i = 0; i < elements.getLength(); i++) {
         try {
            character.addTrait(Trait.valueOf(elements.item(i).getTextContent()));
         } catch (Exception e) {
            log.error("Error on loading trait", e);
         }
      }

      elements = doc.getElementsByTagName("description");
      if (elements != null && elements.getLength() > 0) {
         character.setDescription(elements.item(0).getTextContent());
      }

      nodes = doc.getElementsByTagName("initialSpecialization");
      if (nodes.getLength() > 0) {
         String specialization = nodes.item(0).getTextContent();
         if (specialization != null && !specialization.equals("")) {
            character.setInitialSpecialization(SpecializationType.valueOf(specialization.toUpperCase()));
         }
      }

      elements = doc.getElementsByTagName("youngerBase");
      if (elements != null && elements.getLength() > 0) {
         character.setYoungerBase(elements.item(0).getTextContent());
      }

      elements = doc.getElementsByTagName("olderBase");
      if (elements != null && elements.getLength() > 0) {
         character.setOlderBase(elements.item(0).getTextContent());
      }
   }

   public synchronized void saveCharacter(CharacterBase character) throws IOException {
      InputStream is = null;

      try {
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.newDocument();
         Element rootElement = doc.createElement("character");
         doc.appendChild(rootElement);
         if (character.getType() != null) {
            Element element = doc.createElement("type");
            rootElement.appendChild(element);
            element.appendChild(doc.createTextNode(character.getType().toString()));
         }

         Element element = doc.createElement("name");
         rootElement.appendChild(element);
         element.appendChild(doc.createTextNode(character.getName()));
         element = doc.createElement("gender");
         rootElement.appendChild(element);
         element.appendChild(doc.createTextNode(character.getGender().toString()));

         for (BaseAttributeTypes attribute : BaseAttributeTypes.values()) {
            if (attribute != BaseAttributeTypes.COMMAND) {
               element = doc.createElement(attribute.toString());
               rootElement.appendChild(element);
               element.appendChild(doc.createTextNode(character.getAttribute(attribute) + ""));
            }
         }

         for (Trait trait : character.getTraits()) {
            element = doc.createElement("trait");
            rootElement.appendChild(element);
            element.appendChild(doc.createTextNode(trait.toString()));
         }

         if (character.getDescription() != null && !character.getDescription().equals("")) {
            element = doc.createElement("description");
            rootElement.appendChild(element);
            element.appendChild(doc.createTextNode(character.getDescription().toString()));
         }

         if (character.getInitialSpecialization() != null) {
            element = doc.createElement("initialSpecialization");
            rootElement.appendChild(element);
            element.appendChild(doc.createTextNode(character.getInitialSpecialization().toString()));
         }

         if (character.getYoungerBase() != null && !character.getYoungerBase().equals("")) {
            element = doc.createElement("youngerBase");
            rootElement.appendChild(element);
            element.appendChild(doc.createTextNode(character.getYoungerBase().toString()));
         }

         if (character.getOlderBase() != null && !character.getOlderBase().equals("")) {
            element = doc.createElement("olderBase");
            rootElement.appendChild(element);
            element.appendChild(doc.createTextNode(character.getOlderBase().toString()));
         }

         for (ImageData image : character.getImages()) {
            element = doc.createElement("image");
            rootElement.appendChild(element);
            element.setAttribute("name", image.getFilename());

            for (ImageTag tag : image.getTags()) {
               Element tagElement = doc.createElement("tag");
               element.appendChild(tagElement);
               tagElement.appendChild(doc.createTextNode(tag.toString()));
            }

            if (image.getCustomText() != null && !image.getCustomText().equals("")) {
               Element textElement = doc.createElement("customtext");
               element.appendChild(textElement);
               textElement.appendChild(doc.createTextNode(image.getCustomText()));
            }
         }

         doc.normalizeDocument();
         ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
         Source xmlSource = new DOMSource(doc);
         Result outputTarget = new StreamResult(outputStream);
         Transformer transformer = TransformerFactory.newInstance().newTransformer();
         transformer.setOutputProperty("indent", "yes");
         transformer.setOutputProperty("omit-xml-declaration", "yes");
         transformer.transform(xmlSource, outputTarget);
         is = new ByteArrayInputStream(outputStream.toByteArray());
         TFile propertiesFile = new TFile(character.getFolder().getCanonicalPath() + File.separator + "properties.xml");
         boolean finished = false;
         int tries = 5;

         do {
            try {
               if (propertiesFile.exists()) {
                  propertiesFile.rm();
               }

               TFile.cp(is, propertiesFile);
               finished = true;
            } catch (IOException e) {
               if (tries <= 0) {
                  throw e;
               }

               tries--;
               log.debug("Write failed, try again");
               Thread.sleep(2000L);
            }
         } while (!finished);

         log.debug("Character saved {}", new Object[]{character.getName()});
      } catch (Exception e) {
         log.error("Error on writing changes into XML file", e);
         throw new IOException("Error on writing changes into XML file", e);
      } finally {
         if (is != null) {
            try {
               is.close();
            } catch (IOException e) {
               log.error("Error on closing input stream", e);
            }
         }
      }
   }

   public void addImages(CharacterBase character, File[] selectedFiles) {
      try {
         for (File f : selectedFiles) {
            if (ImageUtil.getInstance().isImage(f.toPath())) {
               String newKey = character.getFolder().getCanonicalPath() + File.separator + f.getName();
               this.deletePicture(newKey);
               TFile newFile = new TFile(newKey);
               TFile.cp(f, newFile);
               ImageData imageData = new ImageData();
               imageData.setKey(newKey);
               imageData.setFilename(f.getName());
               character.getImages().add(imageData);
            }
         }
      } catch (Exception e) {
         log.error("Error on adding images", e);
      }
   }

   public synchronized void deletePicture(ImageData image) {
      this.deletePicture(image.getKey());
   }

   private synchronized void deletePicture(String imageKey) {
      try {
         TFile file = new TFile(imageKey);
         if (file.exists()) {
            file.rm();
         }
      } catch (IOException e) {
         log.error("Failed to delete image with key '{}'", new Object[]{imageKey});
         log.throwing(e);
      }
   }

   public synchronized CharacterBase createCharacter(String id, List<CharacterBase> characters) {
      try {
         if (id != null && !id.equals("")) {
            for (CharacterBase character : characters) {
               if (id.equals(character.getId())) {
                  return null;
               }
            }

            TFile source = new TFile("characters" + File.separator + "template");
            if (!source.exists()) {
               source = new TFile("characters" + File.separator + "template.zip");
            }

            TFile destination = new TFile("characters" + File.separator + id);
            TFile.cp_r(source, destination, source.getArchiveDetector(), destination.getArchiveDetector());
            CharacterBase character = new CharacterBase();
            character.setId(id);
            character.setName("Template");
            character.setFolder(destination);
            character.getImages().add(new ImageData("characters" + File.separator + character.getId() + File.separator + "template.jpg"));
            characters.add(character);
            return character;
         } else {
            return null;
         }
      } catch (Exception e) {
         log.error("Error on creating character", e);
         return null;
      }
   }
}
