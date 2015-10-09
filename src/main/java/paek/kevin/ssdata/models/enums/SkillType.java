package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum SkillType {
  @SerializedName("Active")
  ACTIVE,

  @SerializedName("Passive")
  PASSIVE,

  @SerializedName("Ace")
  ACE,

  @SerializedName("Item")
  ITEM,

  @SerializedName("Weather")
  WEATHER,

  @SerializedName("Job")
  JOB,

  @SerializedName("Love")
  LOVE,

  @SerializedName("System")
  SYSTEM,

  UNKNOWN;

  public static SkillType valueOf(int value) {
    switch (value) {
      case 1:
        return SkillType.ACTIVE;
      case 2:
        return SkillType.PASSIVE;
      case 3:
        return SkillType.ACE;
      case 4:
        return SkillType.ITEM;
      case 5:
        return SkillType.WEATHER;
      case 6:
        return SkillType.JOB;
      case 7:
        return SkillType.LOVE;
      case 8:
        return SkillType.SYSTEM;
      default:
        return SkillType.UNKNOWN;
    }
  }
}
