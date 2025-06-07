<?php
header('Content-Type: application/json');

$servername = "localhost";
$username = "u931748780_root";
$password = "Telacatola1459.";
$dbname = "u931748780_bddmeal";

try {
    // Crear conexión PDO
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    $request_method = $_SERVER['REQUEST_METHOD'];

    switch ($request_method) {

        case 'POST':
            $ID = $_POST['ID'];
            $foto = $_POST['foto'];


            $sql = "SELECT Foto_Perfil FROM usuarios WHERE ID = :ID";         
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':ID', $ID);
            $stmt_check->execute();

            if($stmt_check->rowCount()==1){
                $sql = "UPDATE usuarios set Foto_Perfil=:foto_perfil WHERE ID = :ID";
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':ID', $ID);
                $stmt_check->bindParam(':foto_perfil', $foto);
                $stmt_check->execute();
                if ($stmt_check->rowCount()==1) {
                    echo json_encode(array("status" => "success", "message" => "Foto actualizada"));

                }else{
                    echo json_encode(array("status" => "error", "message" => "Error al actualizar la foto"));

                }
                //echo json_encode(array("status" => "success", "message" => "hola: ".$foto));

                
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