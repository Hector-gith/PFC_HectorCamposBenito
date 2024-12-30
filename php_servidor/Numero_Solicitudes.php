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
        case 'POST':


            break;

        case 'GET':

            $id = $_GET['id'];

            $sql = "SELECT COUNT(*) FROM Solicitudes WHERE ID_RECEPTOR = :id";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id);
            $stmt_check->execute();
            $cuenta = $stmt_check->fetchColumn();        

            echo json_encode($cuenta);
                

            break;

        default:
            header("HTTP/1.0 405 Method Not Allowed");
            echo json_encode(array("status" => "error", "message" => "Método no soportado"));
            break;
    }
} catch(PDOException $e) {
    echo json_encode(array("status" => "error", "message" => "Connection failed: " . $e->getMessage()));
}

$conn = null;
?>