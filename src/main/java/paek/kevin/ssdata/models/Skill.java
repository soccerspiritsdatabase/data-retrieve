package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.SkillType;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Skill extends Model {

  transient private String nameId;
  transient private String descId;
  private SkillType type;
  private float cost;
  private int cooldown;
  private List<SkillValue> values;
  private String effectType;
  private int effectBgType;
  private String icon;

  private Text name;
  private Text description;
  private List<SkillEffect> effects;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readInt32();

    nameId = br.readString();
    descId = br.readString();
    type = SkillType.valueOf(br.readInt32());
    cost = br.readSingle();
    cooldown = br.readInt32();
    // coolFirst
    br.readInt32();
    // useStamina
    br.readSingle();
    // coolGrowth
    br.readSingle();
    // successPer
    br.readSingle();
    // successPerGrowth
    br.readSingle();
    // sustainmentTime
    br.readInt32();
    // buffEffect
    br.readInt32();
    // addon
    br.readInt32();
    // addonMax
    br.readInt32();

    // targetSection
    br.readInt32();
    // conditionCheck
    br.readInt32();
    // conditionPos
    br.readInt32();
    // condition1
    br.readInt32();
    // conditionValue1
    br.readInt32();
    // count
    br.readInt32();
    // condition2
    br.readInt32();
    // conditionValue2
    br.readInt32();

    values = new ArrayList<SkillValue>();
    for (int i = 0; i < 5; i++) {
      SkillValue skillValue = new SkillValue();
      int value = br.readInt32();
      skillValue.init = br.readSingle();
      skillValue.growth = br.readSingle();
      // cardGrowth
      br.readSingle();
      if (value != 0) {
        values.add(skillValue);
      }
    }

    effectType = br.readString();
    effectBgType = br.readInt32();
    icon = br.readString();
    // buffCheck
    br.readInt32();
    // effectText
    br.readString();

    return true;
  }

  public String getNameId() {
    return nameId;
  }

  public String getDescId() {
    return descId;
  }

  public SkillType getType() {
    return type;
  }

  public float getCost() {
    return cost;
  }

  public int getCooldown() {
    return cooldown;
  }

  public List<SkillValue> getValues() {
    return values;
  }

  public String getEffectType() {
    return effectType;
  }

  public int getEffectBgType() {
    return effectBgType;
  }

  public String getIcon() {
    return icon;
  }

  public Text getName() {
    return name;
  }

  public void setName(Text name) {
    this.name = name;
  }

  public Text getDescription() {
    return description;
  }

  public void setDescription(Text description) {
    this.description = description;
    if ('.' != description.getEn().charAt(description.getEn().length() - 1)) {
      description.setEn(description.getEn() + ".");
    }

    int levelMin = 0, levelMax = 0;
    switch (type) {
      case ACTIVE:
      case PASSIVE:
        levelMin = 1;
        levelMax = 5;
        break;
      case ACE:
        levelMin = levelMax = 1;
        break;
      case ITEM:
        levelMin = levelMax = 15;
        break;
    }

    this.effects = new ArrayList<SkillEffect>();
    for (int level = levelMin; level <= levelMax; level++) {
      SkillEffect effect = new SkillEffect();
      effect.description  = this.description.clone();
      for (int i = 0; i < values.size(); i++) {
        SkillValue value = values.get(i);
        int factor = level - 1;
        if (type == SkillType.ITEM && level == levelMax) {
          factor = level + 1;
        }
        float calcValue = value.init + (value.growth * factor);
        String calcValueStr = (calcValue == (int) calcValue) ? String.format("%d", (int) calcValue) : String.format("%s", calcValue);
        effect.description.setEn(effect.description.getEn().replace("{" + i + "}", calcValueStr));
        effect.description.setKr(effect.description.getKr().replace("{" + i + "}", calcValueStr));
      }
      effect.level = level;
      this.effects.add(effect);
    }
  }



  class SkillValue {
    private float init;
    private float growth;
  }

  class Range {
    private int min;
    private int max;
  }

  class SkillEffect {
    private int level;
    private Text description;
  }
}
