
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="refresh" content="65;url=dials.php">
        <title></title>
    </head>
    <body>



	<?php
                    require_once("wpdata.php");
					require_once("phpdial_gauge.php");
					echo "Status as of<br>";
					echo "$status_when "; 
					
          
                    $gauge_name="arrays.png";
					$val = $pvTkW * 1000;
                    $my_gauge = new dial_gauge($val,true,0,10000);
                    //header("Content-Type: image/png");	// png output
                    $my_gauge->display_png($gauge_name);
                ?>
                <table>
                    <tr>
                        <td><img src="<?php echo $gauge_name;?>" title="ARRAYS" alt="PV" height="250" width="250"/> </td>
                            <?php
                               $gauge_name="inverters.png";
							    if($fx_sellWatts > 0) {
									$dial = $fx_sellWatts;
									$label = "SELLING (watts)";
									
								}
								if ($fx_buyWatts > 0) {
									$dial = -1 * $fx_buyWatts;
									$label = "BUYING (watts)";
									}
									
                                $my_gauge = new dial_gauge((int)$dial,true,-10000,10000);
                                //header("Content-Type: image/png");	// png output
                                $my_gauge->display_png($gauge_name);
                                ?>
                        <td><img src="<?php echo $gauge_name;?>" title="INVERTERS" alt="inverters" height="250" width="250"/> </td>
 </td>
                    </tr>
                    </tr>
                    <tr>
                       <td>    <div align="center" class="style1">PV (watts)</div></td>
                      <td>    <div align="center" class="style1"><?php echo $label;?></div></td>
                  </tr>
                </table>

</body>
</html>