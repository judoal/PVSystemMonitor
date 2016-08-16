<!DOCTYPE HTML>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>PondCam</title>
</head>

<body>

<?php
$img = 'image.gif'; 
file_put_contents($img, file_get_contents('http://home.la-gordon.org:8756/snapshot.cgi?user=guest&pwd=guest&url=nul'));
?>

<img src="image.gif" ALT="pondcam" WIDTH=640 HEIGHT=640>

</body>
</html>
