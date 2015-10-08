import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DownloadFiles {

  public static final String BASE_URL = "http://bereq-c2s.qpyou.cn/soccerspirits/Android/Live/";
  private static final String PATCH_XML_URL = BASE_URL.concat("PatchLive.xml");

  public DownloadFiles() {

  }

  public void start() throws Exception {
    Document xml = null;
    try {
      xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(PATCH_XML_URL);
    } catch (Exception e) {
      throw new Exception("Failed to get xml data.", e);
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
          dbFiles.add(patchFile);
        }
      }
    }

    for (PatchFile dbFile : dbFiles) {
      try {
        byte[] data = dbFile.download();
        byte[] decryptedData = RC4.decrypt(data);
        Files.write(Paths.get(".").resolve(dbFile.getFileName()), decryptedData);
        return;
      } catch (IOException e) {
        throw new Exception(String.format("Failed to download DB File [%s]", dbFile.getFileName()), e);
      }
    }
  }
}

class PatchFile {

  private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

  private String path;
  private String fileName;
  private String version;
  private String downloadUrl;

  public PatchFile(String path, String fileName, String version) {
    this.path = path;
    this.fileName = fileName;
    this.version = version;
    this.downloadUrl = String.format("%sver%s/%s/%s.scab", DownloadFiles.BASE_URL, version, path, fileName);
  }

  public byte[] download() throws IOException {
    HttpGet httpGet = new HttpGet(downloadUrl);
    HttpResponse httpResponse = HTTP_CLIENT.execute(httpGet);
    return IOUtils.toByteArray(httpResponse.getEntity().getContent());
  }

  public String getPath() {
    return path;
  }

  public String getFileName() {
    return fileName;
  }

  public String getVersion() {
    return version;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }
}

class RC4 {

  private static final byte[] ENCRYPTION_KEY = "1tkdne#:Qho*tL@n6^Tn".getBytes();
  private static final int SKIP = 0x3f5;

  public static byte[] decrypt(final byte[] input) {
    RC4 rc4 = new RC4(ENCRYPTION_KEY);
    return rc4.crypt(input, SKIP);
  }

  private final int[] S = new int[256];

  public RC4(final byte[] key) {
    if (key.length < 1 || key.length > 256) {
      throw new IllegalArgumentException(
              "key must be between 1 and 256 bytes");
    } else {
      int[] buffer = new int[256];
      for (int i = 0; i < 256; i++) {
        S[i] = i;
        buffer[i] = key[i % key.length] & 0xff;
      }

      int j = 0;
      for (int i = 0; i < 256; i++) {
        j = (j + S[j] + buffer[j]) & 0xff;
        int tmp = S[i];
        S[i] = S[j];
        S[j] = tmp;
      }
    }
  }

  public byte[] crypt(final byte[] input, final int skip) {
    final byte[] output = new byte[input.length];

    int i = 0, j = 0, k, t;
    for (int counter = -skip; counter < input.length; counter++) {
      i = (i + 1) & 0xff;
      int tmp = S[i];
      j = (j + tmp) & 0xff;
      S[i] = S[j];
      S[j] = tmp;

      if (counter >= 0) {
        t = (S[i] + S[j]) & 0xFF;
        k = S[t];
        output[counter] = (byte) (input[counter] ^ k);
      }
    }

    return output;
  }
}