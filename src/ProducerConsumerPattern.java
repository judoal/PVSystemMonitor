/**
 * producer thread is the loop in MataDataStream.
 * consumer thread is ProcessData.processMateData
 * 
 * 
 */

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dials.RotateImage;

public class ProducerConsumerPattern {

    private static BlockingQueue<String> sharedQueue;
    private static BlockingQueue<String> chkSumErrQueue;
    private static BlockingQueue<String> missingDataQueue;
    private static BlockingQueue<String> shortDataStringQueue;
    
    static Logger log = LoggerFactory.getLogger(MateDataStream.class);
    Configs fxC = Configs.getInstance();

    public ProducerConsumerPattern() {
        // Creating shared object
        sharedQueue = new LinkedBlockingQueue<String>();
        chkSumErrQueue = new LinkedBlockingQueue<String>();
        shortDataStringQueue = new LinkedBlockingQueue<String>();
        missingDataQueue = new LinkedBlockingQueue<String>();

        Thread prodThread = new Thread(new Producer(sharedQueue));
        log.info("run consumer");
        Thread consThread = new Thread(new Consumer(sharedQueue,
                missingDataQueue));
        log.info("run producer");
        fxC.timeZero = Calendar.getInstance().getTimeInMillis();
        prodThread.start();
        consThread.start();
    }

    public static BlockingQueue<String> getBlockingQueue() {
        return (sharedQueue);
    }

    public static BlockingQueue<String> getMissingDataQueue() {
        return (missingDataQueue);
    }

    public static BlockingQueue<String> getChkSumErrQueue() {
        return (chkSumErrQueue);
    }

    public static BlockingQueue<String> shortDataStringQueue() {
        return (shortDataStringQueue);
    }

}

class Producer implements Runnable {

    Configs fxC;
    private final BlockingQueue<String> sharedQueue;
    static Logger log = LoggerFactory.getLogger(Producer.class);

    public Producer(BlockingQueue<String> sharedQueue) {
        this.sharedQueue = sharedQueue;
    }

    @Override
    public void run() {
        fxC = Configs.getInstance();
        log.info("starting data monitor...");
        // start missing data timer
//        Timer missingDataTimer = new Timer();
//        missingDataTimer.schedule(new Chk4MissedDataTimer(), 15, 2000);

        while (true) {
            try {
                fxC.sampleTime = Calendar.getInstance();
                fxC.timeStamp = fxC.sampleTime.getTimeInMillis();
                String tmpStr = fxC.bufferedReader.readLine();

                sharedQueue.put(tmpStr);
                fxC.isDataRead = true;
            }

            catch (IOException | InterruptedException e) {
                // TODO Auto-generated catch block
                log.error(e.toString());
            }
        }

    }
}

// Consumer Class in Java
class Consumer implements Runnable {

    String interpDataStr = "";
    String outStr = "";
    long deltaT = 0;
    Configs fxC = Configs.getInstance();
    static Logger log = LoggerFactory.getLogger(Consumer.class);

    private final BlockingQueue<String> sharedQueue;
    private static BlockingQueue<String> missingDataQueue;

    public Consumer(BlockingQueue<String> sharedQueue,
            BlockingQueue<String> missingDataQueue) {
        this.sharedQueue = sharedQueue;
        this.missingDataQueue = missingDataQueue;
    }

    @Override
    public void run() {
        fxC.pvKWH=0;
        fxC.sampleCtr++;
//        Calendar cal = null;
//        Calendar cal = new GregorianCalendar();
//        cal.clear();
/*        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        long time0 = cal.getTimeInMillis();
        long time0 = System.currentTimeMillis();
        deltaT = fxC.timeStamp - time0;
        cal.setTimeInMillis(deltaT);
        int hour0 = cal.get(Calendar.HOUR_OF_DAY);
        System.out.println(hour0 + " : " + fxC.timeStamp + " " + cal.get(Calendar.HOUR_OF_DAY));
*/        while (true) { // TODO - add restart functionality
            String tmpStr1="";
            String tmpStr2 = "";
            try {
                for (int i = 0; i < fxC.numDevices; i++) {

                    interpDataStr = DeviceData.getInstance().getDeviceData(
                            sharedQueue.take());

                    fxC.isDataRead = false;
//                    if (i > 0) {
                        tmpStr1 += DeviceData.getInstance().getDeviceData(
                                interpDataStr)
                                + "\n";
                        fxC.isDataRead = true;

//                    }
                    if (i==0) {
                        // cal = new GregorianCalendar();
//                        cal = Calendar.getInstance();
                        int hour = fxC.sampleTime.get(Calendar.HOUR_OF_DAY);
                        int minute = fxC.sampleTime.get(Calendar.MINUTE);
                        int second = fxC.sampleTime.get(Calendar.SECOND);

                        tmpStr2 += fxC.sampleCtr++
                                + ": "
                                + fxC.timeStamp
                                + ": "
                                + "TOD: "
                                + hour
                                + ":"
                                + minute
                                + ":"
                                + second
                                + "\n";
//                                + DeviceData.getInstance().getDeviceData(
//                                        interpDataStr) + "\n";                                   
                    }

//                    fxC.currentDeviceNum = ++fxC.currentDeviceNum
//                            % fxC.numDevices;
                }
            }
            catch (InterruptedException ex) {
                log.error(ex.toString());
            }
            System.out.print(tmpStr2 + tmpStr1);
            
            try {
                new RotateImage(fxC.pvWatts,true,0,10000);
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
    }
}