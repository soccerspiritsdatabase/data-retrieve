package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum Element {
  @SerializedName("Ardor")
  ARDOR,

  @SerializedName("Thunder")
  THUNDER,

  @SerializedName("Whirlwind")
  WHIRLWIND,

  @SerializedName("Light")
  LIGHT,

  @SerializedName("Dark")
  DARK,

  UNKNOWN;

  public static Element valueOf(int value) {
    switch (value) {
      case 1:
        return Element.ARDOR;
      case 2:
        return Element.THUNDER;
      case 3:
        return Element.WHIRLWIND;
      case 4:
        return Element.LIGHT;
      case 5:
        return Element.DARK;
      default:
        return Element.UNKNOWN;
    }
  }
}
