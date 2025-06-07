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

            $id_u = $_POST['id'];
            $id_f = $_POST['id_f'];
            $comentario = $_POST['comentario'];

            date_default_timezone_set('Europe/Madrid');
            $fecha = date('H:i');

            $sql_registro = "INSERT INTO comentarios (ID_U, ID_F, COMENTARIO, FECHA) VALUES (:id_u, :id_f, :comentario, :fecha)";
            $stmt = $conn->prepare($sql_registro);
            $stmt->bindParam(':id_u', $id_u);
            $stmt->bindParam(':id_f', $id_f);
            $stmt->bindParam(':comentario', $comentario);
            $stmt->bindParam(':fecha', $fecha);
            $stmt->execute();

            if( $stmt == false ) {

                echo json_encode(value: array("status" => "error", "message" => "Comentario no enviado"));

            }else{
                echo json_encode(array("status" => "success", "message" => "Comentario enviado"));

            }

            break;

        case 'GET':

            $id_f = $_GET['id'];

            $sql = "SELECT ID_U, COMENTARIO, FECHA FROM comentarios WHERE ID_F = :id";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id_f);
            $stmt_check->execute();
            
            $comentarios = array();
            
            if($stmt_check->rowCount()>=1){

            while ($row = $stmt_check->fetch(PDO::FETCH_ASSOC)) {
               
                    $sql = "SELECT NICK FROM usuarios WHERE ID = :id";         
                    $stmt_check1 = $conn->prepare($sql);
                    $stmt_check1->bindParam(':id', $row["ID_U"], PDO::PARAM_STR);
                    $stmt_check1->execute();
                
                
                if($stmt_check1->rowCount()==1){

                    $registro=$stmt_check1->fetch(PDO::FETCH_ASSOC);

                    $comentario = array(
                        'ID_U' => $row['ID_U'],
                        'NICK' => $registro['NICK'],
                        'COMENTARIO' => $row['COMENTARIO'],
                        'FECHA' => $row['FECHA']

                    );
                     $comentarios[] = $comentario;
                }
                
                
            }

            echo json_encode($comentarios);
                
            }else{
            echo json_encode($comentarios);
            }

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