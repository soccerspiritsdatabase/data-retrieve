package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.Element;
import paek.kevin.ssdata.models.enums.SpiritStoneRarity;
import paek.kevin.ssdata.models.enums.SpiritStoneZodiac;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiritStone extends Model {

  transient private String nameId;
  private int value;
  private SpiritStoneRarity rarity;
  private Element element;
  private List<Integer> skills;
  private String iconImage;
  private SpiritStoneZodiac zodiac;
  private int evolution;

  private Text name;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readInt32();
    nameId = br.readString();
    value = br.readInt32();
    rarity = SpiritStoneRarity.valueOf(br.readInt32());
    element = Element.valueOf(br.readInt32());

    skills = new ArrayList<Integer>();
    for (int i = 0; i < 3; i++) {
      int skill = br.readInt32();
      if (skill != 0) {
        skills.add(skill);
      }
    }

    // price
    br.readInt32();
    // baseExp
    br.readInt32();
    // needExp
    br.readInt32();
    iconImage = br.readString();
    zodiac = SpiritStoneZodiac.valueOf(br.readInt32());
    evolution = br.readInt32();

    return true;
  }

  public String getNameId() {
    return nameId;
  }

  public int getValue() {
    return value;
  }

  public SpiritStoneRarity getRarity() {
    return rarity;
  }

  public Element getElement() {
    return element;
  }

  public List<Integer> getSkills() {
    return skills;
  }

  public String getIconImage() {
    return iconImage;
  }

  public SpiritStoneZodiac getZodiac() {
    return zodiac;
  }

  public int getEvolution() {
    return evolution;
  }

  public Text getName() {
    return name;
  }

  public void setName(Text name) {
    this.name = name;
  }
}
