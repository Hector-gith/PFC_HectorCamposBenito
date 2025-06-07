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

            $sql = "SELECT * FROM amistades WHERE ID_U1 = :id_s AND ID_U2 = :id_r OR ID_U1 = :id_r AND ID_U2 = :id_s";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id_s', $id_s);
            $stmt_check->bindParam(':id_r', $id_r);
            $stmt_check->execute();
            if ($stmt_check->rowCount()==1) {
                echo json_encode(value: array("status" => "error", "message" => "Ya son amigos"));

            }else{
                $sql = "SELECT * FROM Solicitudes WHERE ID_SOLICITOR = :id_s AND ID_RECEPTOR = :id_r";
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':id_s', $id_s);
                $stmt_check->bindParam(':id_r', $id_r);
                $stmt_check->execute();
    
                if ($stmt_check->rowCount()==1) {
                    echo json_encode(array("status" => "error", "message" => "Ya solicitaste a este usuario"));
                } else {
    
                    $sql_registro = "INSERT INTO Solicitudes (ID_SOLICITOR, ID_RECEPTOR) VALUES (:id_s, :id_r)";
                        $stmt = $conn->prepare($sql_registro);
                        $stmt->bindParam(':id_s', $id_s);
                        $stmt->bindParam(':id_r', $id_r);
                        $stmt->execute();
    
                        if( $stmt == false ) {
        
                            echo json_encode(value: array("status" => "error", "message" => "Error en solicitud"));
        
                        }else{
                            echo json_encode(array("status" => "success", "message" => "Solicitud enviada"));
        
                        }
                    
                }
            }
           


            break;

        case 'GET':

            $nick = $_GET['nick'];
            $id = $_GET['id'];
            $usuarios = array();

            if($nick!=""){
                $clave ='%'.$nick.'%';
                $sql = "SELECT ID, NICK, FOTO_PERFIL FROM usuarios WHERE NICK LIKE :clave";         
                $stmt_check = $conn->prepare($sql);
                $stmt_check->bindParam(':clave', $clave, PDO::PARAM_STR);
                $stmt_check->execute();
            
            
            if($stmt_check->rowCount()>=1){
    
               

            while ($row = $stmt_check->fetch(PDO::FETCH_ASSOC)) {
                if($row["ID"]!=$id){
                    $usuario = array(
                        'ID' => $row['ID'],
                        'NICK' => $row['NICK'],
                        'FOTO_PERFIL' => $row['FOTO_PERFIL']

                    );
                    $usuarios[] = $usuario;
                }
                
            }

            echo json_encode($usuarios);
                
            }else{
            echo json_encode($usuarios);
            }

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