<?php
include('config.inc.php');


 
$strWhere = $_POST["username"];
//$strWhere='admin';
 
$sql = "select * from website WHERE Username = '".$strWhere."' "; 
$result = mysql_query($sql);
$json = array();
 $a='0';
if(mysql_num_rows($result)){
while($row=mysql_fetch_assoc($result)){

	$idweb=$row['Website_ID'];
	$resultt = mysql_query("select * from monitor where Website_ID='$idweb' order by Monitor_ID desc");
	$dbweb=mysql_fetch_array($resultt);
	if($dbweb['Status']=='Down'){
	 $arr['Monitor_ID'] = $dbweb['Monitor_ID'];
	 $arr['Name_Site']=$row['Name_Site'];
	 $arr['Date']=$dbweb['Date'];
	 $arr['Name_url']=$row['Name_url'];
	 $arr['flag']=$dbweb['flag'];
$json['emp_info'][]=$arr;
$a='1';

}
}
}

if($a=='0'){
 $arr['Monitor_ID'] = 'NULL';
	 $arr['Name_Site']='NULL';
	 $arr['Date']='NULL';
	 $arr['Name_url']='NULL';
	 $arr['flag']='NULL';
$json['emp_info'][]=$arr;

}

//mysql_close($con);

echo json_encode($json); 


?>