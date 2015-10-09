package paek.kevin.ssdata.work;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.Charsets;
import paek.kevin.ssdata.Config;
import paek.kevin.ssdata.models.*;
import paek.kevin.ssdata.models.Character;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ParseDBFiles implements Worker {

  private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

  public void doWork() {
    System.out.println("----------------------------------------");
    System.out.println("Parsing models");
    boolean saveFiles = true;
    Map<String, Text> texts = Model.merge(
            parseModel("Texts", Text.class),
            parseModel("TextCharacters", Text.class)
    );
    Map<String, Evolution> evolutions = parseModel("Evolutions", Evolution.class);
    Map<String, PlayerCollection> playerCollections = parseModel("Collections", PlayerCollection.class);
    Map<String, CharacterChain> characterChains = parseModel("CharacterChs", CharacterChain.class);
    Map<String, Character> characters = parseModel("Characters", Character.class);
    Map<String, SpiritStone> spiritStones = parseModel("Items", SpiritStone.class);
    Map<String, SpiritStoneCollection> spiritStoneCollections = parseModel("ItemCollections", SpiritStoneCollection.class);
    Map<String, Skill> skills = parseModel("Skills", Skill.class);

    System.out.println("Combining references");
    for (Character character : characters.values()) {
      CharacterChain chain = characterChains.get(String.valueOf(character.getChainId()));
      character.setChain(chain);
    }

    System.out.println("Writing json files");
    try {
      Files.createDirectories(Config.JSON_FILES_DIR);
      Files.write(Config.JSON_FILES_DIR.resolve("texts.json"), Arrays.asList(gson.toJson(texts)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("evolutions.json"), Arrays.asList(gson.toJson(evolutions)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("playerCollections.json"), Arrays.asList(gson.toJson(playerCollections)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("characterChains.json"), Arrays.asList(gson.toJson(characterChains)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("characters.json"), Arrays.asList(gson.toJson(characters)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("spiritStones.json"), Arrays.asList(gson.toJson(spiritStones)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("spiritStoneCollections.json"), Arrays.asList(gson.toJson(spiritStoneCollections)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("skills.json"), Arrays.asList(gson.toJson(skills)), Charsets.ISO_8859_1);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Failed to save json files"), e);
    }
  }

  private <T extends Model> Map<String, T> parseModel(String fileName, Class<T> modelClass) {
    System.out.println(fileName);
    List<T> modelsList = new ArrayList<T>();
    try {
      System.out.println("\tParsing");
      BinaryReaderDotNet reader = new BinaryReaderDotNet(Files.newInputStream(Config.RAW_DB_FILES_DIR.resolve(fileName)), Charsets.ISO_8859_1);
      Constructor<T> constructor = modelClass.getConstructor();
      while (reader.available() > 0) {
        T model = constructor.newInstance();
        boolean status = model.read(reader);
        if (status) {
          modelsList.add(model);
        }
      }
    } catch (Exception e) {
      throw new RuntimeException(String.format("Failed to parse model (%s)", fileName), e);
    }

    System.out.println("\tDone");
    return Model.toMap(modelsList);
  }
}
