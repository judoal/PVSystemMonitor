
    /*
     * MX Data  
0   ID  [0] ascii numberic character
1   xx      unused
2   Ic  [2] charger current
3   Ipv [3] PV current
4   Vpv [4] PV voltage
5   KWH [5] daily kwh
6   unused
7   Aux [7] aux mode
8   Error[8]
9   Chgr_mode [9]   charger mode
10  Vb  [10]    battery voltage
11  xx      unused
12  xx      unused
13  sum [13]    chksum
     */

public class DoMXCalcs {
    public static String doMXCalcs(Configs fxC) {
        int ID = fxC.splitData[0].charAt(0) - fxC.preMXIndex;       
        String retval=null;
        int chargerCurrent = Integer.parseInt(fxC.splitData[2]);
        int pvCurrent = Integer.parseInt(fxC.splitData[3]);
        int pvVoltage = Integer.parseInt(fxC.splitData[4]);
        double dailyKWH = (Float.parseFloat(fxC.splitData[5]))/10.;
        fxC.pvWatts = (double)(pvCurrent * pvVoltage);
        double pvKiloWatts = fxC.pvWatts/1000.;
 
        double vBatt = (Float.parseFloat(fxC.splitData[10]))/10.;
        

        try {
        retval = "Ichger: " + String.format("%03d", chargerCurrent)+
                 " Ipv: " + String.format("%03d", pvCurrent) + 
                 " Vpv: " + String.format("%03d", pvVoltage) +
                 " Daily KWH: " + String.format("%03f", dailyKWH) +
                 " PV KiloWatts: " + String.format("%03f", pvKiloWatts ) + 
                 " Batt Volts: " + vBatt +
                 " Calc Daily KWH: " + String.format("%03f", fxC.calcDailyKWH[ID]);   
        }
        catch (ArrayIndexOutOfBoundsException e){
            System.out.println(ID);
            e.printStackTrace();
        }
        return retval;
    }
}
