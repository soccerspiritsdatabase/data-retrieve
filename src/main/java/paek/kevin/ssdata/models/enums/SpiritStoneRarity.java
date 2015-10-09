package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum SpiritStoneRarity {
  @SerializedName("Common")
  COMMON,

  @SerializedName("Rare")
  RARE,

  @SerializedName("Unique")
  UNIQUE,

  UNKNOWN;

  public static SpiritStoneRarity valueOf(int value) {
    switch (value) {
      case 1:
        return SpiritStoneRarity.COMMON;
      case 2:
        return SpiritStoneRarity.RARE;
      case 3:
        return SpiritStoneRarity.UNIQUE;
      default:
        return SpiritStoneRarity.UNKNOWN;
    }
  }
}
