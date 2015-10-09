package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum Acquire {
  @SerializedName("Player")
  PLAYER_DRAW,

  @SerializedName("Story")
  STORY,

  @SerializedName("Scout")
  SCOUT,

  @SerializedName("Space & Time")
  SPACE_TIME,

  UNKNOWN;

  public static Acquire valueOf(int value) {
    switch (value) {
      case 1:
        return Acquire.PLAYER_DRAW;
      case 2:
        return Acquire.STORY;
      case 3:
        return Acquire.SCOUT;
      case 4:
        return Acquire.SPACE_TIME;
      default:
        return Acquire.UNKNOWN;
    }
  }
}
