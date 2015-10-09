package paek.kevin.ssdata.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import paek.kevin.ssdata.Config;

import java.io.IOException;

public class PatchFile {

  private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();

  private String path;
  private String fileName;
  private String version;
  private String downloadUrl;

  public PatchFile(String path, String fileName, String version) {
    this.path = path;
    this.fileName = fileName;
    this.version = version;

    this.downloadUrl = String.format("%sver%s/%s/%s.scab", Config.BASE_URL, version, path, fileName);
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
