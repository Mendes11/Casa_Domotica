<?php
$columNames = array('iIDJanela','cNome','iSetpoint','iPos','bAvancado','cDeviceName');
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
$sql = "Select Janela.iIDJanela, Janela.cNome, Janela.iSetpoint, Janela.iPos, Janela.bAvancado, DeviceStatus.cNome as 'cDeviceName' from Janela inner join DeviceStatus on DeviceStatus.iIDDeviceStatus = Janela.iIDDeviceStatus;";
if(!empty($_GET)){
	if(!empty($_GET['args'])){
		$args = $_GET['args'];
		addcslashes($args);
		$sql .= " where ".$args.";";
	}
	$userID = intval($_GET['iIDUsuario']);
}
if($autentic->isAuthentic($userID)){
# Informa qual o conjunto de caracteres será usado.
	mysqli_query($GLOBALS["___mysqli_ston"], "SET NAMES 'utf8'");
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_connection=utf8');
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_client=utf8');
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_results=utf8');
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
			$response['data'] = $myArray;
			$response['success'] = true;
		}else{
			$response['success'] = false;
			$response['errorCode'] = 1;
			$response['errorMessage'] = "Não há dados referentes a pesquisa.";
		}
	}else{
		$response['success'] = false;
		$response['errorCode'] = 2;
		$response['errorMessage'] = "Erro ao realizar comunicação.";
	}
}else{
	$response['success'] = false;
	$response['errorCode'] = 3;
	$response['errorMessage'] = "Erro de autenticação.";
}
	echo json_encode($response);

?>