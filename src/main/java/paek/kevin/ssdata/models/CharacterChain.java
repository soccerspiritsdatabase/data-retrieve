package paek.kevin.ssdata.models;

import paek.kevin.ssdata.models.enums.CharacterChainEffect;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CharacterChain extends Model {

  private List<Chain> chains;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = String.valueOf(br.readInt32());
    // characterId
    br.readInt32();

    chains = new ArrayList<Chain>();
    for (int i = 0; i < 4; i++) {
      Chain chain = new Chain();
      // target
      br.readInt32();
      chain.effect = CharacterChainEffect.valueOf(br.readInt32());
      for (int j = 0; j < 5; j++) {
        int character = br.readInt32();
        if (character != 0) {
          chain.characterIds.add(character);
        }
      }
      if (chain.characterIds.size() > 0) {
        chains.add(chain);
      }
    }

    return true;
  }

  public class Chain {
    private CharacterChainEffect effect;
    private List<Integer> characterIds = new ArrayList<Integer>();
  }
}
