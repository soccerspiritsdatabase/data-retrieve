package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.Element;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterCollection extends Model {

  private int order;
  private String teamNameId;
  private Element element;
  private List<List<Integer>> characterGroups;
  private int reward;
  private int rewardRate;
  private String teamStoryId;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readInt32();
    order = br.readInt32();
    teamNameId = br.readString();
    element = Element.valueOf(br.readInt32());

    characterGroups = new ArrayList<List<Integer>>();
    for (int i = 0; i < 28; i++) {
      List<Integer> characterGroup = new ArrayList<Integer>();
      for (int j = 0; j < 2; j++) {
        int character = br.readInt32();
        if (character != 0) {
          characterGroup.add(character);
        }
      }
      if (characterGroup.size() > 0) {
        characterGroups.add(characterGroup);
      }
    }

    reward = br.readInt32();
    rewardRate = br.readInt32();
    teamStoryId = br.readString();

    return true;
  }

  public int getOrder() {
    return order;
  }

  public String getTeamNameId() {
    return teamNameId;
  }

  public Element getElement() {
    return element;
  }

  public List<List<Integer>> getCharacterGroups() {
    return characterGroups;
  }

  public int getReward() {
    return reward;
  }

  public int getRewardRate() {
    return rewardRate;
  }

  public String getTeamStoryId() {
    return teamStoryId;
  }
}
