package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.*;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Character extends Model {

  private int imageId;
  transient private int chainId;
  private CharacterChain chain;
  private String nameId;
  private int value;
  private Gender gender;
  private Element element;
  private List<Element> stoneElements;
  private List<Position> positions;
  private PlayerType type;
  private int teamId;
  private int cost;
  private Stat power;
  private Stat technique;
  private Stat vitality;
  private Stat speed;
  private int price;
  private int baseGP;
  private List<Integer> evolutionIds;
  private List<WeatherImmunity> weatherImmunities;
  private Skills skillIds;
  private String illustratorId;
  private String cvId;
  private String storyId;
  private CharacterType characterType;
  private List<Acquire> acquire;
  private boolean legend;
  private boolean special;
  private int season;
  private List<Integer> skins;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readInt32();
    imageId = br.readInt32();
    // soundId
    br.readInt32();
    // number
    br.readInt32();
    chainId = br.readInt32();
    nameId = br.readString();
    value = br.readInt32();
    gender = Gender.valueOf(br.readInt32());
    element = Element.valueOf(br.readInt32());

    stoneElements = new ArrayList<Element>();
    for (int i = 0; i < 4; i++) {
      Element element = Element.valueOf(br.readInt32());
      if (!element.equals(Element.UNKNOWN)) {
        stoneElements.add(element);
      }
    }

    // position
    br.readInt32();

    positions = new ArrayList<Position>();
    for (int i = 0; i < 4; i++) {
      Position position = Position.valueOf(br.readInt32());
      if (!position.equals(Position.UNKNOWN)) {
        positions.add(position);
      }
    }

    type = PlayerType.valueOf(br.readInt32());
    teamId = br.readInt32();
    cost = br.readInt32();

    power = new Stat();
    technique = new Stat();
    vitality = new Stat();
    speed = new Stat();

    power.min = br.readInt32();
    technique.min = br.readInt32();
    vitality.min = br.readInt32();
    speed.min = br.readInt32();

    power.max = br.readInt32();
    technique.max = br.readInt32();
    vitality.max = br.readInt32();
    speed.max = br.readInt32();

    price = br.readInt32();
    // noneElemental
    br.readInt32();
    // growType
    br.readSingle();
    // baseExp
    br.readInt32();
    baseGP = br.readInt32();
    // bonusRate
    br.readInt32();

    evolutionIds = new ArrayList<Integer>();
    for (int i = 0; i < 2; i++) {
      int evolution = br.readInt32();
      if (evolution != 0) {
        evolutionIds.add(evolution);
      }
    }

    weatherImmunities = new ArrayList<WeatherImmunity>();
    for (int i = 0; i < 2; i++) {
      WeatherImmunity weatherImmunity = WeatherImmunity.valueOf(br.readInt32());
      if (!weatherImmunity.equals(WeatherImmunity.UNKNOWN)) {
        weatherImmunities.add(weatherImmunity);
      }
    }

    skillIds = new Skills();
    skillIds.ace = br.readInt32();
    skillIds.active = br.readInt32();
    skillIds.passives = new ArrayList<Integer>();
    for (int i = 0; i < 3; i++) {
      int skill = br.readInt32();
      if (skill != 0) {
        skillIds.passives.add(skill);
      }
    }

    illustratorId = br.readString();
    cvId = br.readString();
    storyId = br.readString();
    // season
    br.readInt32();
    // rotation
    br.readInt32();
    characterType = CharacterType.valueOf(br.readInt32());

    acquire = new ArrayList<Acquire>();
    for (String s : br.readString().split("\\s*,\\s*")) {
      try {
        acquire.add(Acquire.valueOf(Integer.parseInt(s)));
      } catch (NumberFormatException e) {}
    }

    // _7
    br.readInt32();
    legend = br.readInt32() != 0;

    // eventTypes
    for (int i = 0; i < 4; i++) {
      br.readInt32();
    }

    // taq
    br.readInt32();
    special = br.readInt32() != 0;
    season = br.readInt32();

    skins = new ArrayList<Integer>();
    for (int i = 0; i < 3; i++) {
      int skin = br.readInt32();
      if (skin != 0) {
        skins.add(skin);
      }
    }

    return true;
  }

  public int getChainId() {
    return chainId;
  }

  public CharacterChain getChain() {
    return chain;
  }

  public void setChain(CharacterChain chain) {
    this.chain = chain;
  }

  class Stat {
    private int min;
    private int max;
  }

  class Skills {
    private int ace;
    private int active;
    private List<Integer> passives;
  }
}
