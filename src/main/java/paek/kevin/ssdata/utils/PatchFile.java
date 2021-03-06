package paek.kevin.ssdata.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import paek.kevin.ssdata.Config;

import java.io.IOException;

public class PatchFile implements Comparable<PatchFile> {

  private String path;
  private String fileName;
  private String version;
  private String hashValue;
  private String downloadUrl;

  public PatchFile(String path, String fileName, String version, String hashValue) {
    this.path = path;
    this.fileName = fileName;
    this.version = version;
    this.hashValue = hashValue;

    this.downloadUrl = String.format("%sver%s/%s/%s.scab", Config.BASE_URL, version, path, fileName);
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
  public double getVersionAsDouble() {
    return Double.parseDouble(version);
  }

  public String getHashValue() {
    return hashValue;
  }

  public String getDownloadUrl() {
    return downloadUrl;
  }

  public int compareTo(PatchFile o) {
    int compare = this.getFileName().compareTo(o.getFileName());
    if (compare == 0) {
      if (this.getHashValue() == null || this.getHashValue().isEmpty()) {
        compare = -1;
      } else {
        compare = Double.compare(this.getVersionAsDouble(), o.getVersionAsDouble());
      }
    }
    return compare;
  }
}
