<?php

// array for JSON response
$response = array();
$queryResponse = array();
$response['result'] = array();
// check for required fields
if (isset($_POST['sql'])) {
	$codigo = null;
	$relacao = array();
	if (isset($_POST['codigo'])) {
		$codigo = $_POST['codigo'];
	}
    $sql = json_decode($_POST['sql']);

    // include db connect class
    require_once __DIR__ . '/db_connect.php';

    // connecting to db
    $db = new DB_CONNECT();
	
	// SQL pode ser um array de sqls, para cada um ele ir� realizar o post e as verifica��es necess�rias.
	foreach($sql as $query){
    // mysql inserting a new row
	$queryResponse = null;
	$queryResponse = array();
	try{
		while(preg_match("/(#(\d)#)/",$query,$match)){
			$query = preg_replace("/(#.*?#){1}/",$relacao[$match[2]],$query,1);
		}
	}catch(Exception $e){
		$queryResponse['erroRelacao'] = $e;
	}
    $result = mysqli_query($GLOBALS["___mysqli_ston"], $query);
    // check if row inserted or not
		if ($result) {
			// retorna, se pedir, o c�digo do auto_increment
			if ($codigo != null){
				$sql2 = "SELECT @@IDENTITY 'teste';";
				$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql2);
				if(!empty($result)){
					$result = mysqli_fetch_array($result);
					$queryResponse["codigo"] = $result["teste"];
					array_push($relacao,$result["teste"]);
				}
			}
			// successfully inserted into database
			$queryResponse["success"] = 1;

			// echoing JSON response
			
		} else {
			// failed to insert row
			$queryResponse["success"] = 0;
			//$queryResponse["erro"] = mysql_errno();
			$queryResponse["erro"] = mysqli_errno($GLOBALS["___mysqli_ston"]);
			$queryResponse["codigo"] = 0;
			
			// echoing JSON response
		}
		array_push($response['result'],$queryResponse);
	}
	echo json_encode($response);
} else {
    // required field is missing
    $queryResponse["success"] = 0;
	array_push($response['result'],$queryResponse);
    // echoing JSON response
    echo json_encode($response);
}
?>