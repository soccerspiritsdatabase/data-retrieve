package paek.kevin.ssdata.comparators;

import paek.kevin.ssdata.models.Character;

import java.util.Comparator;

public class PowerStatComparator implements Comparator<Character> {
  public int compare(Character a, Character b) {
    if (a.getPower().get("max") < b.getPower().get("max")) return 1;
    if (a.getPower().get("max") > b.getPower().get("max")) return -1;
    return 0;
  }
}
