$dbUser = "root"
$dbPass = "root"
$dbName = "carsdb"

# Buscar el pod que comienza con "mariadb-car-"
$podName = kubectl get pods --no-headers | Where-Object { $_ -match "^mariadb-car-" } | ForEach-Object { ($_ -split '\s+')[0] } | Select-Object -First 1

if (-not $podName) {
    Write-Host "No se encontró ningún pod que comience con 'mariadb-car-'."
    exit 1
}

Write-Host "Usando el pod: $podName"
Write-Host "Obteniendo tablas de la base de datos '$dbName' en el pod $podName..."

# Ejecutar SHOW TABLES usando el comando mariadb sin ruta absoluta
$tablaOutput = kubectl exec $podName -- bash -c "mariadb -u $dbUser -p$dbPass -e 'SHOW TABLES;' $dbName"
$tablas = $tablaOutput -split "`n" | Select-Object -Skip 1  # Saltar cabecera

Write-Host "Tablas encontradas:"
foreach ($tabla in $tablas) {
    if ($tabla.Trim() -ne "") {
        Write-Host " - $tabla"
    }
}

foreach ($tabla in $tablas) {
    if ($tabla.Trim() -ne "") {
        Write-Host "`nContenido de la tabla '$tabla':"
        # Ejecutar SELECT * y capturar la salida usando mariadb sin ruta absoluta
        $contenido = kubectl exec $podName -- bash -c "mariadb -u $dbUser -p$dbPass -e 'SELECT * FROM $tabla;' $dbName"
        Write-Host $contenido
    }
}
