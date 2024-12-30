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

            $id = $_POST['id'];
            $id_a = $_POST['id_a'];


            $sql = "DELETE FROM amistades WHERE ID_U1 = :id AND ID_U2 = :id_a OR ID_U1 = :id_a AND ID_U2 = :id";         
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id);
            $stmt_check->bindParam(':id_a', $id_a);
            $stmt_check->execute();

            if( $stmt_check == false ) {

                echo json_encode(array("status" => "error", "message" => "No se pudo crear la amistad"));

            }else{
                echo json_encode(array("status" => "success", "message" => "Amigo eliminado"));

            }

            break;

        case 'GET':

            $id = $_GET['id'];

            $sql = "SELECT * FROM amistades WHERE ID_U1 = :id OR ID_U2 = :id";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id);
            $stmt_check->execute();

                
            
            $usuarios = array();
            
            if($stmt_check->rowCount()>=1){

            while ($row = $stmt_check->fetch(PDO::FETCH_ASSOC)) {
                if($row["ID_U1"]==$id){
                    $sql = "SELECT ID, NICK, FOTO_PERFIL FROM usuarios WHERE ID = :id";         
                    $stmt_check1 = $conn->prepare($sql);
                    $stmt_check1->bindParam(':id', $row["ID_U2"], PDO::PARAM_STR);
                    $stmt_check1->execute();
    
                }else{
                    $sql = "SELECT ID, NICK, FOTO_PERFIL FROM usuarios WHERE ID = :id";         
                    $stmt_check1 = $conn->prepare($sql);
                    $stmt_check1->bindParam(':id', $row["ID_U1"], PDO::PARAM_STR);
                    $stmt_check1->execute();

                }
                
                if($stmt_check1->rowCount()==1){

                    $registro=$stmt_check1->fetch(PDO::FETCH_ASSOC);

                    $usuario = array(
                        'ID' => $registro['ID'],
                        'NICK' => $registro['NICK'],
                        'FOTO_PERFIL'=>$registro['FOTO_PERFIL']
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