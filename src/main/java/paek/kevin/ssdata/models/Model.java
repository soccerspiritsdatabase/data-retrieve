package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model {

  public static <T extends Model> Map<Object, T> toMap(List<T> models) {
    Map<Object, T> map = new HashMap<Object, T>();
    for (T model : models) {
      map.put(model.id, model);
    }
    return map;
  }

  public static <T extends Model> Map<Object, T> merge(Map<Object, T>... maps) {
    Map<Object, T> map = new HashMap<Object, T>();
    for (Map<Object, T> aMap : maps) {
      for (Map.Entry<Object, T> entry : aMap.entrySet()) {
        map.put(entry.getKey(), entry.getValue());
      }
    }
    return map;
  }

  protected Object id;

  public boolean read(BinaryReaderDotNet br) throws IOException {
    return true;
  }
}
