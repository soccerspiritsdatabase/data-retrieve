package paek.kevin.ssdata.comparators;

import paek.kevin.ssdata.models.Character;

import java.util.Comparator;

public class TechniqueStatComparator implements Comparator<Character> {
  public int compare(Character a, Character b) {
    if (a.getTechnique().get("max") < b.getTechnique().get("max")) return 1;
    if (a.getTechnique().get("max") > b.getTechnique().get("max")) return -1;
    return 0;
  }
}
