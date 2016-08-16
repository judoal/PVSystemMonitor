<!--
To change this template, choose Tools | Templates
and open the template in the editor.
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="65;url=m.php">
        <title></title>
    </head>
    <body>
        <!-- parse WPState.csv and load data into variables -->
<?php require_once("wpdata.php"); ?>
Status as of<br>
<?php echo "$status_when "; ?>
<br>
<hr>
<?php
if (($fx1_buyAmp + $fx2_buyAmp + $fx3_buyAmp) >= ($fx1_sellAmp + $fx2_sellAmp + $fx3_sellAmp)) {
echo "<div class=\"buy\">BUYING $fx_buy_kWh KWH</div>";
} else {
echo "<div class=\"sell\">SELLING $fx_sell_kWh KWH</div>";
}

echo "<b>PV Panel Data</b><br />";
echo "PV1 kW: " . $pv1kW . " kW<br />";
echo "PV2 kW: " . $pv2kW . " kW<br />";
echo "PV3 kW: " . $pv3kW . " kW<br />";
echo "PV4 kW: " . $pv4kW . " kW<br />";
echo "Total PV kW: $pvTkW kW<br /><br />";

echo "PV1 kWh: $cc4_daily_kWh kWh<br />";
echo "PV2 kWh: $cc5_daily_kWh kWh<br />";
echo "PV3 kWh: $cc6_daily_kWh kWh<br />";
echo "PV4 kWh: $cc7_daily_kWh kWh<br />";
echo "Total PV kWh: $pvkWh kWh<br />";

echo "<b><br />Inverter data</b><br />";

echo "<b>Master:</b> $fx1_OPmode<br>$fx1_ACmode<br>";
echo "AC in: " . $fx1_ACin . "V<br />";
echo "AC out: " . $fx1_ACout . "V<br />";
echo "Load Amps: $fx1_load_Amps A<br />";
echo "Load kW: $fx1_load_kW kW<br />";
if ($fx1_buyAmp > 0) {
echo "<div class=\"buy\">Buying $fx1_buyAmp A</div>";
}
if ($fx1_sellAmp > 0){
echo "<div class=\"sell\">Selling $fx1_sellAmp A</div>";
}

echo "<br><b>Slave #1:</b> $fx2_OPmode<br>$fx2_ACmode<br>";
echo "AC in: " . $fx2_ACin . "v<br />";
echo "AC out: " . $fx2_ACout . "v<br />";
echo "Load Amps: $fx2_load_Amps A<br />";
echo "Load kW: $fx2_load_kW kW<br />";
if ($fx2_buyAmp > 0) {
echo "<div class=\"buy\">Buying $fx2_buyAmp A</div>";
}
if ($fx2_sellAmp > 0) {
echo "<div class=\"sell\">Selling $fx2_sellAmp A</div>";
}

echo "<br><b>Slave #2:</b> $fx3_OPmode<br>$fx3_ACmode<br>";
echo "AC in: " . $fx3_ACin . "v<br />";
echo "AC out: " . $fx3_ACout . "v<br />";
echo "Load Amps: $fx3_load_Amps A<br />";
echo "Load kW: $fx3_load_kW kW<br />";
if ($fx3_buyAmp > 0) {
echo "<div class=\"buy\">Buying $fx3_buyAmp A</div>";
}
if ($fx3_sellAmp > 0) {
echo "<div class=\"sell\">Selling $fx3_sellAmp A</div>";
}


echo "<br /> LOAD: $load kW<br>";




// show warning & error count
$warn = $fx1_warn + $fx2_warn + $fx3_warn;
$errors = $fx1_err + $fx2_err + $fx3_err;
echo "<br>FX Warnings: <b class=\"warn\">$warn</b>";
echo "<br>FX Errors: <b class=\"error\">$errors</b><br>";
echo "<div class=\"hdr\">Batteries:</div>";
//echo "SOC: $batt_soc%<br>";
echo "$batt_voltage Volts<br>";
//echo "$batt_temp F<br>";
//echo "Charge Met: $dc_chargemet<br>";
//echo "Charging from DC: $chgAmpDC Amps<br>";
//echo "Charging from AC: $chgAmpAC Amps<br>";
?>

    </body>
</html>
