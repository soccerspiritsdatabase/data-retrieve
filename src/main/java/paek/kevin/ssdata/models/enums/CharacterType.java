package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum CharacterType {
  @SerializedName("Player")
  PLAYER,

  @SerializedName("Manager")
  MANAGER,

  @SerializedName("Other")
  OTHER,

  UNKNOWN;

  public static CharacterType valueOf(int value) {
    switch (value) {
      case 1:
        return CharacterType.PLAYER;
      case 2:
        return CharacterType.MANAGER;
      case 3:
        return CharacterType.OTHER;
      default:
        return CharacterType.UNKNOWN;
    }
  }
}
