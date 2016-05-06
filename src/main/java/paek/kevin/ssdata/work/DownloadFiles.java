package paek.kevin.ssdata.work;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import paek.kevin.ssdata.Config;
import paek.kevin.ssdata.models.Character;
import paek.kevin.ssdata.utils.PatchFile;
import paek.kevin.ssdata.utils.RC4;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DownloadFiles {

  public static void getDbFiles() throws RuntimeException {
    Map<String, PatchFile> dbFilesMap = new HashMap<String, PatchFile>();
    Collection<PatchFile> dbFiles = getPatchFiles("DB");
    for (Iterator<PatchFile> iterator = dbFiles.iterator(); iterator.hasNext();) {
      PatchFile dbFile = iterator.next();
      if (!Config.DB_FILES.contains(dbFile.getFileName())) {
        iterator.remove();
      }
    }

    System.out.println("----------------------------------------");
    System.out.println("Downloading DB files");
    for (PatchFile dbFile : dbFiles) {
      try {
        System.out.println(dbFile.getFileName());
        System.out.println(String.format("\tDownloading (%s)", dbFile.getDownloadUrl()));
        byte[] data = download(dbFile.getDownloadUrl());
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

  public static void getCardFiles(Map<Object, Character> characters) {
    Map<Integer, Character> imageIds = new HashMap<Integer, Character>();
    for (Character character : characters.values()) {
      imageIds.put(character.getImageId(), character);
      for (Integer id : character.getSkins()) {
        imageIds.put(id, character);
      }
    }

    Collection<PatchFile> cardFiles = getPatchFiles("Cards");
    for (Iterator<PatchFile> iterator = cardFiles.iterator(); iterator.hasNext();) {
      PatchFile cardFile = iterator.next();
      if (!cardFile.getFileName().endsWith("_CS")) {
        iterator.remove();
      } else {
        int imageId = Integer.parseInt(cardFile.getFileName().substring(0, cardFile.getFileName().indexOf("_")));
        if (!imageIds.containsKey(imageId)) {
          iterator.remove();
        }
      }
    }

    System.out.println("----------------------------------------");
    System.out.println("Downloading Card files");
    for (PatchFile cardFile : cardFiles) {
      try {
        Path destination = Config.RAW_CARD_FILES_DIR.resolve(cardFile.getFileName());
        Path imageDestination = Config.CARD_FILES_DIR.resolve(cardFile.getFileName() + ".jpg");

        if (!Files.exists(imageDestination)) {
          System.out.println(cardFile.getFileName());
          if (true || !Files.exists(destination)) {
            System.out.println(String.format("\tDownloading (%s)", cardFile.getDownloadUrl()));
            byte[] data = download(cardFile.getDownloadUrl());
            Files.createDirectories(destination.getParent());
            Files.write(destination, data);
          }

          System.out.println("\tExtracting");
          Runtime.getRuntime().exec(String.format("cmd /c %s bundle-extract %s", Config.PATH_DISUNITY, destination)).waitFor();
          Path rootDir = Config.RAW_CARD_FILES_DIR.resolve(cardFile.getFileName() + "_");
          String hash = rootDir.toFile().listFiles()[0].getName();
          Path tempDir = rootDir.resolve(hash).normalize();
          Runtime.getRuntime().exec(String.format("cmd /c %s extract %s", Config.PATH_DISUNITY, tempDir)).waitFor();
          tempDir = tempDir.getParent().resolve(hash + "_").resolve("Texture2D");

          Runtime.getRuntime().exec(String.format("cmd /c %s -i %s -f r8g8b8a8 -d", Config.PATH_PVR_TEX_TOOL, tempDir.resolve(cardFile.getFileName() + ".ktx"))).waitFor();
          Runtime.getRuntime().exec(String.format("cmd /c %s -i %s -f r8g8b8a8 -d", Config.PATH_PVR_TEX_TOOL, tempDir.resolve(cardFile.getFileName() + "_A.ktx"))).waitFor();

          int id = Integer.parseInt(cardFile.getFileName().substring(0, cardFile.getFileName().indexOf("_")));
          Character character = imageIds.get(id);
          if (character != null) {
            System.out.println("\tMerging/Flipping/Scaling");
            BufferedImage source = ImageIO.read(tempDir.resolve(cardFile.getFileName() + ".png").toFile());
            BufferedImage mask = ImageIO.read(tempDir.resolve(cardFile.getFileName() + "_A.png").toFile());

            BufferedImage output = processImage(source, mask, character);
            System.out.println(String.format("\tSaving (%s)", imageDestination));
            Files.createDirectories(imageDestination.getParent());
            ImageIO.write(output, "jpg", imageDestination.toFile());

            System.out.println("\tCleanup");
            deletePath(rootDir);
          } else {
            System.out.println(String.format("\tCharacter was not found for %s", cardFile.getFileName()));
          }
        }
      } catch (Exception e) {
        throw new RuntimeException(String.format("Failed to download DB File [%s]", cardFile.getFileName()), e);
      }
    }
  }

  private static Collection<PatchFile> getPatchFiles(String type) {
    Document xml = null;
    try {
      System.out.println("----------------------------------------");
      byte[] data;
      if (Files.exists(Config.PATCH_XML_FILE)) {
        System.out.println(String.format("Loading patch info from disk (%s)", Config.PATCH_XML_FILE));
        data = Files.readAllBytes(Config.PATCH_XML_FILE);
      } else {
        System.out.println(String.format("Downloading patch info (%s)", Config.PATCH_XML_URL));
        data = download(Config.PATCH_XML_URL);
        Files.createDirectories(Config.PATCH_XML_FILE.getParent());
        Files.write(Config.PATCH_XML_FILE, data);
      }
      xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(data));
    } catch (Exception e) {
      throw new RuntimeException("Failed to get xml data.", e);
    }

    Map<String, PatchFile> map = new HashMap<String, PatchFile>();

    xml.getDocumentElement().normalize();
    NodeList list = xml.getElementsByTagName("PatchFile");
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        PatchFile patchFile = new PatchFile(
                element.getAttribute("Path"),
                element.getAttribute("FileName"),
                element.getAttribute("Version"),
                element.getAttribute("HashValue")
        );
        if (patchFile.getPath().equals(type)) {
          PatchFile prev = map.get(patchFile.getFileName());
          if (prev == null || patchFile.compareTo(prev) > 0) {
            map.put(patchFile.getFileName(), patchFile);
          }
        }
      }
    }
    return map.values();
  }

  private static byte[] download(String url) throws IOException {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
    byte[] data = IOUtils.toByteArray(httpResponse.getEntity().getContent());
    httpGet.abort();
    httpResponse.close();
    httpClient.close();
    return data;
  }

  private static BufferedImage processImage(BufferedImage source, BufferedImage mask, Character character) throws IOException {
    String backgroundName;
    String borderName;
    if (character.isManager()) {
      backgroundName = "manager";
      borderName = "manager";
    } else {
      backgroundName = character.getElement().toString();
      borderName = character.getElement().toString();
    }

    if (character.getValue() >= 5) {
      backgroundName += "_shiny";
      if (character.isManager()) {
        borderName += "_shiny";
      }
    }

    BufferedImage background = ImageIO.read(Config.RES_CARDS_BACKGROUND_DIR.resolve(backgroundName + ".png").toFile());
    BufferedImage border = ImageIO.read(Config.RES_CARDS_BORDER_DIR.resolve(borderName + ".png").toFile());

    BufferedImage alphaMerged = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_ARGB);
    // apply mask to alpha values
    for (int x = 0; x < source.getWidth(); x++) {
      for (int y = 0; y < source.getHeight(); y++) {
        source.setRGB(x, y, (source.getRGB(x, y) & 0x00ffffff) ^ (mask.getRGB(x, y) & 0x00ff0000) << 8);
      }
    }

    // flip image vertically (top to bottom)
    Graphics2D graphics2D = alphaMerged.createGraphics();
    graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    AffineTransform transform = new AffineTransform();
    transform.concatenate(AffineTransform.getScaleInstance(1, -1));
    transform.concatenate(AffineTransform.getTranslateInstance(0, -alphaMerged.getHeight()));
    graphics2D.transform(transform);
    graphics2D.drawImage(source, 0, 0, alphaMerged.getWidth(), alphaMerged.getHeight(), 0, 0, source.getWidth(), source.getHeight(), null);
    graphics2D.dispose();
    alphaMerged.flush();

    BufferedImage output = new BufferedImage(background.getWidth(), background.getHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics graphics = output.getGraphics();
    graphics.drawImage(background, 0, 0, null);
    graphics.drawImage(alphaMerged, 0, 0, null);
    graphics.drawImage(border, 0, 0, null);
    graphics.dispose();
    output.flush();

    return output;
  }

  private static void deletePath(Path path) throws IOException {
    if (Files.isDirectory(path)) {
      for (File file : path.toFile().listFiles()) {
        deletePath(file.toPath());
      }
    }
    Files.delete(path);
  }
}