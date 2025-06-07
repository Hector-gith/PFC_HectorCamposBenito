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
            // Manejar operaciones de escritura (crear/actualizar/eliminar)
            $correo = $_POST['correo'];
            $nick = $_POST['nick'];
            $contrasena = $_POST['contrasena'];


            // Verificar si el correo está repetido
            $sql_correo = "SELECT COUNT(*) FROM usuarios WHERE correo = :correo";
            $stmt_check = $conn->prepare($sql_correo);
            $stmt_check->bindParam(':correo', $correo);
            $stmt_check->execute();
            $cuenta = $stmt_check->fetchColumn();

            if ($cuenta > 0) {
                echo json_encode(array("status" => "error", "message" => "correo repetido"));
            } else {
                $sql_correo = "SELECT COUNT(*) FROM usuarios WHERE nick = :nick";
                $stmt_check = $conn->prepare($sql_correo);
                $stmt_check->bindParam(':nick', $nick);
                $stmt_check->execute();
                $cuenta = $stmt_check->fetchColumn();
                if($cuenta>0){
                    echo json_encode(array("status" => "error", "message" => "Este nick ya tiene dueño"));
                }else{
                    $pass_cifrado = password_hash($contrasena, PASSWORD_DEFAULT);

                    $sql_registro = "INSERT INTO usuarios (correo, nick, contrasena) VALUES (:correo, :nick, :contrasena)";
                    $stmt = $conn->prepare($sql_registro);
                    $stmt->bindParam(':correo', $correo);
                    $stmt->bindParam(':nick', $nick);
                    $stmt->bindParam(':contrasena', $pass_cifrado);
                    $stmt->execute();
    
                    if( $stmt == false ) {
    
                        echo json_encode(array("status" => "error", "message" => "Usuario no registrado"));
    
                    }else{
                        echo json_encode(array("status" => "success", "message" => "Usuario registrado"));
    
                    }
    
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