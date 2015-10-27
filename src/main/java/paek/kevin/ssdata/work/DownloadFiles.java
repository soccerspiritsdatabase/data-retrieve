package paek.kevin.ssdata.work;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import paek.kevin.ssdata.Config;
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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class DownloadFiles {

  public static void process() throws RuntimeException {
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

    List<PatchFile> cardFiles = new ArrayList<PatchFile>();
    List<PatchFile> dbFiles = new ArrayList<PatchFile>();

    xml.getDocumentElement().normalize();
    NodeList list = xml.getElementsByTagName("PatchFile");
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        PatchFile patchFile = new PatchFile(element.getAttribute("Path"), element.getAttribute("FileName"), element.getAttribute("Version"));
        if ("Cards".equals(patchFile.getPath()) && patchFile.getFileName().contains("_CS")) {
          cardFiles.add(patchFile);
        } else if ("DB".equals(patchFile.getPath())) {
          if (Config.DB_FILES.contains(patchFile.getFileName())) {
            dbFiles.add(patchFile);
          }
        }
      }
    }

    System.out.println("----------------------------------------");
    System.out.println("Downloading Card files");
    for (PatchFile cardFile : cardFiles) {
      try {
        Path destination = Config.RAW_CARD_FILES_DIR.resolve(cardFile.getFileName());
        Path imageDestination = Config.CARD_FILES_DIR.resolve(cardFile.getFileName() + ".png");

        if (!Files.exists(imageDestination)) {
          System.out.println(cardFile.getFileName());
          if (!Files.exists(destination)) {
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

          System.out.println("\tMerging/Flipping/Scaling");
          BufferedImage source = ImageIO.read(tempDir.resolve(cardFile.getFileName() + ".png").toFile());
          BufferedImage mask = ImageIO.read(tempDir.resolve(cardFile.getFileName() + "_A.png").toFile());

          BufferedImage output = processImage(source, mask);
          System.out.println(String.format("\tSaving (%s)", imageDestination));
          Files.createDirectories(imageDestination.getParent());
          ImageIO.write(output, "png", imageDestination.toFile());

          System.out.println("\tCleanup");
          deletePath(rootDir);
        }
      } catch (Exception e) {
        throw new RuntimeException(String.format("Failed to download DB File [%s]", cardFile.getFileName()), e);
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

  private static BufferedImage processImage(BufferedImage source, BufferedImage mask) {
    BufferedImage output = new BufferedImage(source.getWidth(), Math.round(source.getHeight() * Config.CARD_SIZE_RATIO), BufferedImage.TYPE_INT_ARGB);
    // apply mask to alpha values
    for (int x = 0; x < source.getWidth(); x++) {
      for (int y = 0; y < source.getHeight(); y++) {
        source.setRGB(x, y, (source.getRGB(x, y) & 0x00ffffff) ^ (mask.getRGB(x, y) & 0x00ff0000) << 8);
      }
    }

    // flip image vertically (top to bottom)
    Graphics2D graphics2D = output.createGraphics();
    AffineTransform transform = new AffineTransform();
    transform.concatenate(AffineTransform.getScaleInstance(1, -1));
    transform.concatenate(AffineTransform.getTranslateInstance(0, -output.getHeight()));
    graphics2D.transform(transform);
    graphics2D.drawImage(source, 0, 0, output.getWidth(), output.getHeight(), 0, 0, source.getWidth(), source.getHeight(), null);
    graphics2D.dispose();

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