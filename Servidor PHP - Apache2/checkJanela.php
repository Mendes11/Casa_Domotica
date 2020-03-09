<?php
class checkJanela{
	# Informa qual o conjunto de caracteres será usado.
	#header('Content-Type: text/html; charset=utf-8');
	#header('Content-type: application/json;');
	
	function __construct() {
		$this->columNames = array('iIDJanela','cNome','iSetpoint','iPos','bAvancado','cDeviceName','iIDComodo');
    }

    // destructor
    function __destruct() {
        // closing db connection
        
    }
	
	function check($json_data){
	# Informa qual o conjunto de caracteres será usado.
		$response = array();
		$response['hasDifference'] = false;
		$response['errorCode'] = 0;
		$response['errorMessage'] = "";
		$response['data'] = array();
		$sql = "Select Janela.iIDJanela, Janela.cNome, Janela.iSetpoint, Janela.iPos, Janela.bAvancado, DeviceStatus.cNome as 'cDeviceName', Janela.iIDComodo from Janela inner join DeviceStatus on DeviceStatus.iIDDeviceStatus = Janela.iIDDeviceStatus where Janela.iIDComodo = ".$json_data['iIDComodo'].";";
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
					foreach($this->columNames as $name){
						$linha[$name] = $resultado[$name];
					}
					array_push($myArray,$linha);
					$ret = self::checkArray($linha,$json_data);
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
		$ret = true;
		foreach($this->columNames as $name){
			if($arr1[$name] != $arr2[$name]){
				$ret = false;
			}
		}
		return $ret;
	}
}
?>
