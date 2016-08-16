<?php

/*
This code opens the WattPlot WPState.csv file and assigns the data to
variables for the main web site to display. In this script, the csv file is in the
same directory as this php file. If it is not, include the full path in the fopen
statement. Some web hosting providers do not allow fopen to open a remote file.

Include this file in the default php file by using the following statement:
<?php require_once("wpdata.php"); ?>
*/ 

$msg_count = 0; //clear message line count

if (is_readable("WPState.csv")) {

    $fd = fopen("WPState.csv", "r");

    while (!feof($fd)) { //loop until the end of file is reached
        $wp = fgetcsv($fd); //get next line in file and put data in wp[ ] array

/*
Use the first item in each line to check which device the data is for
and then load the data into that device's variables.
Code can be expanded for multiple devices of the same type, as is shown
with the inverter "FX" and charge controller "CC" sections (I have two of each).
*/


        switch ($wp[0]) {


            case "STATUS": //this is a status line
                $status_name = $wp[1];
                $status_when = $wp[3];
                break;

            case "MATE": //this is a mate line
                $mate_number = $wp[1];
                $mate_status = $wp[2];
                $batt_voltage = $wp[3];
                $batt_soc = $wp[4];
                $batt_temp = $wp[5];
                break;


            case "FX": //this is an inverter line

                if ($wp[2] == "FX-1") { //if this is inverter 1 load fx1 vars

                    $fx1_id = $wp[2];

                    $fx1_inverter_kWh = $wp[3];

                    $fx1_charger_kWh = $wp[4];

                    $fx1_buy_kWh = $wp[5];

                    $fx1_sell_kWh = $wp[6];

                    $fx1_aux_hrs = $wp[7];

                    $fx1_batt_minVDC = $wp[8];

                    $fx1_batt_maxVDC = $wp[9];

                    $fx1_aux = $wp[10];

                    $fx1_OPmode = $wp[11];

                    $fx1_ACmode = $wp[12];

                    $fx1_warn = $wp[13];

                    $fx1_err = $wp[14];

                    $fx1_ACin = $wp[17];

                    $fx1_ACout = $wp[18];

                    $fx1_invAmp = $wp[19];

                    $fx1_chgAmp = $wp[20];

                    $fx1_buyAmp = $wp[21];

                    $fx1_sellAmp = $wp[22];

                    $fx1_Alert = $wp[23];

                }

                elseif ($wp[2] == "FX-2") { //else if this is inverter 2 load fx2 vars

                    $fx2_id = $wp[2];

                    $fx2_inverter_kWh = $wp[3];

                    $fx2_charger_kWh = $wp[4];

                    $fx2_buy_kWh = $wp[5];

                    $fx2_sell_kWh = $wp[6];

                    $fx2_aux_hrs = $wp[7];

                    $fx2_batt_minVDC = $wp[8];

                    $fx2_batt_maxVDC = $wp[9];

                    $fx2_aux = $wp[10];

                    $fx2_OPmode = $wp[11];

                    $fx2_ACmode = $wp[12];

                    $fx2_warn = $wp[13];

                    $fx2_err = $wp[14];

                    $fx2_ACin = $wp[17];

                    $fx2_ACout = $wp[18];

                    $fx2_invAmp = $wp[19];

                    $fx2_chgAmp = $wp[20];

                    $fx2_buyAmp = $wp[21];

                    $fx2_sellAmp = $wp[22];

                    $fx2_Alert = $wp[23];

                }

                elseif ($wp[2] == "FX-3") { //else if this is inverter 2 load fx3 vars

                    $fx3_id = $wp[2];

                    $fx3_inverter_kWh = $wp[3];

                    $fx3_charger_kWh = $wp[4];

                    $fx3_buy_kWh = $wp[5];

                    $fx3_sell_kWh = $wp[6];

                    $fx3_aux_hrs = $wp[7];

                    $fx3_batt_minVDC = $wp[8];

                    $fx3_batt_maxVDC = $wp[9];

                    $fx3_aux = $wp[10];

                    $fx3_OPmode = $wp[11];

                    $fx3_ACmode = $wp[12];

                    $fx3_warn = $wp[13];

                    $fx3_err = $wp[14];

                    $fx3_ACin = $wp[17];

                    $fx3_ACout = $wp[18];

                    $fx3_invAmp = $wp[19];

                    $fx3_chgAmp = $wp[20];

                    $fx3_buyAmp = $wp[21];

                    $fx3_sellAmp = $wp[22];

                    $fx3_Alert = $wp[23];

                }



                break;


            case "CC": //this is a charge controller line

                if ($wp[2] == "CC-4") { //i have 2 charge controllers on ports 3,4
                    $cc4_id = $wp[2];
                    $cc4_charger_kWh = $wp[3];
                    $cc4_panel_kWh = $wp[4];
                    $cc4_daily_kWh = $wp[5];
                    $cc4_float_hrs = $wp[6];
                    $cc4_aux_hrs = $wp[7];
                    $cc4_batt_minVDC = $wp[8];
                    $cc4_batt_maxVDC = $wp[9];
                    $cc4_pv_maxV = $wp[10];
                    $cc4_OPmode = $wp[12];
                    $cc4_chgAmp = $wp[13];
                    $cc4_pvAmp = $wp[14];
                    $cc4_pvV = $wp[15];
                    $cc4_Alert = $wp[23];
                }

                elseif ($wp[2] == "CC-5") {
                    $cc5_id = $wp[2];
                    $cc5_charger_kWh = $wp[3];
                    $cc5_panel_kWh = $wp[4];
                    $cc5_daily_kWh = $wp[5];
                    $cc5_float_hrs = $wp[6];
                    $cc5_aux_hrs = $wp[7];
                    $cc5_batt_minVDC = $wp[8];
                    $cc5_batt_maxVDC = $wp[9];
                    $cc5_pv_maxV = $wp[10];
                    $cc5_OPmode = $wp[12];
                    $cc5_chgAmp = $wp[13];
                    $cc5_pvAmp = $wp[14];
                    $cc5_pvV = $wp[15];
                    $cc5_Alert = $wp[23];
                }

                elseif ($wp[2] == "CC-6") {
                    $cc6_id = $wp[2];
                    $cc6_charger_kWh = $wp[3];
                    $cc6_panel_kWh = $wp[4];
                    $cc6_daily_kWh = $wp[5];
                    $cc6_float_hrs = $wp[6];
                    $cc6_aux_hrs = $wp[7];
                    $cc6_batt_minVDC = $wp[8];
                    $cc6_batt_maxVDC = $wp[9];
                    $cc6_pv_maxV = $wp[10];
                    $cc6_OPmode = $wp[12];
                    $cc6_chgAmp = $wp[13];
                    $cc6_pvAmp = $wp[14];
                    $cc6_pvV = $wp[15];
                    $cc6_Alert = $wp[23];
                }
                elseif ($wp[2] == "CC-7") {
                    $cc7_id = $wp[2];
                    $cc7_charger_kWh = $wp[3];
                    $cc7_panel_kWh = $wp[4];
                    $cc7_daily_kWh = $wp[5];
                    $cc7_float_hrs = $wp[6];
                    $cc7_aux_hrs = $wp[7];
                    $cc7_batt_minVDC = $wp[8];
                    $cc7_batt_maxVDC = $wp[9];
                    $cc7_pv_maxV = $wp[10];
                    $cc7_OPmode = $wp[12];
                    $cc7_chgAmp = $wp[13];
                    $cc7_pvAmp = $wp[14];
                    $cc7_pvV = $wp[15];
                    $cc7_Alert = $wp[23];
                }

                break;

            case "DC": //this is a DC flexnet line, there can be only 1 per mate
                $dc_shunt1_kWh = $wp[3]; //i'm only using shunt1
                $dc_chargemet = $wp[6];
                $dc_aux_hrs = $wp[7];
                $dc_batt_minVDC = $wp[8];
                $dc_batt_maxVDC = $wp[9];
                $dc_batt_minSOC = $wp[10];
                $dc_shunt1_A = $wp[18];
                break;

            case "MESSAGE": //a message line
                $msg_count = $msg_count + 1; //increment the message count
                $msg[$msg_count] = $wp[1];
                break;


            case "\n"; //a line feed... just ignore
                break;
            case "\r"; //a carraige return... just ignore
                break;
            case NULL:
                break;
            default: //if none of the above, file could be corrupt so exit
                exit("error: unrecognized data $wp[0]<br>");

        }

    }
    fclose($fd);//finished reading the file

} else {
    exit ("error: could not read data file");

}


/*set calculated variables here */

$fx_buyWatts = $fx1_buyAmp * $fx1_ACin + $fx2_buyAmp * $fx2_ACin;
$fx_sellWatts = $fx1_sellAmp * $fx1_ACout + $fx2_sellAmp * $fx2_ACout + $fx3_sellAmp * $fx3_ACout;


$pvmax_kW = 12;
$batt_nom_V = 48;


$fx_inverter_kWh = $fx1_inverter_kWh + $fx2_inverter_kWh + $fx3_inverter_kWh;
$fx_charger_kWh = $fx1_charger_kWh + $fx2_charger_kWh + $fx3_charger_kWh;
$fx_buy_kWh = $fx1_buy_kWh + $fx2_buy_kWh + $fx3_buy_kWh;
$fx_sell_kWh = $fx1_sell_kWh + $fx2_sell_kWh + $fx3_sell_kWh;
$fx_aux_hrs = $fx1_aux_hrs + $fx2_aux_hrs + $fx3_aux_hrs;


$fx_batt_minVDC = ($fx1_batt_minVDC <
    $fx2_batt_minVDC)?$fx1_batt_minVDC:$fx2_batt_minVDC;


$fx_batt_maxVDC = ($fx1_batt_maxVDC >
    $fx2_batt_maxVDC)?$fx1_batt_maxVDC:$fx2_batt_maxVDC;


$pv1kW = $cc4_pvV * $cc4_pvAmp * .001;
$pv2kW = $cc5_pvV * $cc5_pvAmp * .001;
$pv3kW = $cc6_pvV * $cc6_pvAmp * .001;
$pv4kW = $cc7_pvV * $cc7_pvAmp * .001;

$pvkWh = $cc4_daily_kWh + $cc5_daily_kWh + $cc6_daily_kWh + $cc7_daily_kWh;


$pvTkW = $pv1kW + $pv2kW + $pv3kW + $pv4kW;
$fx1_load_Amps = $fx1_invAmp - $fx1_sellAmp + $fx1_buyAmp - $fx1_chgAmp;
$fx2_load_Amps = $fx2_invAmp - $fx2_sellAmp + $fx2_buyAmp - $fx2_chgAmp;
$fx3_load_Amps = $fx3_invAmp - $fx3_sellAmp + $fx3_buyAmp - $fx3_chgAmp;

$load_Amps = $fx1_load_Amps + $fx2_load_Amps;

$fx1_load_kW = $fx1_load_Amps * $fx1_ACout * 0.001;
$fx2_load_kW = $fx2_load_Amps * $fx2_ACout * 0.001;
$fx3_load_kW = $fx3_load_Amps * $fx3_ACout * 0.001;

$load = ($load_Amps * (($fx1_ACout + $fx2_ACout)/2))* .001; //load in kW



$chgAmpDC = $cc3_chgAmp + $cc4_chgAmp;

$chgAmpAC = $fx1_chgAmp + $fx2_chgAmp;

$load_today_kWh = $fx_inverter_kWh - $fx_sell_kWh + $fx_buy_kWh -
    $fx_charger_kWh;

if ($fx_buy_kWh >= $fx_sell_kWh) { //are we buying or selling power
    $sellflag = FALSE;
    $fx_today_kWh = $fx_buy_kWh - $fx_sell_kWh;
}

else {
    $sellflag = TRUE;
    $fx_today_kWh = $fx_sell_kWh - $fx_buy_kWh;
}

$chg_today_kWh = round(($fx_charger_kWh + $cc3_charger_kWh +
    $cc4_charger_kWh),2);

$batt_temp = ((9/5)*$batt_temp)+32; //change to farenheit

?>