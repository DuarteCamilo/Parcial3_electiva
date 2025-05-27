param(
    [string]$backupFile = ""
)

# Buscar automáticamente el pod de MariaDB en el namespace parcial3
Write-Host "Buscando pod de MariaDB en el namespace 'parcial3'..."
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

if ($backupFile -eq "") {
    $latestBackup = Get-ChildItem -Path "respaldo\*.sql" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($latestBackup) {
        $backupFile = $latestBackup.FullName
    } else {
        Write-Error "No se encontraron archivos de respaldo en la carpeta 'respaldo'."
        exit
    }
} else {
    if (-Not [System.IO.Path]::IsPathRooted($backupFile)) {
        $backupFile = Join-Path (Get-Location) $backupFile
    }
}

if (-Not (Test-Path $backupFile)) {
    Write-Error "Archivo de respaldo no encontrado en '$backupFile'. Verifica la ruta."
    exit
}

Write-Host "Restaurando respaldo '$backupFile' en la base '$dbName' del pod '$podName'..."

# Restaurar base de datos
Get-Content -Path $backupFile -Raw | kubectl exec -i -n parcial3 $podName -- bash -c "MYSQL_PWD='$dbPass' mariadb -u $dbUser $dbName"

if ($LASTEXITCODE -eq 0) {
    Write-Host "Restauración completada exitosamente."
} else {
    Write-Error "Error durante la restauración."
}
