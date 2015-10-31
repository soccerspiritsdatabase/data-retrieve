package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum SpiritStoneZodiac {
  @SerializedName("Aries")
  ARIES,

  @SerializedName("Taurus")
  TAURUS,

  @SerializedName("Gemini")
  GEMINI,

  @SerializedName("Cancer")
  CANCER,

  @SerializedName("Leo")
  LEO,

  @SerializedName("Virgo")
  VIRGO,

  @SerializedName("Libra")
  LIBRA,

  @SerializedName("Scorpio")
  SCORPIO,

  @SerializedName("Sagittarius")
  SAGITTARIUS,

  @SerializedName("Capricorn")
  CAPRICORN,

  @SerializedName("Aquarius")
  AQUARIUS,

  @SerializedName("Pisces")
  PISCES,

  @SerializedName("")
  UNKNOWN;

  public static SpiritStoneZodiac valueOf(int value) {
    switch (value) {
      case 1:
        return SpiritStoneZodiac.ARIES;
      case 2:
        return SpiritStoneZodiac.TAURUS;
      case 3:
        return SpiritStoneZodiac.GEMINI;
      case 4:
        return SpiritStoneZodiac.CANCER;
      case 5:
        return SpiritStoneZodiac.LEO;
      case 6:
        return SpiritStoneZodiac.VIRGO;
      case 7:
        return SpiritStoneZodiac.LIBRA;
      case 8:
        return SpiritStoneZodiac.SCORPIO;
      case 9:
        return SpiritStoneZodiac.SAGITTARIUS;
      case 10:
        return SpiritStoneZodiac.CAPRICORN;
      case 11:
        return SpiritStoneZodiac.AQUARIUS;
      case 12:
        return SpiritStoneZodiac.PISCES;
      default:
        return SpiritStoneZodiac.UNKNOWN;
    }
  }
}
