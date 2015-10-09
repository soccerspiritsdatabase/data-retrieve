package paek.kevin.ssdata.models.enums;

public enum Position {
  LWF,
  ST,
  SS,
  RWF,
  LWM,
  CAM,
  RWM,
  LM,
  CM,
  RM,
  CDM,
  LWB,
  RWB,
  LB,
  CB,
  RB,
  GK,
  UNKNOWN;

  public static Position valueOf(int value) {
    switch (value) {
      case 1:
        return Position.LWF;
      case 2:
        return Position.ST;
      case 3:
        return Position.SS;
      case 4:
        return Position.RWF;
      case 5:
        return Position.LWM;
      case 6:
        return Position.CAM;
      case 7:
        return Position.RWM;
      case 8:
        return Position.LM;
      case 9:
        return Position.CM;
      case 10:
        return Position.RM;
      case 11:
        return Position.CDM;
      case 12:
        return Position.LWB;
      case 13:
        return Position.RWB;
      case 14:
        return Position.LB;
      case 15:
        return Position.CB;
      case 16:
        return Position.RB;
      case 17:
        return Position.GK;
      default:
        return Position.UNKNOWN;
    }
  }
}
