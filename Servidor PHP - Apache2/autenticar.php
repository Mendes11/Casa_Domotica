<?php

class Autenticar{
	// constructor
	
    function __construct() {

    }

    // destructor
    function __destruct() {
        // closing db connection
        
    }
	
	function isAuthentic($iIDUsuario){
		addslashes($iIDUsuario);
		$sql = "Select iIDUsuario from Usuarios where iIDUsuario = ".$iIDUsuario.";";
//		var_dump($GLOBALS["__mysqli_ston"]);
		$result = mysqli_query($GLOBALS["___mysqli_ston"], $sql);
//		$result = mysqli_query($sql);
//		var_dump($result);
		if(!empty($result)){
			if (mysqli_num_rows($result) > 0) {
//				echo "achou";
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}

?>
