<?php
$response = array();
$nome = array();
$resultado = array();
# Informa qual o conjunto de caracteres será usado.
header('Content-Type: text/html; charset=utf-8');

// include db connect class
require_once __DIR__ . '/db_connect.php';

// connecting to db
$db = new DB_CONNECT();

$sql = $_GET['sql'];
	$numColunas = $_GET['NumColunas'];
	$num =(int)$numColunas;
	$i = 0;
	for($i=0;$i<$num;$i++)
	{
		$nome[$i] = $_GET["$i"];
	}
	# Informa qual o conjunto de caracteres será usado.
	mysqli_query($GLOBALS["___mysqli_ston"], "SET NAMES 'utf8'");
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_connection=utf8');
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_client=utf8');
	mysqli_query($GLOBALS["___mysqli_ston"], 'SET character_set_results=utf8');
	
	$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql);
	if (!empty($result)){
		if (mysqli_num_rows($result) > 0) {
			$rows = mysqli_num_rows($result);		
			$response['linhas'] = $rows;
			$response['success'] = 1;
			$response["result"] = array();
			
			while ($resultado = mysqli_fetch_array($result)) {
			$j = 0;
			$linha = array();
			for($j=0;$j<$num;$j++)
			{
				$linha[$nome[$j]] = $resultado[$nome[$j]];
			}
			array_push($response["result"], $linha);
			}
		}else{
			$response['success'] = 0;
		}
	}else{
		$response['success'] = 0;
	}
	echo json_encode($response);
	
?>