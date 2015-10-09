package paek.kevin.ssdata.models.enums;

import com.google.gson.annotations.SerializedName;

public enum WeatherImmunity {
  @SerializedName("Rainy Weather")
  RAINY_WEATHER,

  @SerializedName("Piercing Wind")
  PIERCING_WIND,

  @SerializedName("Soul Grave")
  SOUL_GRAVE,

  UNKNOWN;

  public static WeatherImmunity valueOf(int value) {
    switch (value) {
      case 2:
        return WeatherImmunity.RAINY_WEATHER;
      case 4:
        return WeatherImmunity.PIERCING_WIND;
      case 5:
        return WeatherImmunity.SOUL_GRAVE;
      default:
        return WeatherImmunity.UNKNOWN;
    }
  }
}
