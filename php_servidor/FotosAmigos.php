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

            

            break;

        case 'GET':
            

            $id = $_GET['id'];
            $fotos = array();
            date_default_timezone_set('Europe/Madrid');
            $fechaHoy = date('Y-m-d');

            
            $sql = "SELECT ID, FOTO, DESCRIPCION, FECHA FROM fotos WHERE ID_U = :id";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id);
            $stmt_check->execute();
            if($stmt_check->rowCount()>=1){
                $registro=$stmt_check->fetch(PDO::FETCH_ASSOC);
                $fechaFoto = date('Y-m-d', strtotime($registro["FECHA"]));

                    if($fechaFoto==$fechaHoy){

                        $sql = "SELECT COUNT(*) FROM comentarios WHERE ID_F = :id_f";
                        $stmt_check1 = $conn->prepare($sql);
                        $stmt_check1->bindParam(':id_f', $registro["ID"]);
                        $stmt_check1->execute();
                        $cuenta = $stmt_check1->fetchColumn();
                        $horafoto = date('H:i', strtotime($registro["FECHA"]));
                        
                        $sql = "SELECT * FROM ubicacion WHERE ID_F = :id_f";         
                        $stmt_check = $conn->prepare($sql);
                        $stmt_check->bindParam(':id_f', $registro["ID"]);
                        $stmt_check->execute();

                        if($stmt_check->rowCount()==1){
                            $registro1=$stmt_check->fetch(PDO::FETCH_ASSOC);
                            
                            $restaurante = $registro1['RESTAURANTE'];
                            $latitud = $registro1['LATITUD'];
                            $longitud = $registro1['LONGITUD'];
                            
                            $foto = array(
                            'ID' => $registro['ID'],
                            'NICK' => "TÚ",
                            'FOTO_PERFIL'=>"1",
                            'FOTO' => $registro['FOTO'],
                            'DESCRIPCION' => $registro['DESCRIPCION'],
                            'FECHA' => $horafoto,
                            'COMENTARIOS' => $cuenta,
                            'RESTAURANTE' => $restaurante,
                            'LATITUD' => $latitud,
                            'LONGITUD' => $longitud
                        

                            );

                        }else{

                            $sql = "SELECT * FROM recetas WHERE ID_F = :id_f";         
                            $stmt_check = $conn->prepare($sql);
                            $stmt_check->bindParam(':id_f', $registro["ID"]);
                            $stmt_check->execute();
                            $registro1=$stmt_check->fetch(PDO::FETCH_ASSOC);

                            $ingredientes = $registro1['INGREDIENTES'];
                            $preparacion = $registro1['PREPARACION'];
                            
                            $foto = array(
                            'ID' => $registro['ID'],
                            'NICK' => "TÚ",
                            'FOTO_PERFIL'=>"1",
                            'FOTO' => $registro['FOTO'],
                            'DESCRIPCION' => $registro['DESCRIPCION'],
                            'FECHA' => $horafoto,
                            'COMENTARIOS' => $cuenta,
                            'INGREDIENTES' => $ingredientes,
                            'PREPARACION' => $preparacion


                            );
                        }
                        

                        
                        $fotos[] = $foto;
                    }
                
            }

            $sql = "SELECT * FROM amistades WHERE ID_U1 = :id OR ID_U2 = :id";
            $stmt_check = $conn->prepare($sql);
            $stmt_check->bindParam(':id', $id);
            $stmt_check->execute();
            
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
                    $sql = "SELECT ID, FOTO, DESCRIPCION, FECHA FROM fotos WHERE ID_U = :id";
                    $stmt_check2 = $conn->prepare($sql);
                    $stmt_check2->bindParam(':id', $registro["ID"]);
                    $stmt_check2->execute();
                    if($stmt_check2->rowCount()>=1){
                        $registro2=$stmt_check2->fetch(PDO::FETCH_ASSOC);
                        $fechaFoto = date('Y-m-d', strtotime($registro2["FECHA"]));
            
                            if($fechaFoto==$fechaHoy){
                                $sql = "SELECT COUNT(*) FROM comentarios WHERE ID_F = :id_f";
                                $stmt_check3 = $conn->prepare($sql);
                                $stmt_check3->bindParam(':id_f', $registro2["ID"]);
                                $stmt_check3->execute();
                                $cuenta = $stmt_check3->fetchColumn();
                                $horafoto = date('H:i', strtotime($registro2["FECHA"]));

                                $sql = "SELECT * FROM ubicacion WHERE ID_F = :id_f";         
                                $stmt_check4 = $conn->prepare($sql);
                                $stmt_check4->bindParam(':id_f', $registro2["ID"]);
                                $stmt_check4->execute();

                                if($stmt_check4->rowCount()==1){
                                    $registro3=$stmt_check4->fetch(PDO::FETCH_ASSOC);
                                    
                                    $restaurante = $registro3['RESTAURANTE'];
                                    $latitud = $registro3['LATITUD'];
                                    $longitud = $registro3['LONGITUD'];
                                    
                                    $foto = array(
                                    'ID' => $registro2['ID'],
                                    'NICK' => $registro['NICK'],
                                    'FOTO_PERFIL'=>$registro['FOTO_PERFIL'],
                                    'FOTO' => $registro2['FOTO'],
                                    'DESCRIPCION' => $registro2['DESCRIPCION'],
                                    'FECHA' => $horafoto,
                                    'COMENTARIOS' => $cuenta,
                                    'RESTAURANTE' => $restaurante,
                                    'LATITUD' => $latitud,
                                    'LONGITUD' => $longitud
                                

                                    );

                                }else{

                                    $sql = "SELECT * FROM recetas WHERE ID_F = :id_f";         
                                    $stmt_check4 = $conn->prepare($sql);
                                    $stmt_check4->bindParam(':id_f', $registro2["ID"]);
                                    $stmt_check4->execute();
                                    $registro3=$stmt_check4->fetch(PDO::FETCH_ASSOC);

                                    $ingredientes = $registro3['INGREDIENTES'];
                                    $preparacion = $registro3['PREPARACION'];
                                    
                                    $foto = array(
                                    'ID' => $registro2['ID'],
                                    'NICK' => $registro['NICK'],
                                    'FOTO_PERFIL'=>$registro['FOTO_PERFIL'],
                                    'FOTO' => $registro2['FOTO'],
                                    'DESCRIPCION' => $registro2['DESCRIPCION'],
                                    'FECHA' => $horafoto,
                                    'COMENTARIOS' => $cuenta,
                                    'INGREDIENTES' => $ingredientes,
                                    'PREPARACION' => $preparacion


                                    );
                                }
                                
                                $fotos[] = $foto;
                            }
                    }

                }
                
                
            }

            echo json_encode($fotos);
                
            }else{
            echo json_encode($fotos);
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