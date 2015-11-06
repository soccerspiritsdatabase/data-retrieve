package paek.kevin.ssdata.comparators;

import paek.kevin.ssdata.models.Character;

import java.util.Comparator;

public class VitalityStatComparator implements Comparator<Character> {
  public int compare(Character a, Character b) {
    if (a.getVitality().get("max") < b.getVitality().get("max")) return 1;
    if (a.getVitality().get("max") > b.getVitality().get("max")) return -1;
    return 0;
  }
}
