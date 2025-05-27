# Buscar automáticamente el pod de MariaDB en el namespace parcial3
$podName = kubectl get pods -n parcial3 --no-headers -o custom-columns=":metadata.name" | Where-Object { $_ -like "mariadb-car-*" } | Select-Object -First 1

if (-not $podName) {
    Write-Error "No se encontró ningún pod que comience con 'mariadb-car-'"
    exit
}

$podName = $podName.Trim()
Write-Host "Pod encontrado: $podName"

$dbUser = "root"
$dbPass = "root"
$dbName = "carsdb"
$backupFile = "respaldo\respaldo_$(Get-Date -Format 'yyyyMMdd_HHmmss').sql"

if (-Not (Test-Path "respaldo")) {
    New-Item -ItemType Directory -Path "respaldo"
}

Write-Host "Generando respaldo en el archivo '$backupFile'..."

kubectl exec -n parcial3 $podName -- bash -c "MYSQL_PWD='$dbPass' mariadb-dump -u $dbUser $dbName" > $backupFile

if ($LASTEXITCODE -eq 0) {
    Write-Host "Respaldo creado correctamente en '$backupFile'."
} else {
    Write-Error "Error al crear el respaldo."
}
