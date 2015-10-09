package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum Gender {
  @SerializedName("Male")
  MALE,

  @SerializedName("Female")
  FEMALE,

  @SerializedName("Unknown")
  UNKNOWN;

  public static Gender valueOf(int value) {
    switch (value) {
      case 1:
        return Gender.MALE;
      case 2:
        return Gender.FEMALE;
      default:
        return Gender.UNKNOWN;
    }
  }
}
