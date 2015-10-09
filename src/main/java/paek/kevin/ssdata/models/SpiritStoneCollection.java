package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.Element;
import paek.kevin.ssdata.models.enums.SpiritStoneRarity;
import paek.kevin.ssdata.models.enums.SpiritStoneZodiac;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiritStoneCollection extends Model {

  private List<Integer> commons;
  private List<Integer> rares;
  private List<Integer> uniques;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = String.valueOf(br.readInt32());

    commons = new ArrayList<Integer>();
    for (int i = 0; i < 4; i++) {
      commons.add(br.readInt32());
    }

    rares = new ArrayList<Integer>();
    for (int i = 0; i < 4; i++) {
      rares.add(br.readInt32());
    }

    uniques = new ArrayList<Integer>();
    for (int i = 0; i < 4; i++) {
      uniques.add(br.readInt32());
    }

    return true;
  }
}
