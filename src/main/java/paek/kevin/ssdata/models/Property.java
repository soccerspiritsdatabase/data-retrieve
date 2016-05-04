package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;

public class Property extends Model {

  private String value;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readString();
    value = br.readString();

    return true;
  }

  public String getValue() {
    return value;
  }
}
