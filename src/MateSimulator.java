import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class simulates a serial stream over IP from a data file that contains
 * line of data from the Outback Power Systems MATE2 in the same format as in the stream. 
 * A block is sent once each second.
 * 
 * @author allengordon  2016
 * 
 */

public class MateSimulator {

    static BufferedReader bufRead = null;
    static BufferedWriter bufWrite = null;
    static ServerSocket outSocket = null;
    static OutputStreamWriter outStrWrt = null;
    static PrintWriter out1 = null;
    static InetAddress addr;
    static int devCnt;
    static String dataString = "";
    static int portNo = 5555;
    static Socket connectionSocket = null;
    static BufferedWriter writer;

    public static void main(String[] args) {
//        File file = new File("2015-12-14_1.obm");
        File file = new File("matetest.obm");
        Thread startReader = new Thread (new StartReader());

        try {
            outSocket = new ServerSocket(portNo);
            bufRead = new BufferedReader(new FileReader(file));
            System.out.println("initializing...");
            devCnt = initialize(bufRead);
            startReader.start();
            connectionSocket = outSocket.accept();

            writer = new BufferedWriter(new OutputStreamWriter(
                    connectionSocket.getOutputStream()));
           
            while (true) {
                for (int i = 0; i < devCnt; i++) {
                    readBlock();
                }
                Thread.sleep(1000);
                writeBlock();
            }
        }
        catch (IOException e) {
            System.exit(-1);
        }
//        catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void readBlock() throws IOException {
        String tmpStr = bufRead.readLine();
        if (!tmpStr.startsWith("~")) {
            dataString += tmpStr + "\n";
        }
    }

    public static void writeBlock() throws IOException {
//        System.out.println(dataString);
        if (writer != null){
            writer.write(dataString );
            writer.flush();
        }
        dataString = "";
    }

    /**
     * initialize()
     * 
     * @param bR The buffered reader
     * 
     * @return number of devices connected to the MATE
     * data in blocks of ascii strings, 
     * the first value in the first string in the block
     * is the same.  Each string starts with a different 
     * alphanumeric character
     * @throws IOException
     */
    private static int initialize(BufferedReader bR) throws IOException {
        String str = "";
        String str1;
        String str0 = bR.readLine().split(",")[0];

        while ((str0 = bR.readLine().split(",")[0]).equals("")) {
        }

        int devCnt = 1;

        while (!(str = bR.readLine().split(",")[0]).equals(str0)) {
        }

        str1 = str;
        str = bR.readLine().split(",")[0];
        while (!(str.equals(str1))) {
            if (!str.equals("")) {
                devCnt++;
                if (isNumeric(str)) {
                }
            }
            str = bR.readLine().split(",")[0];
        }

        // this loop syncs to the 1st block to be logged
        for (int i = 1; i < devCnt-2 ; i++) {
            bR.readLine();
        }
        return devCnt;
    }

    public static boolean isNumeric(String data) {
        boolean isFX = false;
        if (data.matches("((-|\\+)?[0-9]+(\\.[0-9]+)?)+")) {
            isFX = true;
        }
        return isFX;
    }

}

class StartReader implements Runnable {
    @Override
    public void run() {
        try {
            MateDataStream.main(new String [] {"stream"});
        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
    }
}
