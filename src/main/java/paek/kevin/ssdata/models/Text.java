package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;

public class Text extends Model {

  private String kr;
  private String en;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readString();
    kr = filter(br.readString());
    en = filter(br.readString());
    // fr
    br.readString();
    // de
    br.readString();
    // ja
    br.readString();
    // zhCN
    br.readString();
    // zhTW
    br.readString();

    return true;
  }

  public Text clone() {
    Text clone = new Text();
    clone.id = id;
    clone.kr = kr;
    clone.en = en;
    return clone;
  }

  private String filter(String input) {
    return input.replaceAll("\\[[\\dA-F-]+\\]", "");
  }

  public String getKr() {
    return kr;
  }

  public void setKr(String kr) {
    this.kr = kr;
  }

  public String getEn() {
    return en;
  }

  public void setEn(String en) {
    this.en = en;
  }
}
