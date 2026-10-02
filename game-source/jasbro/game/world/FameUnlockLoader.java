package jasbro.game.world;

import jasbro.game.character.specialization.SpecializationType;
import jasbro.game.housing.HouseType;
import jasbro.game.housing.RoomInfoUtil;
import jasbro.game.interfaces.UnlockObject;
import jasbro.game.world.locations.LocationType;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FameUnlockLoader {
   private static final Logger log = LogManager.getLogger(FameUnlockLoader.class);
   private static final String UNLOCKFILENAME = "fameUnlocks.xml";
   private static final String LISTNODENAME = "unlock";
   private static final String TYPENODENAME = "type";
   private static final String ENTRYNODENAME = "name";
   private static final String FAMENODENAME = "fame";

   public void loadUnlocks(Unlocks unlocks) {
      FileInputStream fileInputStream = null;

      try {
         File file = new File("fameUnlocks.xml");
         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         fileInputStream = new FileInputStream(file);
         Document doc = dBuilder.parse(fileInputStream);
         doc.getDocumentElement().normalize();
         NodeList elements = doc.getElementsByTagName("unlock");

         for (int i = 0; i < elements.getLength(); i++) {
            Element element = (Element)elements.item(i);

            try {
               String type = element.getElementsByTagName("type").item(0).getTextContent().toUpperCase();
               String name = element.getElementsByTagName("name").item(0).getTextContent().toUpperCase();
               long fame = Long.parseLong(element.getElementsByTagName("fame").item(0).getTextContent());
               UnlockObject unlockObject = null;
               if (type.equals("SPECIALIZATION")) {
                  unlockObject = SpecializationType.valueOf(name);
               } else if (type.equals("HOUSE")) {
                  unlockObject = HouseType.valueOf(name);
               } else if (type.equals("ROOM")) {
                  unlockObject = RoomInfoUtil.getRoomUnlock(name);
               } else if (type.equals("LOCATION")) {
                  unlockObject = LocationType.valueOf(name);
               }

               if (unlockObject != null && fame > 0L) {
                  unlocks.addFameUnlock(unlockObject, fame);
               }
            } catch (Exception e) {
               log.error("Error while loading unlock entry {}", new Object[]{element.getTextContent()});
               log.throwing(e);
            }
         }
      } catch (Exception e) {
         log.error("Error while loading unlocks", e);
      } finally {
         if (fileInputStream != null) {
            try {
               fileInputStream.close();
            } catch (IOException e) {
            }
         }
      }
   }
}
