<?php
$columNames = array('iIDLuminosidade','iSetpoint','iSensor','bControle','bManualOn');
$sql = "Select iIDLuminosidade, iSetpoint, iSensor, JbControle, bManualOn, from Luminosidade;";
# Informa qual o conjunto de caracteres será usado.
header('Content-Type: text/html; charset=utf-8');
$response = array();
$response['success'] = false;
$response['errorCode'] = 0;
$response['errorMessage'] = "";
$response['data'] = array();
$userID = -1;
// include db connect class
require_once __DIR__ . '/db_connect.php';
require_once __DIR__ . '/autenticar.php';

// connecting to db
$db = new DB_CONNECT();
$autentic = new Autenticar();
if(!empty($_GET)){
	if(!empty($_GET['args'])){
		$args = $_GET['args'];
		addcslashes($args);
		$sql .= " where ".$args.";";
	}
	if(!empty($_GET['json_data'])){
		try{
			$json_data = ($_GET['json_data']);
			$json_data = stripslashes(html_entity_decode($json_data));
			$json_data=json_decode($json_data,true);
		}catch(Exception $e){
			
		}
	}else{
		$response['success'] = false;
		$response['errorCode'] = 4;
		$response['errorMessage'] = "Argumentos inválidos.";
		echo json_encode($response);
		die;
	}
	$userID = intval($_GET['iIDUsuario']);
}
if($autentic->isAuthentic($userID)){
# Informa qual o conjunto de caracteres será usado.
	mysqli_query($GLOBALS["___mysqli_ston"], "SET NAMES 'utf8'");
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_connection=utf8');
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_client=utf8');
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_results=utf8');
	$hasDifference = false;
	while(!$hasDifference){
		$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql);
		if(!empty($result)){
			if (mysqli_num_rows($result) > 0) {
				$linha = array();
				$myArray = array();
				while ($resultado = mysqli_fetch_array($result)) {
					foreach($columNames as $name){
						$linha[$name] = $resultado[$name];
					}
					array_push($myArray,$linha);
				}
				$ret = checkArray($myArray[0],$json_data);
				if(!$ret){
					$response['data'] = $myArray;
					$response['success'] = true;
					$hasDifference = true;
				}
			}else{
				$response['success'] = false;
				$response['errorCode'] = 1;
				$response['errorMessage'] = "Não há dados referentes a pesquisa.";
				$hasDifference = true;
			}
		}else{
			$response['success'] = false;
			$response['errorCode'] = 2;
			$response['errorMessage'] = "Erro ao realizar comunicação.";
			$hasDifference = true;
		}
	}
}else{
	$response['success'] = false;
	$response['errorCode'] = 3;
	$response['errorMessage'] = "Erro de autenticação.";
	$hasDifference = true;
}
	echo json_encode($response);
	die;
	
function checkArray($arr1,$arr2){
	global $columNames;
	$ret = true;
	foreach($columNames as $name){
		if($arr1[$name] != $arr2[$name]){
			$ret = false;
		}
	}
	return $ret;
}
?>