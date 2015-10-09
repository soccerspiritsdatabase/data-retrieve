package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.*;

public class Model {

  public static <T extends Model> Map<Object, T> toMap(List<T> models) {
    Map<Object, T> map = new TreeMap<Object, T>();
    for (T model : models) {
      map.put(model.id, model);
    }
    return map;
  }

  public static <T extends Model> Map<Object, T> merge(Map<Object, T>... maps) {
    Map<Object, T> map = new TreeMap<Object, T>();
    for (Map<Object, T> aMap : maps) {
      for (Map.Entry<Object, T> entry : aMap.entrySet()) {
        map.put(entry.getKey(), entry.getValue());
      }
    }
    return map;
  }

  transient protected Comparable id;

  public boolean read(BinaryReaderDotNet br) throws IOException {
    return true;
  }

  public Comparable getId() {
    return id;
  }

  public class ModelComparator implements Comparator<Model> {

    public int compare(Model m1, Model m2) {
      return m1.id.compareTo(m2.id);
    }
  }
}
