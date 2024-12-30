<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "u178650151_root";
$password = "Telacatola1459.";
$dbname = "u178650151_bddmeal";

try {
    // Crear conexión PDO
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    $request_method = $_SERVER['REQUEST_METHOD'];

    switch ($request_method) {

        case 'GET':
            $ID = $_GET['ID'];

            // Verificar si el correo está repetido
            $sql = "SELECT Nick, Foto_Perfil FROM usuarios WHERE ID = :ID";         
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':ID', $ID);
            $stmt_check->execute();

            if($stmt_check->rowCount()==1){
                $registro=$stmt_check->fetch(PDO::FETCH_ASSOC);

                echo json_encode($registro);
                
            }else{
                echo json_encode(array("error" => "Error"));
            }

            break;

        default:
            // Método no soportado
            header("HTTP/1.0 405 Method Not Allowed");
            echo json_encode(array("status" => "error", "message" => "Método no soportado"));
            break;
    }
} catch(PDOException $e) {
    // Si hay un error, mostrar el mensaje de error
    echo json_encode(array("status" => "error", "message" => "Connection failed: " . $e->getMessage()));
}

// Cerrar la conexión
$conn = null;
?>