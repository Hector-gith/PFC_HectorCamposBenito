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
            $descripcion = $_POST['descripcion'];
            $ingredientes = $_POST['ingredientes'];
            $preparacion = $_POST['preparacion'];
            $restaurante = $_POST['restaurante'];
            $latitud = $_POST['latitud'];
            $longitud = $_POST['longitud'];


            $sql = "SELECT ID FROM fotos WHERE ID_U = :ID";         
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':ID', $ID);
            $stmt_check->execute();

            if($stmt_check->rowCount()==1){
                $registro=$stmt_check->fetch(PDO::FETCH_ASSOC);
                $sql = "DELETE FROM comentarios WHERE ID_F = :ID";         
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':ID', $registro["ID"]);
                $stmt_check->execute();

                $sql = "DELETE FROM recetas WHERE ID_F = :ID";         
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':ID', $registro["ID"]);
                $stmt_check->execute();

                $sql = "DELETE FROM ubicacion WHERE ID_F = :ID";         
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':ID', $registro["ID"]);
                $stmt_check->execute();

                $sql = "DELETE FROM fotos WHERE ID_U = :ID";         
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':ID', $ID);
                $stmt_check->execute();

            }
            date_default_timezone_set('Europe/Madrid');
            $timestamp_actual = date('Y-m-d H:i:s', time());
            $sql_registro = "INSERT INTO fotos (ID_U, FOTO, DESCRIPCION, FECHA) VALUES (:id_u, :foto, :descripcion, :fecha)";
            $stmt = $conn->prepare($sql_registro);
            $stmt->bindParam(':id_u', $ID);
            $stmt->bindParam(':foto', $foto);
            $stmt->bindParam(':descripcion', $descripcion);
            $stmt->bindParam(':fecha', $timestamp_actual);
            $stmt->execute();

            if( $stmt == false ) {

                echo json_encode(array("status" => "error", "message" => "Error, foto no guardada"));

            }else{
                $sql = "SELECT ID FROM fotos WHERE ID_U = :ID";         
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':ID', $ID);
                $stmt_check->execute();

                if($stmt_check->rowCount()==1){
                     $registro=$stmt_check->fetch(PDO::FETCH_ASSOC);
                    if($latitud==0){
                        $sql_registro = "INSERT INTO recetas (ID_F, INGREDIENTES, PREPARACION) VALUES (:id_f, :ingredientes, :preparacion)";
                        $stmt = $conn->prepare($sql_registro);
                        $stmt->bindParam(':id_f', $registro["ID"]);
                        $stmt->bindParam(':ingredientes', $ingredientes);
                        $stmt->bindParam(':preparacion', $preparacion);
                        $stmt->execute();
                    }else{

                        $sql_registro = "INSERT INTO ubicacion (ID_F, RESTAURANTE, LATITUD, LONGITUD) VALUES (:id_f, :restaurante, :latitud, :longitud)";
                        $stmt = $conn->prepare($sql_registro);
                        $stmt->bindParam(':id_f', $registro["ID"]);
                        $stmt->bindParam(':restaurante', $restaurante);
                        $stmt->bindParam(':latitud', $latitud);
                        $stmt->bindParam(':longitud', $longitud);
                        $stmt->execute();
                    }
                   
                    echo json_encode(array("status" => "success", "message" => "Foto guardada correctamente"));
                
                }

                  
               

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