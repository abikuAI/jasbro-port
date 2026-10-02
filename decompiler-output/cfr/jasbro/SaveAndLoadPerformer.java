/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thoughtworks.xstream.XStream
 *  com.thoughtworks.xstream.io.HierarchicalStreamDriver
 *  com.thoughtworks.xstream.io.xml.StaxDriver
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import jasbro.game.GameData;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SaveAndLoadPerformer {
    private static final Logger log = LogManager.getLogger(SaveAndLoadPerformer.class);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save(File file, GameData gameData) {
        BufferedWriter writer = null;
        try {
            if (file.exists()) {
                file.delete();
            }
            XStream xstream = new XStream((HierarchicalStreamDriver)new StaxDriver());
            xstream.autodetectAnnotations(true);
            String xml = xstream.toXML((Object)gameData);
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(xml);
            writer.flush();
        }
        catch (Exception e) {
            log.error("Error on saving data", (Throwable)e);
        }
        finally {
            if (writer != null) {
                try {
                    writer.close();
                }
                catch (IOException e) {
                    log.error("Error on closing writer", (Throwable)e);
                }
            }
        }
    }

    public GameData load(File selectedFile) throws IOException {
        GameData gameData = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(selectedFile));){
            String line;
            XStream xstream = new XStream((HierarchicalStreamDriver)new StaxDriver());
            xstream.autodetectAnnotations(true);
            String xml = "";
            do {
                if ((line = bufferedReader.readLine()) == null) continue;
                xml = xml + line + "\n";
            } while (line != null);
            if (log.isDebugEnabled()) {
                BufferedWriter writer = new BufferedWriter(new FileWriter(new File("test.xml")));
                writer.write(xml);
                writer.flush();
                writer.close();
            }
            gameData = (GameData)xstream.fromXML(xml);
        }
        catch (IOException e) {
            IOException le = new IOException("Failed to open or read file '" + selectedFile + "'", e);
            log.error((Object)le);
            throw le;
        }
        return gameData;
    }
}

