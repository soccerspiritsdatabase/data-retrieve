package paek.kevin.ssdata.work;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.Charsets;
import paek.kevin.ssdata.Config;
import paek.kevin.ssdata.comparators.PowerStatComparator;
import paek.kevin.ssdata.comparators.SpeedStatComparator;
import paek.kevin.ssdata.comparators.TechniqueStatComparator;
import paek.kevin.ssdata.comparators.VitalityStatComparator;
import paek.kevin.ssdata.models.*;
import paek.kevin.ssdata.models.Character;
import paek.kevin.ssdata.models.enums.CharacterType;
import paek.kevin.ssdata.models.enums.SkillType;
import paek.kevin.ssdata.utils.BinaryReaderDotNet;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.*;

public class ParseDBFiles {

  private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

  public static Map<Object, Character> process() {
    System.out.println("----------------------------------------");
    System.out.println("Parsing models");
    Map<Object, Text> texts = Model.merge(
            parseModel("Texts", Text.class),
            parseModel("TextCharacters", Text.class)
    );
    Map<Object, Evolution> evolutions = parseModel("Evolutions", Evolution.class);
    Map<Object, CharacterCollection> characterCollections = parseModel("Collections", CharacterCollection.class);
    Map<Object, CharacterChain> characterChains = parseModel("CharacterChs", CharacterChain.class);
    Map<Object, Character> characters = parseModel("Characters", Character.class);
    Map<Object, SpiritStone> spiritStones = parseModel("Items", SpiritStone.class);
    Map<Object, SpiritStoneCollection> spiritStoneCollections = parseModel("ItemCollections", SpiritStoneCollection.class);
    Map<Object, Skill> skills = parseModel("Skills", Skill.class);

    System.out.println("Combining references");

    System.out.println("\tCalculate set of valid characters");
    Set<Integer> charactersInCollection = new LinkedHashSet<Integer>();
    for (CharacterCollection characterCollection : characterCollections.values()) {
      for (List<Integer> ids : characterCollection.getCharacterGroups()) {
        for (Integer id : ids) {
          charactersInCollection.add(id);
        }
      }
    }
    // remove characters not in collection
    for (Iterator<Map.Entry<Object, Character>> iterator = characters.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Character> entry = iterator.next();
      Character character = entry.getValue();
      if (character.getValue() < 2 || !charactersInCollection.contains(character.getId())) {
        iterator.remove();
      }
    }

    System.out.println("\tJoin Character fields");
    for (Iterator<Map.Entry<Object, Character>> iterator = characters.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Character> entry = iterator.next();
      Character character = entry.getValue();
      character.setName(texts.get(character.getNameId()));
      character.setChain(characterChains.get(character.getChainId()));
      character.setIllustrator(texts.get(character.getIllustratorId()));
      character.setCv(texts.get(character.getCvId()));
      character.setStory(texts.get(character.getStoryId()));

      Evolution evolution = evolutions.get(character.getEvolution());
      if (evolution != null) {
        evolution.setPreResult(Integer.parseInt(String.valueOf(character.getId())));
      }

      character.setIsPlayer(CharacterType.PLAYER.equals(character.getCharacterType()));
      character.setIsManager(CharacterType.MANAGER.equals(character.getCharacterType()));
      character.setIsOther(CharacterType.OTHER.equals(character.getCharacterType()));
    }

    System.out.println("\tCalculate stat rankings");
    calculateStatRankings(characters);

    System.out.println("\tJoin Spirit Stone fields");
    for (Iterator<Map.Entry<Object, SpiritStone>> iterator = spiritStones.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, SpiritStone> entry = iterator.next();
      SpiritStone spiritStone = entry.getValue();
      spiritStone.setName(texts.get(spiritStone.getNameId()));
    }

    System.out.println("\tCalculate set of valid skills");
    Set<Integer> validSkillIds = new LinkedHashSet<Integer>();
    // skills from characters
    for (Iterator<Map.Entry<Object, Character>> iterator = characters.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Character> entry = iterator.next();
      Character character = entry.getValue();

      Character.Skills characterSkills = character.getSkills();
      validSkillIds.add(characterSkills.getAce());
      validSkillIds.add(characterSkills.getActive());
      for (int id : characterSkills.getPassives()) {
        validSkillIds.add(id);
      }
    }
    // skills from spirit stones
    for (Iterator<Map.Entry<Object, SpiritStone>> iterator = spiritStones.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, SpiritStone> entry = iterator.next();
      SpiritStone spiritStone = entry.getValue();
      for (Integer id : spiritStone.getSkills()) {
        validSkillIds.add(id);
      }
    }

    // remove invalid skills
    for (Iterator<Map.Entry<Object, Skill>> iterator = skills.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Skill> entry = iterator.next();
      Skill skill = entry.getValue();
      if (!validSkillIds.contains(skill.getId())) {
        iterator.remove();
      }
    }

    System.out.println("\tJoin Skill fields");
    for (Iterator<Map.Entry<Object, Skill>> iterator = skills.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<Object, Skill> entry = iterator.next();
      Skill skill = entry.getValue();
      if (!SkillType.ITEM.equals(skill.getType())) {
        skill.setName(texts.get(skill.getNameId()));
      }
      skill.setDescription(texts.get(skill.getDescId()));
    }

    System.out.println("Writing json files");
    try {
      Files.createDirectories(Config.JSON_FILES_DIR);
      Files.write(Config.JSON_FILES_DIR.resolve("evolutions.json"), Arrays.asList(gson.toJson(evolutions)));
      Files.write(Config.JSON_FILES_DIR.resolve("characterCollections.json"), Arrays.asList(gson.toJson(characterCollections)));
      Files.write(Config.JSON_FILES_DIR.resolve("characters.json"), Arrays.asList(gson.toJson(characters)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("spiritStones.json"), Arrays.asList(gson.toJson(spiritStones)), Charsets.ISO_8859_1);
      Files.write(Config.JSON_FILES_DIR.resolve("spiritStoneCollections.json"), Arrays.asList(gson.toJson(spiritStoneCollections)));
      Files.write(Config.JSON_FILES_DIR.resolve("skills.json"), Arrays.asList(gson.toJson(skills)), Charsets.ISO_8859_1);
    } catch (IOException e) {
      throw new RuntimeException(String.format("Failed to save json files"), e);
    }

    return characters;
  }

  private static <T extends Model> Map<Object, T> parseModel(String fileName, Class<T> modelClass) {
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

  private static void calculateStatRankings(Map<Object, Character> characterMap) {
    List<Character> characters = new ArrayList<Character>();
    for (Character character : characterMap.values()) {
      if (character.isPlayer() && character.getEvolution() == 0) {
        characters.add(character);
      }
    }

    int offset = 0;
    Character prevCharacter = null;

    characters.sort(new PowerStatComparator());
    for (int i = 0; i < characters.size(); i++) {
      Character character = characterMap.get(characters.get(i).getId());
      if (prevCharacter != null && prevCharacter.getPower().get("max").equals(character.getPower().get("max"))) {
        offset--;
      }
      character.getPower().put("ranking", i + 1 + offset);
      prevCharacter = character;
    }

    characters.sort(new TechniqueStatComparator());
    for (int i = 0; i < characters.size(); i++) {
      characterMap.get(characters.get(i).getId()).getTechnique().put("ranking", i + 1);
    }

    characters.sort(new VitalityStatComparator());
    for (int i = 0; i < characters.size(); i++) {
      characterMap.get(characters.get(i).getId()).getVitality().put("ranking", i + 1);
    }

    characters.sort(new SpeedStatComparator());
    for (int i = 0; i < characters.size(); i++) {
      characterMap.get(characters.get(i).getId()).getSpeed().put("ranking", i + 1);
    }
  }
}
