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

  private static final Path BIN_DIR = Paths.get(".").resolve("bin").toAbsolutePath();
  private static final Path DATA_DIR = BIN_DIR.resolve("data").normalize();
  public static final Path PATCH_XML_FILE = BIN_DIR.resolve("PatchLive.xml").normalize();

  private static final Path RAW_FILES_DIR = BIN_DIR.resolve("raw_files").normalize();
  public static final Path RAW_DB_FILES_DIR = RAW_FILES_DIR.resolve("db").normalize();
  public static final Path RAW_CARD_FILES_DIR = RAW_FILES_DIR.resolve("cards").normalize();

  public static final Path JSON_FILES_DIR = DATA_DIR.resolve("json_files").normalize();
  public static final Path CARD_FILES_DIR = DATA_DIR.resolve("card_files").normalize();

  private static final Path TOOLS_DIR = Paths.get(".").resolve("tools").toAbsolutePath();
  public static final Path PATH_DISUNITY = TOOLS_DIR.resolve("disunity/disunity.bat").normalize();
  public static final Path PATH_PVR_TEX_TOOL = TOOLS_DIR.resolve("PVRTexToolCLI.exe").normalize();

  public static final float CARD_SIZE_RATIO = 0.7607421875f;

  private static final Path RES_DIR = Paths.get(".").resolve("res").toAbsolutePath();
  private static final Path RES_CARDS_DIR = RES_DIR.resolve("cards").normalize();
  public static final Path RES_CARDS_BACKGROUND_DIR = RES_CARDS_DIR.resolve("background").normalize();
  public static final Path RES_CARDS_BORDER_DIR = RES_CARDS_DIR.resolve("border").normalize();
}
