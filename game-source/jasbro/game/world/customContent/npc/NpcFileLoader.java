package jasbro.game.world.customContent.npc;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import jasbro.Jasbro;
import jasbro.game.interfaces.HasImagesInterface;
import jasbro.gui.pictures.ImageData;
import jasbro.gui.pictures.ImageUtil;
import jasbro.util.Comparators;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileReader;
import net.java.truevfs.access.TFileWriter;
import net.java.truevfs.access.TPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NpcFileLoader {
   private static final Logger log = LogManager.getLogger(NpcFileLoader.class);
   public static final String ENEMYPATH = "npcs/enemies";
   public static final String ENEMYFILENAME = "properties.xml";
   private static NpcFileLoader instance;

   private NpcFileLoader() {
   }

   public static synchronized NpcFileLoader getInstance() {
      if (instance == null) {
         instance = new NpcFileLoader();
      }

      return instance;
   }

   public synchronized List<ComplexEnemyTemplate> loadAllEnemies() {
      return AccessController.doPrivileged(new PrivilegedAction<List<ComplexEnemyTemplate>>() {
         public List<ComplexEnemyTemplate> run() {
            List<ComplexEnemyTemplate> customQuests = new ArrayList<>();
            TFile questFolder = new TFile("npcs/enemies");
            NpcFileLoader.this.addEnemies(questFolder, customQuests, 5);
            return customQuests;
         }
      });
   }

   private void addEnemies(TFile enemyFolder, List<ComplexEnemyTemplate> enemies, int depth) {
      if (enemyFolder.isDirectory()) {
         for (TFile file : enemyFolder.listFiles()) {
            if (file.isFile() && file.getName().equals("properties.xml")) {
               try {
                  enemies.add(this.loadEnemyTemplate(file));
               } catch (Exception e) {
                  log.error("Error loading enemy file: {}", new Object[]{file.getName()});
                  log.throwing(e);
               }
            } else if (depth > 0 && file.isDirectory()) {
               this.addEnemies(file, enemies, depth - 1);
            }
         }
      }
   }

   private ComplexEnemyTemplate loadEnemyTemplate(TFile file) throws IOException {
      ComplexEnemyTemplate enemyTemplate = null;
      BufferedReader bufferedReader = null;

      try {
         XStream xstream = new XStream(new StaxDriver());
         xstream.autodetectAnnotations(true);
         bufferedReader = new BufferedReader(new TFileReader(file));
         String xml = "";

         String line;
         do {
            line = bufferedReader.readLine();
            if (line != null) {
               xml = xml + line + "\n";
            }
         } while (line != null);

         enemyTemplate = (ComplexEnemyTemplate)xstream.fromXML(xml);
         enemyTemplate.setFile(file);
         enemyTemplate.setId(file.getParentFile().getName().split("\\.")[0]);
         this.scanForImages(file.getParentFile(), enemyTemplate);
      } catch (IOException e) {
         log.error("File does not exist or can not be read", e);
         throw e;
      } finally {
         if (bufferedReader != null) {
            try {
               bufferedReader.close();
            } catch (IOException e) {
            }
         }
      }

      return enemyTemplate;
   }

   public void save(ComplexEnemyTemplate enemyTemplate) {
      BufferedWriter writer = null;
      TFile file = enemyTemplate.getFile();

      try {
         if (file == null) {
            TFile folder = new TFile("npcs/enemies/" + enemyTemplate.getId());
            folder.mkdir();
            file = new TFile("npcs/enemies/" + enemyTemplate.getId() + "/" + "properties.xml");
            enemyTemplate.setFile(file);
         }

         if (file.exists()) {
            file.rm();
         }

         XStream xstream = new XStream(new StaxDriver());
         xstream.autodetectAnnotations(true);
         String xml = xstream.toXML(enemyTemplate);
         writer = new BufferedWriter(new TFileWriter(file));
         writer.write(xml);
         writer.flush();
      } catch (Exception e) {
         log.error("Error on saving data", e);
      } finally {
         if (writer != null) {
            try {
               writer.close();
            } catch (IOException e) {
               log.error("Error on closing writer", e);
            }
         }
      }
   }

   public void delete(ComplexEnemyTemplate enemyTemplate) {
      try {
         if (enemyTemplate.getFile() != null) {
            enemyTemplate.getFile().rm();
         }

         Jasbro.getInstance().getEnemyTemplates().remove(enemyTemplate.getId());
      } catch (IOException e) {
         log.error("Error on deleting enemy", e);
      }
   }

   public void scanForImages(TFile folder, HasImagesInterface npc) {
      List<TFile> imageList = new ArrayList<>();

      for (TFile curFile : folder.listFiles()) {
         if (ImageUtil.getInstance().isImage(curFile)) {
            imageList.add(curFile);
         } else if (curFile.isDirectory()) {
            this.addImagesFromFolder(curFile, imageList, 2);
         }
      }

      for (TFile imageFile : imageList) {
         String relativePath = new TPath(folder).relativize(new TPath(imageFile)).toString();
         String imageId = folder.getPath() + File.separator + relativePath;
         ImageData imageData = new ImageData();
         imageData.setKey(imageId);
         relativePath = relativePath.replace('\\', '/');
         imageData.setFilename(relativePath);
         if (!npc.getImages().contains(imageData)) {
            npc.getImages().add(imageData);
         }
      }

      Collections.sort(npc.getImages(), new Comparators.ImageDataComparator());
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
}
