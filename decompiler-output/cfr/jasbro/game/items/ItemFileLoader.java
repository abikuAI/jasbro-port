/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.thoughtworks.xstream.XStream
 *  com.thoughtworks.xstream.io.HierarchicalStreamDriver
 *  com.thoughtworks.xstream.io.xml.StaxDriver
 *  net.java.truevfs.access.TFile
 *  net.java.truevfs.access.TFileReader
 *  net.java.truevfs.access.TFileWriter
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package jasbro.game.items;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import jasbro.Jasbro;
import jasbro.game.items.Item;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileReader;
import net.java.truevfs.access.TFileWriter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ItemFileLoader {
    private static final Logger log = LogManager.getLogger(ItemFileLoader.class);
    public static final String ITEMPATH = "items";
    public static final String ITEMFILEREGEX = ".+\\.xml";
    private static ItemFileLoader instance;

    private ItemFileLoader() {
    }

    public static synchronized ItemFileLoader getInstance() {
        if (instance == null) {
            instance = new ItemFileLoader();
        }
        return instance;
    }

    public synchronized List<Item> loadAllItems() {
        TFile itemFolder = new TFile(ITEMPATH);
        ArrayList<Item> items = new ArrayList<Item>();
        this.addItems(itemFolder, items, 5);
        return items;
    }

    private void addItems(TFile itemFolder, List<Item> items, int depth) {
        if (itemFolder.isDirectory()) {
            for (TFile file : itemFolder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".xml")) {
                    try {
                        items.add(this.loadItem(file));
                    }
                    catch (Exception e) {
                        log.error("Error loading item file: {}", new Object[]{file.getName()});
                        log.throwing((Throwable)e);
                    }
                    continue;
                }
                if (depth <= 0 || !file.isDirectory()) continue;
                this.addItems(file, items, depth - 1);
            }
        }
    }

    private Item loadItem(TFile file) throws IOException {
        Item item = null;
        try (BufferedReader bufferedReader = new BufferedReader((Reader)new TFileReader((File)file));){
            String line;
            XStream xstream = new XStream((HierarchicalStreamDriver)new StaxDriver());
            xstream.autodetectAnnotations(true);
            String xml = "";
            do {
                if ((line = bufferedReader.readLine()) == null) continue;
                xml = xml + line + "\n";
            } while (line != null);
            item = (Item)xstream.fromXML(xml);
            item.setFile(file);
            item.setId(file.getName().substring(0, file.getName().length() - 4));
        }
        catch (IOException e) {
            throw (IOException)log.throwing((Throwable)e);
        }
        return item;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void save(Item item) {
        BufferedWriter writer = null;
        TFile file = item.getFile();
        if (file == null) {
            file = new TFile("items/" + item.getId() + ".xml");
            item.setFile(file);
        }
        try {
            if (file.exists()) {
                file.rm();
            }
            XStream xstream = new XStream((HierarchicalStreamDriver)new StaxDriver());
            xstream.autodetectAnnotations(true);
            String xml = xstream.toXML((Object)item);
            writer = new BufferedWriter((Writer)new TFileWriter((File)file));
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

    public void delete(Item item) {
        try {
            if (item.getFile() != null) {
                item.getFile().rm();
            }
            Jasbro.getInstance().getItems().remove(item.getId());
        }
        catch (IOException e) {
            log.error("Error on deleting item", (Throwable)e);
        }
    }
}

