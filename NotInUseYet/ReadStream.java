import java.io.BufferedReader;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;

public class ReadStream {

    public static final String LOGFILEROOT = System.getProperty("user.dir");
    String mateData = "";
    static int numDevices = -1;
    String[] data;
    boolean isDataValid = false;
    String[] splitData;
    int batchNo = 0;
    static int intervalCtr = 0;
    public static final int NEWLINE = 0x0A;
    public static final int EOL = 0x0D;
    public static final int MATE2BYTES = 49;
    static int numFX = 1;
    static DataInputStream dIS = null;
    static byte iByte = 0;

    public static void main(String[] args) throws Exception {

//        MateDataStream mdataStream = new MateDataStream();
        // SchedTrigger sT = new SchedTrigger();
        // sT.run();
        Socket clientSocket = null;
        BufferedReader bufferedReader = null;

        byte[] ipaddr = new byte[] { (byte) 192, (byte) 168, 1, 57 };
        InetAddress addr = InetAddress.getByAddress(ipaddr);
        clientSocket = new Socket(addr, 8899);
        dIS = new DataInputStream(clientSocket.getInputStream());
        gotoStart(); // initialize to start of data block
        if (validateStream()) {
            numDevices = getNumDevices();
//            System.out.println("NUM DEV: " + numDevices);
        }
        else {
            // error
        }

        // get block of data for <numDevices> streams
        // each stream is MATE2BYTES bytes
        readDeviceData();

    }
    
    public static void readDeviceData(){
        byte[] dataDevArry = new byte[numDevices * MATE2BYTES];

        for (int nBytes = 0; nBytes < MATE2BYTES; nBytes++) {               
            // build array of bytes for device`
            dataDevArry[nBytes] = readByte();   
            System.out.print(String.format("%02X", dataDevArry[nBytes]) + " ");
            
            
            
            // 1st byte -> numberic or alpha (check range of byte values

        }

        
    }

    public static byte[] readDataBlock() {
        byte[] dataBlockArry = new byte[numDevices * MATE2BYTES];

        int index=0;
        for (int nDev = 0; nDev < numDevices; nDev++) {
        }
        return dataBlockArry;
    }

    /*
     * get the number of devices in the stream, determine by 1st data byte
     */
    static int getNumDevices() {
        int tmpVal;
        int numDevices = 0;
        tmpVal = readByte();
        do {
            // dIS.kip((long) MATE2BYTES);
            validateStream();
            iByte = readByte();
//            System.out.print(String.format("%02X", iByte) + " ");
            numDevices++;
        }
        while (tmpVal != iByte);
        System.out.println();
        return numDevices;
    }

    /*
     * read a byte from stream
     */
    static byte readByte() {
        byte b;
        try {
            b = dIS.readByte();
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            b = Byte.MAX_VALUE;
        }
        return b;
    }

    /*
     * validate stream to see if it contains the co rrect number of bytes Data
     * in stream from each device is 49 bytes.
     */

    static boolean validateStream() {
        // count bytes to next NEWLINE -1
        int numBytes = 0;
        boolean retval;
        int numDevices = 0;
        // System.out.println();
        do {
            try {
                iByte = dIS.readByte();
                numBytes++;
                /*
                 * byte[] iByteArray = new byte[1]; iByteArray[0] = iByte; //
                 * ascii value System.out.println(numBytes + ": " +
                 * String.format("%02X", iByte) + "   " + new String(iByteArray,
                 * "UTF-8"));
                 */}
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        while ((iByte != NEWLINE) & (numBytes % MATE2BYTES != 0));

        return (numBytes % MATE2BYTES == 0 ? true : false);
    }

    /*
     * goto beginning of mate data stream
     */
    static void gotoStart() {
        iByte = -1;
        while (iByte != NEWLINE) {
            try {
                iByte = dIS.readByte();
            }
            catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            /*
             * String iByteStr = String.format("%02X", iByte); // hex primitive
             * byte[] iByteArray = new byte[1]; iByteArray[0] = iByte; // ascii
             * value try { System.out.println("   " + new String(iByteArray,
             * "UTF-8")); } catch (UnsupportedEncodingException e) { // TODO
             * Auto-generated catch block e.printStackTrace(); }
             */}
    }
}
