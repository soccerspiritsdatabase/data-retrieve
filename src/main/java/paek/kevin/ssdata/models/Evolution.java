package paek.kevin.ssdata.models;

import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Evolution extends Model {

  private List<Material> materials;
  private int result;
  private int cost;

  @Override
  public boolean read(BinaryReaderDotNet br) throws IOException {
    this.id = String.valueOf(br.readInt32());

    materials = new ArrayList<Material>();
    for (int i = 0; i < 4; i++) {
      Material material = new Material();
      material.count = br.readInt32();
      material.character = br.readInt32();
      if (material.count != 0) {
        materials.add(material);
      }
    }

    result = br.readInt32();
    cost = br.readInt32();

    return true;
  }

  public class Material {
    private int count;
    private int character;
  }
}
