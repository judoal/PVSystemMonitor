public class DoFXCalcs {
    public static String doFXCalcs(Configs fxC) {
        String retval=null;
        int inverterCurrent = Integer.parseInt(fxC.splitData[1]);
        fxC.fxID = Integer.parseInt(fxC.splitData[0]) - 1;
        fxC.Iitot[fxC.fxID] += inverterCurrent;
        fxC.Vacin[fxC.fxID] = Integer.parseInt(fxC.splitData[4]);
        if (!fxC.splitData[0].equals("~")) {
            int KWi = inverterCurrent * fxC.Vacin[fxC.fxID];
            fxC.Pitot[fxC.fxID] += (float) KWi;
            /*
             * System.out.println(fxC.fxID + 1 + ": " + String.format("%03d",
             * inverterCurrent) + " " + String.format("%03d",
             * fxC.Iitot[fxC.fxID]) + " " + String.format("%03d",
             * fxC.Vacin[fxC.fxID]) + " " + String.format("%03d", KWi) + " " +
             * Float.toString(fxC.Pitot[fxC.fxID]));
             */
            retval = "Inv amps: " + String.format("%03d", inverterCurrent) + " "
                    //+ String.format("%03d", fxC.Iitot[fxC.fxID]) + " "
                    + "Voltage in: " + String.format("%03d", fxC.Vacin[fxC.fxID]) + " "
                    + "Watts: " + String.format("%03d", KWi) + " "
                    + "Power: " +  Float.toString(fxC.Pitot[fxC.fxID]);
        }
        return retval;
    }
}
