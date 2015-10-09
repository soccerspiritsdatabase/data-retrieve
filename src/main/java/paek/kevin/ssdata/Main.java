package paek.kevin.ssdata;

import paek.kevin.ssdata.work.DownloadFiles;
import paek.kevin.ssdata.work.ParseDBFiles;
import paek.kevin.ssdata.work.Worker;

public class Main {

  public static void main(String[] args) {
    Worker[] workers = new Worker[]{
            //new DownloadFiles(),
            new ParseDBFiles()
    };

    for (Worker worker : workers) {
      worker.doWork();
    }
  }
}
