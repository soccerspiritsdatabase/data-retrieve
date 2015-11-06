package paek.kevin.ssdata.comparators;

import paek.kevin.ssdata.models.Character;

import java.util.Comparator;

public class SpeedStatComparator implements Comparator<Character> {
  public int compare(Character a, Character b) {
    if (a.getSpeed().get("max") < b.getSpeed().get("max")) return 1;
    if (a.getSpeed().get("max") > b.getSpeed().get("max")) return -1;
    return 0;
  }
}
