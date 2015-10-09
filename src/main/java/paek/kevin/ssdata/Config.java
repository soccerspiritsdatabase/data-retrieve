package paek.kevin.ssdata;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Config {

  public static final String BASE_URL = "http://bereq-c2s.qpyou.cn/soccerspirits/Android/Live/";
  public static final String PATCH_XML_URL = BASE_URL.concat("PatchLive.xml");
  public static final List<String> DB_FILES = Arrays.asList(new String[]{
          "Texts",
          "TextCharacters",
          "Characters",
          "Collections",
          "Skills",
          "CharacterChs",
          "Items",
          "ItemCollections",
          "Evolutions",
  });

  private static final Path RAW_FILES_DIR = Paths.get(".").resolve("raw_files");
  public static final Path RAW_DB_FILES_DIR = RAW_FILES_DIR.resolve("db");

  public static final Path JSON_FILES_DIR = Paths.get(".").resolve("json_files");
}
