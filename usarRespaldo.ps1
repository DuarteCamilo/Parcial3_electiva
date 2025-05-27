# usarRespaldo.ps1
param(
    [string]$backupFile = ""
)

# Buscar automáticamente el pod de MariaDB
Write-Host "Buscando pod de MariaDB..."
$podName = kubectl get pods --no-headers -o custom-columns=":metadata.name" | Where-Object { $_ -like "mariadb-car-*" } | Select-Object -First 1

if (-not $podName) {
    Write-Error "No se encontró ningún pod que comience con 'mariadb-car-'"
    exit
}

$podName = $podName.Trim()  # Limpiar espacios en blanco
Write-Host "Pod encontrado: $podName"

$dbUser = "root"
$dbPass = "root"
$dbName = "carsdb"

# Si no se proporciona archivo, usar el más reciente
if ($backupFile -eq "") {
    $latestBackup = Get-ChildItem -Path "respaldo\*.sql" | Sort-Object LastWriteTime -Descending | Select-Object -First 1
    if ($latestBackup) {
        $backupFile = $latestBackup.FullName
    } else {
        Write-Error "No se encontraron archivos de respaldo en la carpeta 'respaldo'."
        exit
    }
} else {
    # Si se proporciona ruta relativa, convertir a absoluta
    if (-Not [System.IO.Path]::IsPathRooted($backupFile)) {
        $backupFile = Join-Path (Get-Location) $backupFile
    }
}

if (-Not (Test-Path $backupFile)) {
    Write-Error "Archivo de respaldo no encontrado en '$backupFile'. Verifica la ruta."
    exit
}

Write-Host "Restaurando respaldo '$backupFile' en la base '$dbName' del pod '$podName'..."

# Método 1: Usar MYSQL_PWD (recomendado)
Get-Content -Path $backupFile -Raw | kubectl exec -i $podName -- bash -c "MYSQL_PWD='$dbPass' mariadb -u $dbUser $dbName"

# Método alternativo si el anterior no funciona:
# kubectl exec -i $podName -- bash -c "mariadb -u $dbUser -p$dbPass $dbName" < $backupFile

if ($LASTEXITCODE -eq 0) {
    Write-Host "Restauración completada exitosamente."
} else {
    Write-Error "Error durante la restauración."
}