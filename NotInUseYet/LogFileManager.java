import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class LogFileManager {
    
    static File yearDir;
    static File monthDir;
    static File dayFile;
    String dateString="";
    String logFileRoot = "";
    String logDataPathName="";
    Boolean _instantiated = false;
    static File file;
    static Calendar now = Calendar.getInstance();
    static int year = now.get(Calendar.YEAR);
    static int month = now.get(Calendar.MONTH);
    static int day = now.get(Calendar.DATE);
    static int hour = now.get(Calendar.HOUR);
    static int min = now.get(Calendar.MINUTE);
    static int sec = now.get(Calendar.SECOND);
    static int milli = now.get(Calendar.MILLISECOND);

    
       
    public LogFileManager(String logFileRoot) {
        if (logFileRoot == ""){
            this.logFileRoot=System.getProperty("user.dir");
        }
        else {
          this.logFileRoot=logFileRoot;
        }
        String fSep = System.getProperty("file.separator");
               
        String path1 = this.logFileRoot + fSep + year;
        File logFile = new File(logFileRoot,path1);
        logFile.mkdirs();
        String path2 = month + "";
        logFile = new File(logFile,path2);
        logFile.mkdirs();
//        System.out.println("path2: " + logFile);
        String date = day + ".log";
//        System.out.println(date);
        file = new File(logFile,date);
//        System.out.println("file: " + file);
        try {
//            System.out.println("FILE: " + file.createNewFile());
            file.createNewFile();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public static File getPathName(){
        if(!LogFileManager.file.exists()) {
            new LogFileManager(MateDataStream.LOGFILEROOT);
        }
        
        return file;
    }
}
