package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.*;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Character extends Model {

  private int imageId;
  transient private int chainId;
  transient private String nameId;
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
  transient private int evolutionId;
  private List<WeatherImmunity> weatherImmunities;
  private Skills skills;
  transient private String illustratorId;
  transient private String cvId;
  transient private String storyId;
  private CharacterType characterType;
  private List<Acquire> acquire;
  private boolean legend;
  private boolean special;
  private int season;
  private List<Integer> skins;

  private CharacterChain chain;
  private Text name;
  private Text illustrator;
  private Text cv;
  private Text story;
  private Evolution evolution;

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

    evolutionId = br.readInt32();
    // evolutionId2
    br.readInt32();

    weatherImmunities = new ArrayList<WeatherImmunity>();
    for (int i = 0; i < 2; i++) {
      WeatherImmunity weatherImmunity = WeatherImmunity.valueOf(br.readInt32());
      if (!weatherImmunity.equals(WeatherImmunity.UNKNOWN)) {
        weatherImmunities.add(weatherImmunity);
      }
    }

    skills = new Skills();
    skills.aceId = br.readInt32();
    skills.activeId = br.readInt32();
    skills.passiveIds = new ArrayList<Integer>();
    for (int i = 0; i < 3; i++) {
      int skill = br.readInt32();
      if (skill != 0) {
        skills.passiveIds.add(skill);
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

  public int getImageId() {
    return imageId;
  }

  public int getChainId() {
    return chainId;
  }

  public String getNameId() {
    return nameId;
  }

  public int getValue() {
    return value;
  }

  public Gender getGender() {
    return gender;
  }

  public Element getElement() {
    return element;
  }

  public List<Element> getStoneElements() {
    return stoneElements;
  }

  public List<Position> getPositions() {
    return positions;
  }

  public PlayerType getType() {
    return type;
  }

  public int getTeamId() {
    return teamId;
  }

  public int getCost() {
    return cost;
  }

  public Stat getPower() {
    return power;
  }

  public Stat getTechnique() {
    return technique;
  }

  public Stat getVitality() {
    return vitality;
  }

  public Stat getSpeed() {
    return speed;
  }

  public int getPrice() {
    return price;
  }

  public int getBaseGP() {
    return baseGP;
  }

  public int getEvolutionId() {
    return evolutionId;
  }

  public List<WeatherImmunity> getWeatherImmunities() {
    return weatherImmunities;
  }

  public Skills getSkills() {
    return skills;
  }

  public String getIllustratorId() {
    return illustratorId;
  }

  public String getCvId() {
    return cvId;
  }

  public String getStoryId() {
    return storyId;
  }

  public CharacterType getCharacterType() {
    return characterType;
  }

  public List<Acquire> getAcquire() {
    return acquire;
  }

  public boolean isLegend() {
    return legend;
  }

  public boolean isSpecial() {
    return special;
  }

  public int getSeason() {
    return season;
  }

  public List<Integer> getSkins() {
    return skins;
  }

  public CharacterChain getChain() {
    return chain;
  }

  public void setChain(CharacterChain chain) {
    this.chain = chain;
  }

  public Text getName() {
    return name;
  }

  public void setName(Text name) {
    this.name = name;
  }

  public Text getIllustrator() {
    return illustrator;
  }

  public void setIllustrator(Text illustrator) {
    this.illustrator = illustrator;
  }

  public Text getCv() {
    return cv;
  }

  public void setCv(Text cv) {
    this.cv = cv;
  }

  public Text getStory() {
    return story;
  }

  public void setStory(Text story) {
    this.story = story;
  }

  public Evolution getEvolution() {
    return evolution;
  }

  public void setEvolution(Evolution evolution) {
    this.evolution = evolution;
  }

  class Stat {
    private int min;
    private int max;
  }

  public class Skills {
    transient private int aceId;
    transient private int activeId;
    transient private List<Integer> passiveIds;

    private Skill ace;
    private Skill active;
    private List<Skill> passives;

    public int getAceId() {
      return aceId;
    }

    public int getActiveId() {
      return activeId;
    }

    public List<Integer> getPassiveIds() {
      return passiveIds;
    }

    public Skill getAce() {
      return ace;
    }

    public void setAce(Skill ace) {
      this.ace = ace;
    }

    public Skill getActive() {
      return active;
    }

    public void setActive(Skill active) {
      this.active = active;
    }

    public List<Skill> getPassives() {
      return passives;
    }

    public void setPassives(List<Skill> passives) {
      this.passives = passives;
    }
  }
}
