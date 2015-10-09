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
import java.util.*;

public class ParseDBFiles implements Worker {

  private final Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

  public void doWork() {
    System.out.println("----------------------------------------");
    System.out.println("Parsing models");
    Map<Object, Text> texts = Model.merge(
            parseModel("Texts", Text.class),
            parseModel("TextCharacters", Text.class)
    );
    Map<Object, Evolution> evolutions = parseModel("Evolutions", Evolution.class);
    Map<Object, CharacterCollection> characterCollections = parseModel("Collections", CharacterCollection.class);
    Map<Object, CharacterChain> characterChains = parseModel("CharacterChs", CharacterChain.class);
    Map<Object, paek.kevin.ssdata.models.Character> characters = parseModel("Characters", paek.kevin.ssdata.models.Character.class);
    Map<Object, SpiritStone> spiritStones = parseModel("Items", SpiritStone.class);
    Map<Object, SpiritStoneCollection> spiritStoneCollections = parseModel("ItemCollections", SpiritStoneCollection.class);
    Map<Object, Skill> skills = parseModel("Skills", Skill.class);

    System.out.println("Combining references");
    // get valid characters based on CharacterCollections
    Set<Integer> validCharacterIds = new LinkedHashSet<Integer>();
    for (CharacterCollection characterCollection : characterCollections.values()) {
      for (List<Integer> ids : characterCollection.getCharacterGroups()) {
        for (Integer id : ids) {
          validCharacterIds.add(id);
        }
      }
    }

    Set<Integer> validSkillIds = new LinkedHashSet<Integer>();

    for (Iterator<Map.Entry<Object, Character>> iterator = characters.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Character> entry = iterator.next();
      Character character = entry.getValue();
      // filter out invalid characters
      if (validCharacterIds.contains(character.getId())) {
        // link fields
        character.setName(texts.get(character.getNameId()));
        character.setChain(characterChains.get(character.getChainId()));
        character.setIllustrator(texts.get(character.getIllustratorId()));
        character.setCv(texts.get(character.getCvId()));
        character.setStory(texts.get(character.getStoryId()));
        character.setEvolution(evolutions.get(character.getEvolutionId()));
        {
          Character.Skills skillIds = character.getSkills();
          skillIds.setAce(skills.get(skillIds.getAceId()));
          validSkillIds.add(skillIds.getAceId());
          skillIds.setActive(skills.get(skillIds.getActiveId()));
          validSkillIds.add(skillIds.getActiveId());
          skillIds.setPassives(new ArrayList<Skill>());
          for (Integer id : skillIds.getPassiveIds()) {
            skillIds.getPassives().add(skills.get(id));
            validSkillIds.add(id);
          }
        }
      } else {
        // remove invalid character
        iterator.remove();
      }
    }

    for (Iterator<Map.Entry<Object, SpiritStone>> iterator = spiritStones.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, SpiritStone> entry = iterator.next();
      SpiritStone spiritStone = entry.getValue();
      // link fields
      spiritStone.setName(texts.get(spiritStone.getNameId()));
      spiritStone.setSkills(new ArrayList<Skill>());
      for (Integer id : spiritStone.getSkillIds()) {
        spiritStone.getSkills().add(skills.get(id));
        validSkillIds.add(id);
      }
    }

    for (Iterator<Map.Entry<Object, Skill>> iterator = skills.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Skill> entry = iterator.next();
      Skill skill = entry.getValue();
      if (validSkillIds.contains(skill.getId())) {
        // link fields
        skill.setName(texts.get(skill.getNameId()));
        skill.setDescription(texts.get(skill.getDescId()));
      } else {
        iterator.remove();
      }
    }

    System.out.println("Writing json files");
    try {
      Files.createDirectories(Config.JSON_FILES_DIR);
      Files.write(Config.JSON_FILES_DIR.resolve("texts.json"), Arrays.asList(gson.toJson(texts)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("evolutions.json"), Arrays.asList(gson.toJson(evolutions)));
      Files.write(Config.JSON_FILES_DIR.resolve("characterCollections.json"), Arrays.asList(gson.toJson(characterCollections)));
      Files.write(Config.JSON_FILES_DIR.resolve("characterChains.json"), Arrays.asList(gson.toJson(characterChains)));
      Files.write(Config.JSON_FILES_DIR.resolve("characters.json"), Arrays.asList(gson.toJson(characters)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("spiritStones.json"), Arrays.asList(gson.toJson(spiritStones)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("spiritStoneCollections.json"), Arrays.asList(gson.toJson(spiritStoneCollections)));
      Files.write(Config.JSON_FILES_DIR.resolve("skills.json"), Arrays.asList(gson.toJson(skills)), Charsets.ISO_8859_1);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Failed to save json files"), e);
    }
  }

  private <T extends Model> Map<Object, T> parseModel(String fileName, Class<T> modelClass) {
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
