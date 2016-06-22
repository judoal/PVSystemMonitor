/*
 * The complete set of mdataStream.data strings from the stream repeated every second
 * 
 * 
 * FX,Ii,Icc,Ib,Vacin,Vacout,Is,mode,err,AC,Vb,msc,warn,sum
 * 1,20,00,00,125,124,16,09,000,02,540,152,032,058
 * 2,18,00,00,128,126,10,09,000,02,540,152,000,060
 * 3,17,00,00,128,127,17,09,000,02,540,152,000,068
 * 
 * 
 * 
 * 
 *processedData[0] == 1, 2, 3, ... for FX data
 *processedData[1]     Ii:  inverter current 
 *processedData[3]     Ib:  buy current
 *processedData[6]     Is:  sell current
 *processData[4]       Vacin:  input line voltage
 *processData[5]       Vacout: output line voltage
 *processData[10]      Vbatt:   battery voltage
 *
 * 
 * 
 * 
 * 
 * MX,xx,Ic,Ipv,Vpv,kwh,Ic',aux,err,chrg,Vb,ah,xxx,sum
 * E,00,34,30,066,068,00,00,000,02,541,000,000,069
 * F,00,36,35,059,050,00,00,000,02,543,000,000,072
 * G,00,42,35,069,054,00,00,000,02,544,000,000,076
 * H,00,44,41,060,073,00,00,000,02,542,000,000,066
 * 
 * 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MateDataStream {

    public static final String LOGFILEROOT = System.getProperty("user.dir");
    static Logger log = LoggerFactory.getLogger(MateDataStream.class);

    public static void main(String[] args) throws Exception {
        System.out.println("starting data collection");
        Configs fxC = Configs.getInstance();
        fxC.sampleCtr = 0;
        fxC.isDataValid = false;
        fxC.isDataRead = false;

        SchedTrigger sT = new SchedTrigger();
        sT.run();
        Socket clientSocket = null;
        fxC.bufferedReader = null;
        fxC.currentDeviceNum = 0;

        fxC.numFX = 0;
        fxC.numMX = 0;
        fxC.numFlexMax = 0;

        fxC.preMXIndex = Integer.MAX_VALUE;
        fxC.preFXIndex = Integer.MAX_VALUE;
        fxC.preFlexMaxIndex = Integer.MAX_VALUE;

        try {

            if (args[0].equals("stream")) {
                System.out.println("reading from localhost");
                clientSocket = new Socket("localhost", 5555);
                fxC.bufferedReader = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
            }
            else if (args[0].equals("file")) {
                File file = new File("2015-12-14_1.obm");
                fxC.bufferedReader = new BufferedReader(new FileReader(file));
            }
            else if (args[0].equals("mate")) {
                clientSocket = new Socket(InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, 57 }), 8899);
                fxC.bufferedReader = new BufferedReader(new InputStreamReader(
                        clientSocket.getInputStream()));
            }
                       

            initialize(fxC.bufferedReader);
            fxC.currentDeviceNum = 1;
            fxC.calcDailyKWH = new double[fxC.numMX];
            log.info("starting ...");

            new ProducerConsumerPattern();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * initialize()
     * 
     * @param bR
     * 
     * @return number of devices connected to the MATE
     * @throws IOException
     */
    private static void initialize(BufferedReader bR) throws IOException {
        log.info("initializing...");
        Configs fxC = Configs.getInstance();
        String str = "";
        fxC.numFX = 0;
        String str1;
        String str0 = bR.readLine().split(",")[0];
        while ((str0 = bR.readLine().split(",")[0]).equals("")) {
            // TODO timeout
        }

        fxC.numDevices = 1;

        while (!(str = bR.readLine().split(",")[0]).equals(str0)) {
            // TODO timeout
        }

        str1 = str;
        str = bR.readLine().split(",")[0];
        fxC.firstBlock = "";

        while (!(str.equals(str1))) {
            if (!str.equals("")) {
                fxC.numDevices++;
                /*
                 * if (fxC.isNumeric(str)) { fxC.numFX++; }
                 */
            }
            str = bR.readLine().split(",")[0];
        }

        // this loop syncs to the 1st block to be logged
        for (int i = 1; i < fxC.numDevices; i++) {
            str = bR.readLine();
            /*
             * if (!fxC.isNumeric(str.substring(0))) { mxIDTmp =
             * Math.min(mxIDTmp, fxC.FirstNonFXID); fxC.FirstNonFXID = mxIDTmp;
             * } } fxC.numMX = devCnt - fxC.numFX; fxC.calcDailyKWH = new
             * double[fxC.numMX]; for (int j = 0; j < fxC.numMX; j++) {
             * fxC.calcDailyKWH[j] = 0.; }
             */
            // fxC.firstMXID = Math.abs(fxC.firstMXID+fxC.intA+1);
        }
        // fxC.numDevices = devCnt;

        // initialize indexes
        // DeviceInfo devInf = new DeviceInfo();
        for (int i = 0; i < fxC.numDevices; i++) {
            str = bR.readLine();
            MateDataStream.findDeviceInfo(str, fxC);
        }
    }

    static void findDeviceInfo(String str, Configs fxC) {
        String IDStr = str.substring(0);
        int index = str.charAt(0);
        // in range
        if (Character.isDigit (str.charAt(0))) {
            // inverter
            fxC.preFXIndex = Math.min(fxC.preFXIndex, index);
            fxC.numFX++;
        }
        else if (Character.isUpperCase(str.charAt(0))) {
            // MX charge controller
            fxC.preMXIndex = Math.min(fxC.preMXIndex, index);
            fxC.numMX++;
        }
        else if (Character.isLowerCase(str.charAt(0))) {
            // flexmax
            fxC.preFlexMaxIndex = Math.min(fxC.preFlexMaxIndex, index);
            fxC.numFlexMax++;
        }
        else {
            // ?
        }
    }

}
