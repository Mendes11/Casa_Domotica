<?php

/**
 * A class file to connect to database
 */
class DB_CONNECT {

    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }

    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }

    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables
        require_once __DIR__ . '/db_config.php';

        // Connecting to mysql database
        ($GLOBALS["___mysqli_ston"]  = mysqli_connect("localhost", "root", "biboca1234","Domotica")) or die("morreu");
	
        // Selecing database
//       $db = mysqli_select_db($GLOBALS["___mysqli_ston"], constant('DB_DATABASE')) or die(mysqli_error($GLOBALS["___mysqli_ston"])) or die(mysqli_error($GLOBALS["___mysqli_ston"]));
//	var_dump($GLOBALS["__mysqli_ston"]);
        // returing connection cursor
        return $GLOBALS["___mysqli_ston"];
    }

    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        ((is_null($___mysqli_res = mysqli_close($GLOBALS["___mysqli_ston"]))) ? false : $___mysqli_res);
    }

}

?>
