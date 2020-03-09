<?php
header('Content-type: application/json;');
// include db connect class
require_once __DIR__ . '/db_connect.php';
require_once __DIR__ . '/autenticar.php';

// connecting to db
$db = new DB_CONNECT();
$autentic = new Autenticar();
$response = array();
$response['success'] = false;
$response['errorCode'] = 0;
$response['errorMessage'] = "";
$response['data'] ="";
$userID = -1;
$json_data = null;
if(!empty($_POST)){
	$userID = $_POST['iIDUsuario'];
	try{
			$json_data = $_POST['json_data'];
			$json_data = stripslashes(html_entity_decode($json_data));
			$json_data=json_decode($json_data,true);
		}catch(Exception $e){
			
		}
	## Primeiro anula todos os pedidos anteriores.
	$sql = "Update UserPortaoLog set bPending = 1 where iIDPortao = ".$json_data['iIDPortao'].";";
	$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql);
	if($result){
		$sql = "Insert into UserPortaoLog (bOpen,fAutoCloseTime,iIDPortao,iIDUsuario) values(".$json_data['bOpen'].",".floatval($json_data['fAutoCloseTime']).",".$json_data['iIDPortao'].",".$userID.");";
		$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql);
		if($result){
			$sql2 = "SELECT @@IDENTITY 'teste';";
			$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql2);
			if(!empty($result)){
				$result = mysqli_fetch_array($result);
				$iIDUserPortaoLog = $result["teste"];
				while($response['success'] != true){
					$sql = "Select * from UserPortaoLog where bPending = 0 and iIDUserPortaoLog = ".$iIDUserPortaoLog.";";
					$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql);
					if(!empty($result)){
						if(mysqli_num_rows($result) == 0){
							$response['success'] = true;
						}
					}
				}
			}
			$response['success'] = true;
		}else{
			$response['errorCode'] = mysqli_errno($GLOBALS["___mysqli_ston"]);
			$response['errorMessage'] = "Erro ao inserir dados.\n".$sql."\n".mysqli_error($GLOBALS["___mysqli_ston"]);
		}
	}else{
		$response['success'] = false;
		$response['errorCode'] = mysqli_errno($GLOBALS["___mysqli_ston"]);
		$response['errorMessage'] = "Erro ao Cancelar todos os pedidos pendentes anteriores. Erro :".mysqli_error($GLOBALS["___mysqli_ston"]);
	}
}
echo json_encode($response)
?>