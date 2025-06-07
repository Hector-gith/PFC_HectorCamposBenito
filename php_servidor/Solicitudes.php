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

            $id_s = $_POST['id_s'];
            $id_r = $_POST['id_r'];
            $aceptado = $_POST['aceptado'];

 
            $sql = "SELECT * FROM Solicitudes WHERE ID_SOLICITOR = :id_s AND ID_RECEPTOR = :id_r";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id_s', $id_s);
            $stmt_check->bindParam(':id_r', $id_r);
            $stmt_check->execute();

            if ($stmt_check->rowCount()==1) {
                if($aceptado==1){
                    $sql = "DELETE FROM Solicitudes WHERE ID_SOLICITOR = :id_s AND ID_RECEPTOR = :id_r";         
                    $stmt_check = $conn->prepare($sql);
                    $stmt_check->bindParam(':id_s', $id_s);
                    $stmt_check->bindParam(':id_r', $id_r);
                    $stmt_check->execute();
 
                    $sql = "DELETE FROM Solicitudes WHERE ID_SOLICITOR = :id_r AND ID_RECEPTOR = :id_s";         
                    $stmt_check = $conn->prepare($sql);
                    $stmt_check->bindParam(':id_s', $id_s);
                    $stmt_check->bindParam(':id_r', $id_r);
                    $stmt_check->execute();

                    $sql_registro = "INSERT INTO amistades (ID_U1, ID_U2) VALUES (:id_s, :id_r)";
                    $stmt = $conn->prepare($sql_registro);
                    $stmt->bindParam(':id_s', $id_s);
                    $stmt->bindParam(':id_r', $id_r);
                    $stmt->execute();

                    if( $stmt == false ) {
    
                        echo json_encode(array("status" => "error", "message" => "No se pudo crear la amistad"));
    
                    }else{
                        echo json_encode(array("status" => "success", "message" => "Ahora son amigos"));
    
                    }

    
                }else{
                    $sql = "DELETE FROM Solicitudes WHERE ID_SOLICITOR = :id_s AND ID_RECEPTOR = :id_r";         
                    $stmt_check = $conn->prepare($sql);
                    $stmt_check->bindParam(':id_s', $id_s);
                    $stmt_check->bindParam(':id_r', $id_r);
                    $stmt_check->execute();
                    echo json_encode(array("status" => "success", "message" => "Solicitud eliminada"));

                }
            } else {
    
                        echo json_encode(array("status" => "error", "message" => "Error en la solicitud, pruebe de nuevo"));
            }


            break;

        case 'GET':

            $id = $_GET['id'];

            $sql = "SELECT ID_SOLICITOR FROM Solicitudes WHERE ID_RECEPTOR = :id";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id);
            $stmt_check->execute();

                
            
            $usuarios = array();
            
            if($stmt_check->rowCount()>=1){

            while ($row = $stmt_check->fetch(PDO::FETCH_ASSOC)) {

                $sql = "SELECT ID, NICK, FOTO_PERFIL FROM usuarios WHERE ID = :id";         
                $stmt_check1 = $conn->prepare($sql);
                $stmt_check1->bindParam(':id', $row["ID_SOLICITOR"], PDO::PARAM_STR);
                $stmt_check1->execute();

                if($stmt_check1->rowCount()==1){

                    $registro=$stmt_check1->fetch(PDO::FETCH_ASSOC);

                    $usuario = array(
                        'ID' => $registro['ID'],
                        'NICK' => $registro['NICK'],
                        'FOTO_PERFIL' => $registro['FOTO_PERFIL']

                    );
                     $usuarios[] = $usuario;
                }
                
                
            }

            echo json_encode($usuarios);
                
            }else{
            echo json_encode($usuarios);
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