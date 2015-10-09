package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;

public class Text extends Model {

  private String kr;
  private String en;
  transient private String fr;
  transient private String de;
  private String ja;
  transient private String zhCN;
  transient private String zhTW;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readString();
    this.kr = br.readString();
    this.en = br.readString();
    this.fr = br.readString();
    this.de = br.readString();
    this.ja = br.readString();
    this.zhCN = br.readString();
    this.zhTW = br.readString();

    return true;
  }
}
