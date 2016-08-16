import java.io.IOException;


/**
 * Consumer #1
 * @author allengordon
 *
 */

public class ProcessData {
   
    public static final int NEWLINE = 0x0A;
    public static final int EOL = 0x0D;
    public static final int MATE2BYTES = 49;

    
    public ProcessData() throws IOException, NumberFormatException {
        
        Configs fxC = Configs.getInstance();

        fxC.sampleCtr = 0;
        
        fxC.Ib = "";
        fxC.Is = "";
 

        fxC.Pitot = new Float[fxC.numFX];
        fxC.Pbtot = new Float[fxC.numFX];
        fxC.Pstot = new Float[fxC.numFX];
        

        fxC.Iitot = new int[fxC.numFX];
        fxC.Ibtot = new int[fxC.numFX];
        fxC.Istot = new int[fxC.numFX];
        fxC.Ii = new int[fxC.numFX];

        fxC.Vacin = new int[fxC.numFX];
        fxC.Vacout = new String[fxC.numFX];

        for (int i = 0; i < fxC.numFX; i++) {
            fxC.Pitot[i] = fxC.Pbtot[i] = fxC.Pstot[i] = (float) 0.0;
            fxC.Vacin[i] = 0;
            fxC.Vacout[i] = "0";
            fxC.Iitot[i] = fxC.Istot[i] = fxC.Ibtot[i] = 0;
            fxC.Pstot[i] = fxC.Pbtot[i] = (float) 0.0;
        }

    }

    public String processMateData(Configs fxC, String[] str) throws IOException {
        /*
         * get device type from first character if numeric -> MX charge
         * controller otherwise alpha -> FX or battery
         */
        String retVal = ""; //= Long.toString(timeStamp) + " ";
        fxC.splitData = str;
        
        try {
              if (Character.isDigit(fxC.splitData[0].charAt(0))) {
            // numeric -> FX inverter
                /*
                 * for (int i=0; i < splitData.length; i++) {
                 * System.out.print(splitData[i]); } System.out.println();
                 */
                retVal += decodeFXdata(fxC, fxC.splitData);                
            }
            else {
                // alpha -> uppercase == MX 
               //           lowercasee == flexmate
               retVal += decodeMXdata(fxC, fxC.splitData);
            }
        }
        catch (NumberFormatException e) {
            /* TODO error handler */
            e.printStackTrace();
        }

        return retVal;
    }

    /**
     * FX Data index description 0 ID numeric character 1 fxC.Ii inverter current,
     * ascfxC.Ii numeric 2 Ic charger current, ascfxC.Ii numerfxC.Iic 3 Ib buy current,
     * ascfxC.Ii numeric 4 Vi AC input voltage, ascfxC.Ii numeric 5 Vo AC output voltage
     * 6 Is Sell current 7 Op_Mode Operating Mode 8 Error Error Mode 9 AC_Mode
     * 10 Vb battery voltage 11 Misc miscellaneous 12 warning mode 13 Sum check
     * sum
     * 
     * @return decoded FX data
     * 
     * 
     */

    static String decodeFXdata(Configs fxC, String[] string)
            throws NumberFormatException {
            
        fxC.splitData = string;
        float Vbatt;
        
        String interpData = "";
        for (int index = 0; index < fxC.splitData.length; index++) {
            switch (index) {
            case 0:
                // FX ID
                interpData += fxC.splitData[index];
                break;
            case 1:
                // Inverter current fxC.Ii
                interpData += fxC.splitData[index];
                break;
            case 2:
                interpData += fxC.splitData[index];
                break;
            case 3:
                // inverter buy current Ib
                interpData += fxC.splitData[index];
                break;
            case 4:
                // fxC.Vacin
                interpData += fxC.splitData[index];
                break;
            case 5:
                /*
                 * fxC.Vacout, 230 volt system?
                 */
                if ((Integer.parseInt(fxC.splitData[11]) & 0x1) == 1) {
                    interpData += Integer.parseInt(fxC.splitData[index]) * 2;
                }
                else {
                    interpData += fxC.splitData[index];
                }
                break;
            case 6:
                // sell current Is
                interpData += fxC.splitData[index];
                break;
            case 7:
                /**
                 * operational mode: DATA 00" Inv Off, 01" Search, 02" Inv On,
                 * 03" Charge, 04" Silent, 05" Float, 06" EQ, 07" Charger Off,
                 * 08" Support, Ò09Ó Sell Enabled, Ò10Ó Pass Thru, Ò90Ó FX
                 * Error, Ò91Ó AGS Error, Ò92Ó Comm Error,
                 */
                int tmp = Integer.parseInt(fxC.splitData[7]);
                int tmpIndex;
                String tmpStr = "";
                for (tmpIndex = 0; tmpIndex < 100; tmpIndex++) {
                    switch (tmp) {
                    case 0:
                        tmpStr = "Inv Off";
                        break;
                    case 1:
                        tmpStr = "Search";
                        break;
                    case 2:
                        tmpStr = "Inv On";
                        break;
                    case 3:
                        tmpStr = "Charge";
                        break;
                    case 4:
                        tmpStr = "Silent";
                        break;
                    case 5:
                        tmpStr = "Float";
                        break;
                    case 6:
                        tmpStr = "EQ";
                        break;
                    case 7:
                        tmpStr = "Charger Off";
                        break;
                    case 8:
                        tmpStr = "Support";
                    case 9:
                        tmpStr = "Sell Enabled";
                        break;
                    case 10:
                        tmpStr = "Pass Thru";
                        break;
                    case 90:
                        tmpStr = "FX Error";
                        break;
                    case 91:
                        tmpStr = "AGS Error";
                        break;
                    case 92:
                        tmpStr = "Comm Error";
                        break;
                    default:
                        tmpStr = "Unknown Op Mode";
                        break;
                    }
                }
                interpData += tmpStr;
                break;
            case 8:
                // error mode
                String tmpErrStr = "";
                for (int bitshift = 0; bitshift < 8; bitshift++) {
                    if ((Integer.parseInt(fxC.splitData[8]) >> bitshift & 0x1) == 1) {
                        switch (bitshift) {
                        case 0:
                            tmpErrStr = "Low AC output";
                            break;
                        case 1:
                            tmpErrStr = "Stacking Error";
                            break;
                        case 2:
                            tmpErrStr = "Over Temp";
                            break;
                        case 3:
                            tmpErrStr = "Low Battery";
                            break;
                        case 4:
                            tmpErrStr = "Phase Loss";
                            break;
                        case 5:
                            tmpErrStr = "High Battery";
                            break;
                        case 6:
                            tmpErrStr = "Shorted Output";
                            break;
                        case 7:
                            tmpErrStr = "Back Feed";
                            break;
                        default:
                            tmpErrStr = "Unknown error";
                            break;
                        }
                    }
                }
                interpData += tmpErrStr;
                break;
            case 9:
                // AC mode
                String tmpACStr = "";
                if (fxC.splitData[9].equals("00")) {
                    tmpACStr = "No AC";
                }
                else if (fxC.splitData[9].equals("01")) {
                    tmpACStr = "AC Drop";
                }
                else if (fxC.splitData[9].equals("02")) {
                    tmpACStr = "AC Use";
                }
                else {
                    tmpACStr = "Unknown AC state";
                }
                interpData += tmpACStr;
                break;
            case 10:
                /**
                 * battery voltage convert to float, divide by 10, return a
                 * string representation of the floating point result to 1
                 * decimal place
                 */ 
                  
                 fxC.Vbatt = (float)(Float.parseFloat(fxC.splitData[index])/10.0); 
                 interpData += String.valueOf(fxC.Vbatt); 
                 break; 
            case 11:
                if ((Integer.parseInt(fxC.splitData[11]) & 0x1) == 1) {
                    interpData += "230V ";
                }
                if ((Integer.parseInt(fxC.splitData[11]) >> 7 & 0x1) == 1) {
                    interpData += "AUX Output ON ";
                }
                break;
            case 12: // warning
                String tmpWarnStr = "";
                for (int bitshift = 0; bitshift < 8; bitshift++) {
                    if ((Integer.parseInt(fxC.splitData[12]) >> bitshift & 0x1) == 1) {
                        switch (bitshift) {
                        case 0:
                            tmpWarnStr = "AC Input Freq High";
                            break;
                        case 1:
                            tmpWarnStr = "AC Input Freq Low";
                            break;
                        case 2:
                            tmpWarnStr = "Input VAC High";
                            break;
                        case 3:
                            tmpWarnStr = "Input VAC Low";
                            break;
                        case 4:
                            tmpWarnStr = "Buy Amps > input size";
                            break;
                        case 5:
                            tmpWarnStr = "Temp Sensor Failed";
                            break;
                        case 6:
                            tmpWarnStr = "Comm Error";
                            break;
                        case 7:
                            tmpWarnStr = "Fan Failure";
                            break;
                        default:
                            tmpWarnStr = "Unknown Warning";
                            break;
                        }
                    }
                }
                interpData += tmpWarnStr;
                break;
            case 13: // check sum, shouldn't be needed
                break;
            default: // all others
                break;
            }
            interpData += ",";
        }
        interpData += "\n\t" + DoFXCalcs.doFXCalcs(fxC);
        return interpData;
    }
    
    /*
     * MX Data  
0   ID  [0] ascii numberic character
1   xx      unused
2   Ic  [2] charger current
3   Ipv [3] PV current
4   Vpv [4] PV voltage
5   KWH [5] daily kwh
6   Ic' [6] tenths of charger current--note only one char is used here
7   Aux [7] aux mode
8   Error[8]
9   Chgr_mode [9]   charger mode
10  Vb  [10]    battery voltage
11  Ah  [11]    daily amp hours
12  xx      unused
13  sum [12]    chksum
     */

    static String decodeMXdata(Configs fxC, String[] str) throws IOException {
          String [] splitData = str;
        String interpData = "";
        for (int index = 0; index < splitData.length; index++) {
            switch (index) {
            case 0: // id
                interpData += splitData[index];
                break;
            case 1: // not used
            case 2: // charger current, amp
                interpData += splitData[index];
                break;
            case 3: // PV current
                interpData += splitData[index];
                break;
            case 4: // PV FPanel Voltage
                interpData += splitData[index];
                break;
            case 5: // daily PV KWH
                /**
                 * convert to float, divide by 10, return a string
                 * representation of the floating point result to 1 decimal
                 * place
                 */
                Float dataFloatValue = Float.parseFloat(splitData[index]);
                dataFloatValue /= 10.f;
                interpData += Float.toString(dataFloatValue);
                break;
            case 6: // tenths of amps for charger current
                    // add to splitData[2] for total charger current
                interpData += splitData[index];
                break;
            case 7: // Aux mode
                int tmp = Integer.parseInt(splitData[index]);
                // int tmpIndex;
                String tmpStr = "";
                // for (tmpIndex = 0; tmpIndex < 11; tmpIndex++) {
                switch (tmp) {
                case 0:
                    tmpStr = "Aux Mode Disabled";
                    break;
                case 1:
                    tmpStr = "Diversion";
                    break;
                case 2:
                    tmpStr = "Remote";
                    break;
                case 3:
                    tmpStr = "Manual";
                    break;
                case 4:
                    tmpStr = "Fent Fan";
                    break;
                case 5:
                    tmpStr = "PV Trigger";
                    break;
                case 6:
                    tmpStr = "Float";
                    break;
                case 7:
                    tmpStr = "Error Output";
                    break;
                case 8:
                    tmpStr = "Night Light";
                    break;
                case 9:
                    tmpStr = "PWM Diversion";
                    break;
                case 10:
                    tmpStr = "Low Battery";
                    break;
                }
                interpData += tmpStr;
                // }
            case 8: // Error modes
                String tmpErrStr = "";
                for (int bitshift = 0; bitshift < 8; bitshift++) {
                    if ((Integer.parseInt(splitData[8]) >> bitshift & 0x1) == 1) {
                        switch (bitshift) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                            tmpErrStr = "";
                            break;
                        case 6:
                            tmpErrStr = "Shorted Battery Sensor";
                            break;
                        case 7:
                            tmpErrStr = "Too Hot";
                            break;
                        default:
                            tmpErrStr = "Unknown Error";
                            break;
                        }
                    }
                }
                interpData += tmpErrStr;
                break;
            case 9: // Charger mode
                tmp = Integer.parseInt(splitData[index]);
                tmpStr = "";
                for (int tmpIndex = 0; tmpIndex < 5; tmpIndex++) {
                    switch (tmp) {
                    case 0:
                        tmpStr = "Silent";
                        break;
                    case 1:
                        tmpStr = "Float";
                        break;
                    case 2:
                        tmpStr = "Bulk";
                        break;
                    case 3:
                        tmpStr = "Absorb";
                        break;
                    case 4:
                        tmpStr = "EQ";
                        break;
                    default:
                        tmpStr = "Unknown Charger Mode";
                        break;
                    }
                }
                interpData += tmpStr;
                break;
            case 10: // battery voltage x 10
                /**
                 * convert to float, divide by 10, return a string
                 * representation of the floating point result to 1 decimal
                 * place
                 */
                dataFloatValue = Float.parseFloat(splitData[index]);
                dataFloatValue /= 10.f;
                interpData += Float.toString(dataFloatValue);
                break;
            case 11: // Charge controller Daily AH
                interpData += splitData[index];
                break;
            case 12: // unused
            case 13: // check sum
                interpData += "";
                break;
            default: // all others
                interpData += "Unknown Charger mode";
                break;
            }
            interpData += ",";
        }
        interpData += "\n\t" + DoMXCalcs.doMXCalcs(fxC);
        return interpData;
    }

    /* TODO - add decode for flexmax battery data, and MATE3 */
}

