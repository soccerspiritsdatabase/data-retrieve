package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerCollection extends Model {

  private int order;
  private String teamNameId;
  private int color;
  private List<Player> players;
  private int reward;
  private int rewardRate;
  private String teamStoryId;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = br.readInt32();
    order = br.readInt32();
    teamNameId = br.readString();
    color = br.readInt32();

    players = new ArrayList<Player>();
    for (int i = 0; i < 28; i++) {
      Player player = new Player();
      player.normal = br.readInt32();
      player.evolution = br.readInt32();
      if (player.normal != 0 || player.evolution != 0) {
        players.add(new Player());
      }
    }

    reward = br.readInt32();
    rewardRate = br.readInt32();
    teamStoryId = br.readString();

    return true;
  }

  public class Player {
    private int normal;
    private int evolution;
  }
}
