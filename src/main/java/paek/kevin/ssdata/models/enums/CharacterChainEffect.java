package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum CharacterChainEffect {
  @SerializedName("Affection")
  AFFECTION,

  @SerializedName("Friendship")
  FRIENDSHIP,

  @SerializedName("Rival")
  RIVAL,

  @SerializedName("Nemesis")
  NEMESIS,

  UNKNOWN;

  public static CharacterChainEffect valueOf(int value) {
    switch (value) {
      case 1:
        return CharacterChainEffect.AFFECTION;
      case 2:
        return CharacterChainEffect.FRIENDSHIP;
      case 3:
        return CharacterChainEffect.RIVAL;
      case 4:
        return CharacterChainEffect.NEMESIS;
      default:
        return CharacterChainEffect.UNKNOWN;
    }
  }
}
