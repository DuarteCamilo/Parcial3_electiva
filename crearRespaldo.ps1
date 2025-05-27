# crearRespaldo.ps1
# Buscar automáticamente el pod de MariaDB
$podName = kubectl get pods --no-headers -o custom-columns=":metadata.name" | Where-Object { $_ -like "mariadb-car-*" } | Select-Object -First 1

if (-not $podName) {
    Write-Error "No se encontró ningún pod que comience con 'mariadb-car-'"
    exit
}

Write-Host "Pod encontrado: $podName"
$dbUser = "root"
$dbPass = "root"
$dbName = "carsdb"
$backupFile = "respaldo\respaldo_$(Get-Date -Format 'yyyyMMdd_HHmmss').sql"

Write-Host "Usando el pod: $podName"
Write-Host "Generando respaldo en el archivo '$backupFile'..."

# Crear directorio de respaldo si no existe
if (-Not (Test-Path "respaldo")) {
    New-Item -ItemType Directory -Path "respaldo"
}

# Usar MYSQL_PWD para evitar warning de contraseña en línea de comandos
kubectl exec $podName -- bash -c "MYSQL_PWD='$dbPass' mariadb-dump -u $dbUser $dbName" > $backupFile

if ($LASTEXITCODE -eq 0) {
    Write-Host "Respaldo creado correctamente en '$backupFile'."
} else {
    Write-Error "Error al crear el respaldo."
}