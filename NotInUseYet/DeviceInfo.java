/**
 * Outback Power Inverters have numeric IDs Outback Power MX charge controllers
 * have uppercase sequential alpha IDs Outback Power Flexmax have sequential
 * lowercase alpha
 * 
 * for 3 inverters and 4 charge controllers the ID sequence would be
 * "1","2""3","E","F","G","H","i","j","k","~"," "
 * 
 * to index these: 1. in range? 2. separate into numeric, upper case alpha,
 * lower case alpha, other? 3. get first character in series-series normalized
 * to this value
 * 
 * @author allengordon
 * 
 */
public class DeviceInfo {
    static Configs fxC = Configs.getInstance();

    public DeviceInfo() {
        fxC.numFX = 0;
        fxC.numMX = 0;
        fxC.numFlexMax = 0;

        fxC.preMXIndex = Integer.MAX_VALUE;
        fxC.preFXIndex = Integer.MAX_VALUE;
        fxC.preFlexMaxIndex = Integer.MAX_VALUE;
    }

    public static void main(String[] args) {
        DeviceInfo devInf = new DeviceInfo();

        String[] deviceID = { "1", "2", "3", "E", "F", "G", "H", "a", "b", "c",
                "~", " " };

        for (int i = 0; i < deviceID.length; i++) {           
            devInf.findDeviceInfo(deviceID[i]);
       }
        System.out.println("num devices: " + fxC.numDevices);
        System.out.println("num FX: " + fxC.numFX + " index: " + fxC.preFXIndex);
        System.out.println("num MX: " + fxC.numMX + " index: " + fxC.preMXIndex);
        System.out.println("num FlexMax: " + fxC.numFlexMax + " index: " + fxC.preFlexMaxIndex);
    }
    
    void findDeviceInfo(String str) {
        int index = str.charAt(0);
        if (str.matches("[A-Za-z0-9]+")) {
            // in range
            if (fxC.isNumeric(str)) {
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
}
