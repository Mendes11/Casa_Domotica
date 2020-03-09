<?php
class checkLuminosidade{
	
	function __construct() {
		$this->columNames = array('iIDLuminosidade','iSetpoint','iSensor','bControle','bManualOn','iIDComodo');
    }

    // destructor
    function __destruct() {
        // closing db connection
        
    }
	
	function check($json_data){
	# Informa qual o conjunto de caracteres será usado.
		$this->sql = "Select iIDLuminosidade, iSetpoint, iSensor, bControle, bManualOn, iIDComodo from Luminosidade";
		$response = array();
		$response['hasDifference'] = false;
		$response['errorCode'] = 0;
		$response['errorMessage'] = "";
		mysqli_query($GLOBALS["___mysqli_ston"], "SET NAMES 'utf8'");
		mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_connection=utf8');
		mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_client=utf8');
		mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_results=utf8');
		$myArray = array();
		$this->sql .= " where iIDComodo = ".$json_data['iIDComodo'].";";
		$result = mysqli_query($GLOBALS["___mysqli_ston"], $this->sql);
		if(!empty($result)){
			if (mysqli_num_rows($result) > 0) {
				$linha = array();
				while ($resultado = mysqli_fetch_array($result)) {
					foreach($this->columNames as $name){
						$linha[$name] = $resultado[$name];
					}
					array_push($myArray,$linha);
				}
				$ret = self::checkData($myArray[0],$json_data);
				if(!$ret){
					$response['hasDifference'] = true;
				}
				$response['data'] = $myArray;
			}else{
				if($response['hasDifference'] == true){
					## Para casos em que houve mudanças, para não perder a pesquisa.
					$response['errorCode'] = 8; ## Eu deveria criar um padrão disso urgente...
					$response['errorMessage'] = "Dados referentes ao id ".$json_data['iIDLuminosidade']." não foram encontrados...";
				}else{
					$response['hasDifference'] = false;
					$response['errorCode'] = 1;
					$response['errorMessage'] = "Não há dados referentes a pesquisa.";
				}
			}
		}else{
			$response['hasDifference'] = false;
			$response['errorCode'] = 2;
			$response['errorMessage'] = "Erro ao realizar comunicação.";
		}
	return $response;
}
	function checkData($d1,$d2){
		$ret = true;
		foreach($this->columNames as $name){
			if($d1[$name] != $d2[$name]){
				$ret = false;
			}
		}
		return $ret;
	}
	function checkArray($arr1,$arr2){
			foreach($arr1 as $aux){
				$isDiff = true;
				foreach($arr2 as $aux2){
					if($isDiff){
						$ret = self::checkData($aux,$aux2);
						if($ret){
							$isDiff = false;
						}
					}else{
						
					}
				}
				if($isDiff){
					return false; ## Retorna que não são iguais...
				}
			}
			return true;
	}
		
}
?>