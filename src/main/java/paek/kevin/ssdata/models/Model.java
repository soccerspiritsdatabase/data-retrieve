package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

  public static <T extends Model> Map<String, T> toMap(List<T> models) {
    Map<String, T> map = new HashMap<String, T>();
    for (T model : models) {
      map.put(model.id, model);
    }
    return map;
  }

  public static <T extends Model> Map<String, T> merge(Map<String, T>... maps) {
    Map<String, T> map = new HashMap<String, T>();
    for (Map<String, T> aMap : maps) {
      for (Map.Entry<String, T> entry : aMap.entrySet()) {
        map.put(entry.getKey(), entry.getValue());
      }
    }
    return map;
  }

  protected String id;

  public boolean read(BinaryReaderDotNet br) throws IOException {
    return true;
  }
}
