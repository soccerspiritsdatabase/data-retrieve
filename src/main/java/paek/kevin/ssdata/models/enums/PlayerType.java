package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum PlayerType {
  @SerializedName("Defender")
  DEFENDER,

  @SerializedName("Attacker")
  ATTACKER,

  @SerializedName("Leader")
  LEADER,

  @SerializedName("Striker")
  STRIKER,

  @SerializedName("Assist")
  ASSIST;

  public static PlayerType valueOf(int value) {
    switch (value) {
      case 1:
        return PlayerType.DEFENDER;
      case 2:
        return PlayerType.ATTACKER;
      case 3:
        return PlayerType.LEADER;
      case 4:
        return PlayerType.STRIKER;
      case 5:
        return PlayerType.ASSIST;
      default:
        return null;
    }
  }
}
