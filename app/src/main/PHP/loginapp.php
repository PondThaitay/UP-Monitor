<?php
include("config.inc.php");
$username=$_POST['Username'];
$password=$_POST['Password'];

$result=mysql_query("Select count(*) from user where Username='$username' and Password='$password'");
$res=mysql_fetch_array($result);
$intnumrow=$res[0];
if($intnumrow==0){
	$arr['Username']="0";
	$arr['Error']="Username Or Password Wrong";
}else{
	$userresult=mysql_query("select * from user where Username='$username' and Password='$password'");
	$resuser=mysql_fetch_array($userresult);
	$arr['Username']=$resuser['Username'];
	$arr['Error']="";

}

echo json_encode($arr);


?>