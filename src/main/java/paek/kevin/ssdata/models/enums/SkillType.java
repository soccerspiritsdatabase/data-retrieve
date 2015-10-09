package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum SkillType {
  @SerializedName("Nothing")
  NOTHING,

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
        return SkillType.NOTHING;
      case 2:
        return SkillType.ACTIVE;
      case 3:
        return SkillType.PASSIVE;
      case 4:
        return SkillType.ACE;
      case 5:
        return SkillType.ITEM;
      case 6:
        return SkillType.WEATHER;
      case 7:
        return SkillType.JOB;
      case 8:
        return SkillType.LOVE;
      case 9:
        return SkillType.SYSTEM;
      default:
        return SkillType.UNKNOWN;
    }
  }
}
