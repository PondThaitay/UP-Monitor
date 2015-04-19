<?php
include('config.inc.php');

$monitor =$_POST["monitor"]; 

mysql_query("update monitor set flag='0' where Monitor_ID='$monitor'");

?>


