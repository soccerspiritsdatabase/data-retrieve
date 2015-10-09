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
  private List<Integer> skillIds;
  private String iconImage;
  private SpiritStoneZodiac zodiac;
  private int evolutionId;

  private Text name;
  private List<Skill> skills;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readInt32();
    nameId = br.readString();
    value = br.readInt32();
    rarity = SpiritStoneRarity.valueOf(br.readInt32());
    element = Element.valueOf(br.readInt32());

    skillIds = new ArrayList<Integer>();
    for (int i = 0; i < 3; i++) {
      int skill = br.readInt32();
      if (skill != 0) {
        skillIds.add(skill);
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
    evolutionId = br.readInt32();

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

  public List<Integer> getSkillIds() {
    return skillIds;
  }

  public String getIconImage() {
    return iconImage;
  }

  public SpiritStoneZodiac getZodiac() {
    return zodiac;
  }

  public int getEvolutionId() {
    return evolutionId;
  }

  public Text getName() {
    return name;
  }

  public void setName(Text name) {
    this.name = name;
  }

  public List<Skill> getSkills() {
    return skills;
  }

  public void setSkills(List<Skill> skills) {
    this.skills = skills;
  }
}
