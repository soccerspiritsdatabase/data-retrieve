package paek.kevin.ssdata;

import paek.kevin.ssdata.models.*;
import paek.kevin.ssdata.models.Character;
import paek.kevin.ssdata.work.DownloadFiles;
import paek.kevin.ssdata.work.ParseDBFiles;

import java.util.Map;

public class Main {

  public static void main(String[] args) {
    //DownloadFiles.process();
    ParseDBFiles.process();
  }

}
