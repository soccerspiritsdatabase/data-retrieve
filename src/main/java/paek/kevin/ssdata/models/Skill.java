package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.SkillType;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Skill extends Model {

  private String nameId;
  private String descId;
  private SkillType type;
  private float cost;
  private int cooldown;

  private List<Effect> effects;

  private String effectType;
  private int effectBgType;
  private String icon;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = String.valueOf(br.readInt32());

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

    effects = new ArrayList<Effect>();
    for (int i = 0; i < 5; i++) {
      Effect effect = new Effect();
      int value = br.readInt32();
      effect.init = br.readSingle();
      effect.growth = br.readSingle();
      // cardGrowth
      br.readSingle();
      if (value != 0) {
        effects.add(effect);
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

  class Effect {
    private float init;
    private float growth;
  }
}
