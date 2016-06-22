import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Chk4MissedDataTimer extends TimerTask {
    
    Configs fxC = Configs.getInstance();
    BlockingQueue<String> missingDataQueue = ProducerConsumerPattern.getMissingDataQueue();
    static Logger log = LoggerFactory.getLogger(Chk4MissedDataTimer.class);
    
    @Override
    public void run() {
        fxC.expectedTimeStamp = Integer.toString((int)Calendar.getInstance().getTimeInMillis());
        if (!fxC.isDataRead) {
                System.out.println("\n" + "MISSED: " + Calendar.getInstance().getTimeInMillis());
                log.error("MISSED: " + Calendar.getInstance().getTimeInMillis());
        }
    }
}
