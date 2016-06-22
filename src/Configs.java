import java.io.BufferedReader;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Configs {

    private static final Configs instance = new Configs();
    private Configs() {}

    public static Configs getInstance() {
        return instance;
    }
    
    Boolean readFromFile;
    
    BufferedReader bufferedReader;
    ProcessData processData;
    Boolean isDataValid;
    
    GregorianCalendar gCal;
    GregorianCalendar rightNow;
    
    public final int NEWLINE = 0x0A;
    public final int EOL = 0x0D;
    public final int MATE2BYTES = 49;
    public final int GUARDBAND = 64;

    int intA = (int)("A".charAt(0));
    int inta = (int)("a".charAt(0));  

    String firstBlock;

    long timeStamp;
    String expectedTimeStamp;
    long timeZero;
 
    int[] Vacin;
    String[] Vacout;

    int[] Iitot;
    int[] Ibtot;
    int[] Istot;

    int fxID;

    float[] Pi; // inverter KWH
    float[] Pb; // buy KWH
    float[] Ps; // sell KWH
    
    double pvKWH;
    double pvWatts;
    double[] calcDailyKWH;


    float Vbatt;

    String[] splitData;

    int numFX;
    int numMX;
    int numFlexMax;
    int numDevices;
    int currentDeviceNum;
    int preMXIndex;
    int preFXIndex;
    int preFlexMaxIndex;

    Float[] Pitot;
    Float[] Pbtot;
    Float[] Pstot;

    int[] Ii;
    String Ib;
    String Is;

    MateDataStream mateDataStream;
    MateDataStream mdataStream;
    
    /**
     * 1 count per sample; delivered once per second by the MATE.   Assume 1 sec from MAATE docs.  
     * maximum of 86400 samples per 24 hours.
     * use Quartz Schewduler to determine beginning of new day to reset the counter and at beginning 
     * of new month (this will account for leap seconds etc, since the max value of the counter can 
     * in some circumstances exceed the 86400 sec/day
     * This will also account for missing data
     * 
     * Counter used to calculate KWH for the inverters
     */
    int sampleCtr;  
    
    boolean consoleLog = false;
    String logLevel = "ERROR";

    boolean isDataRead;

    Calendar sampleTime;


    public boolean isNumeric(String data) {
        boolean isFX = false;
        if (data.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            isFX = true;
        }
        return isFX;
    }
    
    
    public boolean useConsoleLog() {
        return consoleLog || (logLevel.equalsIgnoreCase("debug") || logLevel.equalsIgnoreCase("trace"));
    }
}

