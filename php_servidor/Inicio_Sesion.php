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

            $correo = $_POST['correo'];
            $contrasena = $_POST['contrasena'];

            // Verificar si el correo está repetido
            $sql_correo = "SELECT * FROM usuarios WHERE Correo = :correo";         
            $stmt_check = $conn->prepare($sql_correo);
            $correoTrim = htmlentities(addslashes(trim($correo)));
            $stmt_check->bindParam(':correo', $correoTrim);
            $stmt_check->execute();

            if($stmt_check->rowCount()==1){
                $registro=$stmt_check->fetch(PDO::FETCH_ASSOC);
                $pssw = htmlentities(addslashes($contrasena));
                if(password_verify($pssw, $registro["CONTRASENA"])){
                    echo json_encode(array("status" => "success", "message" => $registro["ID"]));
                }else{
                    echo json_encode(array("status" => "error", "message" => "La contraseña o usuario no son correctos"));
                }
            }else{
                echo json_encode(array("status" => "error", "message" => "El usuario no existe"));

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