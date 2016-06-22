import java.util.Calendar;
import java.util.concurrent.BlockingQueue;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer
 * 
 * 
 * @author allengordon
 * 
 */

public class DeviceData {

    private static final DeviceData instance = new DeviceData();
    static Logger log = LoggerFactory.getLogger(DeviceData.class);

    ProcessData processData;
    String mateData;

    private DeviceData() {
        try {
            processData = new ProcessData();
        }
        catch (NumberFormatException e) {
            log.error(e.toString());
        }
        catch (IOException e) {
            log.error(e.toString());
        }
    }

    public static DeviceData getInstance() {
        return instance;
    }

    public String getDeviceData(String str) {
        Configs fxC = Configs.getInstance();
        String interpData = "";
        try {
            mateData = str;
            int chkSumCalc = 0;
            int chkSumStream = 0;
            if (fxC.isDataValid) {
                interpData = processData.processMateData(fxC, fxC.splitData);
            }

            if (mateData.length() == 47) {
                fxC.splitData = mateData.split(",", -2); // remove

                int[] meta = getCheckSum(fxC.splitData);

                chkSumCalc = meta[0];
                chkSumStream = meta[1];

                fxC.isDataValid = false;

                if (fxC.splitData[fxC.splitData.length - 1].length() > 1) {
                    fxC.isDataValid = ((chkSumCalc == chkSumStream) ? true
                            : false);
                    if (!fxC.isDataValid) {
                        System.out.println("check sum error: ");
                    }
                    else {
                        try {
                            interpData = fxC.timeStamp
                                    + ": "
                                    + processData.processMateData(fxC,
                                            fxC.splitData);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            else {
                // expected 47 bytes wait for next sample
                // log data length error, device id
            }
            // }
        }
        // }
        catch (Exception e) {
            e.printStackTrace();
        }
        return interpData;
    }

    public int[] getCheckSum(String[] dataString) {
        int strIndex;
        int chkSumCalc = 0;
        int deviceID;
        int chkSumStream = 0;

        for (strIndex = 0; strIndex < dataString.length; strIndex++) {
            try {
                int itemLen = dataString[strIndex].length();
                for (int substrIndex = 0; substrIndex < itemLen; substrIndex++) {
                    int itemVal = Integer.parseInt(dataString[strIndex]
                            .substring(substrIndex, substrIndex + 1));
                    if (strIndex < dataString.length - 1) {
                        chkSumCalc += itemVal;
                    }
                }
                deviceID = Integer.valueOf(String.valueOf(dataString[strIndex])
                        .getBytes()[0]);
            }
            catch (NumberFormatException e) {
                deviceID = dataString[strIndex].getBytes()[0] - 48; // not
                // numeric
                chkSumCalc += deviceID;
            }
            try {
                chkSumStream = Integer.valueOf(dataString[strIndex]);
            }
            catch (NumberFormatException e) {
                // do nothing, ignore non numeric
            }
        }
        int[] retval = { chkSumCalc, chkSumStream };
        return retval;
    }

}
