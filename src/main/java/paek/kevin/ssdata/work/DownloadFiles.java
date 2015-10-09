package paek.kevin.ssdata.work;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import paek.kevin.ssdata.Config;
import paek.kevin.ssdata.utils.PatchFile;
import paek.kevin.ssdata.utils.RC4;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DownloadFiles implements Worker {

  public void doWork() throws RuntimeException {
    Document xml = null;
    try {
      System.out.println("----------------------------------------");
      System.out.println(String.format("Downloading patch info (%s)", Config.PATCH_XML_URL));
      xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(Config.PATCH_XML_URL);
    } catch (Exception e) {
      throw new RuntimeException("Failed to get xml data.", e);
    }

    List<PatchFile> cardFiles = new ArrayList<PatchFile>();
    List<PatchFile> dbFiles = new ArrayList<PatchFile>();

    xml.getDocumentElement().normalize();
    NodeList list = xml.getElementsByTagName("PatchFile");
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        PatchFile patchFile = new PatchFile(element.getAttribute("Path"), element.getAttribute("FileName"), element.getAttribute("Version"));
        if ("Cards".equals(patchFile.getPath())) {
          cardFiles.add(patchFile);
        } else if ("DB".equals(patchFile.getPath())) {
          if (Config.DB_FILES.contains(patchFile.getFileName())) {
            dbFiles.add(patchFile);
          }
        }
      }
    }

    System.out.println("----------------------------------------");
    System.out.println("Downloading DB files");
    for (PatchFile dbFile : dbFiles) {
      try {
        System.out.println(dbFile.getFileName());
        System.out.println(String.format("\tDownloading (%s)", dbFile.getDownloadUrl()));
        byte[] data = dbFile.download();
        System.out.println("\tDecrypting");
        byte[] decryptedData = RC4.decrypt(data);
        Path destination = Config.RAW_DB_FILES_DIR.resolve(dbFile.getFileName());
        System.out.println(String.format("\tWriting to disk (%s)", destination.normalize()));
        Files.createDirectories(destination.getParent());
        Files.write(destination, decryptedData);
        System.out.println("\tDone");
      } catch (IOException e) {
        throw new RuntimeException(String.format("Failed to download DB File [%s]", dbFile.getFileName()), e);
      }
    }
  }
}

