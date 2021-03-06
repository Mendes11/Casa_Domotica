<?php
set_time_limit(2);
$columNames = array('iUserCont');
# Informa qual o conjunto de caracteres será usado.
header('Content-Type: text/html; charset=utf-8');
header('Content-type: application/json;');
$response = array();
$response['success'] = false;
$response['errorCode'] = 0;
$response['errorMessage'] = "";
$response['data'] = array();
$userID = -1;
$json_data = null;
// include db connect class
require_once __DIR__ . '/db_connect.php';
require_once __DIR__ . '/autenticar.php';
require_once __DIR__ . '/checkJanela.php';
require_once __DIR__ . '/checkTomadas.php';
require_once __DIR__ . '/checkLuminosidade.php';
// connecting to db
$db = new DB_CONNECT();
$autentic = new Autenticar();
$janela = new checkJanela();
$tomada = new checkTomadas();
$luminosidade = new checkLuminosidade();
$janelaJson = null;
$tomadaJson = null;
$luminosidadeJson = null;
$contadorPessoas = null;


if(!empty($_GET)){
	if(!empty($_GET['json_data'])){
		try{
			$json_data = ($_GET['json_data']);
			$json_data = stripslashes(html_entity_decode($json_data));
			$json_data=json_decode($json_data,true);
			if(!empty($json_data['Janela'])){
				$janelaJson = $json_data['Janela'];
			}
			if(!empty($json_data['Tomadas'])){
				$tomadaJson = $json_data['Tomadas'];
			}
			if(!empty($json_data['Luminosidade'])){
				$luminosidadeJson = $json_data['Luminosidade'];
			}
			if(!empty($json_data['Sala'])){
				$salaJson = $json_data['Sala'];
			}
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
	$auxResponse = null;
#	while(!$hasDifference){
		usleep(100);
		if(!empty($janelaJson)){
			$ret = $janela->check($janelaJson);
			if($ret['hasDifference']){
				$auxResponse['Janela'] = $ret['data'];
				$response['success'] = true;
				$hasDifference = true;
			}
		}
		usleep(100);
		if(!empty($tomadaJson)){
			$ret = $tomada->check($tomadaJson);
			if($ret['hasDifference']){
				$auxResponse['Tomadas'] = $ret['data'];
				$response['success'] = true;
				$hasDifference = true;
			}
		}
		usleep(100);
		if(!empty($luminosidadeJson)){
			$ret = $luminosidade->check($luminosidadeJson);
			if($ret['hasDifference']){
				$auxResponse['Luminosidade'] = $ret['data'];
				$response['success'] = true;
				$hasDifference = true;
			}
		}
		usleep(100);
		if(!empty($salaJson)){
			$ret = check($salaJson);
			if($ret['hasDifference']){
				$auxResponse['Sala'] = $ret['data'];
				$response['success'] = true;
				$hasDifference = true;
			}
		}
#	}
	if(!empty($auxResponse)){
		$response['data'] = $auxResponse;
	}else{
		$response['data'] = null;
		$response['errorCode'] = 6;
	}
}else{
	$response['success'] = false;
	$response['errorCode'] = 3;
	$response['errorMessage'] = "Erro de autenticação.";
}
	echo json_encode($response);

	die;
function check($json_data){
	global $columNames;
	$sql = "Select iUserCont from Comodos where iIDComodo = 1;";
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
					$ret = checkArray($linha,$json_data);
					if(!$ret){
						$response['hasDifference'] = true;
					}
				}
				$response['data'] = $myArray; #Por hora, joga uma única msm.
			}else{
				$response['hasDifference'] = false;
				$response['errorCode'] = 1;
				$response['errorMessage'] = "Não há dados referentes a pesquisa.";
			}
		}else{
			$response['hasDifference'] = false;
			$response['errorCode'] = 2;
			$response['errorMessage'] = "Erro ao realizar comunicação.";
		}
		return $response;
}
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
