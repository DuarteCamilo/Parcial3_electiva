# Script para desplegar la aplicación en Kubernetes con namespace personalizado

# Construir la imagen Docker de la aplicación
Write-Host "Construyendo imagen Docker..."
docker build -t car-microservice:latest .

# Crear directorio kubernetes si no existe
if (-not (Test-Path -Path "kubernetes")) {
    New-Item -ItemType Directory -Path "kubernetes"
}

# Crear el namespace 'parcial3' si no existe
Write-Host "Verificando si el namespace 'parcial3' existe..."
$namespaceExists = kubectl get namespace parcial3 -o json -ErrorAction SilentlyContinue
if (-not $namespaceExists) {
    Write-Host "Namespace 'parcial3' no existe. Creándolo..."
    kubectl create namespace parcial3
} else {
    Write-Host "Namespace 'parcial3' ya existe."
}

# Desplegar en Kubernetes
Write-Host "Desplegando servicios en Kubernetes dentro del namespace 'parcial3'..."

# Aplicar configuraciones de MariaDB
kubectl apply -n parcial3 -f kubernetes/mariadb-pv.yaml
kubectl apply -n parcial3 -f kubernetes/mariadb-pvc.yaml
kubectl apply -n parcial3 -f kubernetes/mariadb-deployment.yaml
kubectl apply -n parcial3 -f kubernetes/mariadb-service.yaml

# Esperar a que MariaDB esté listo
Write-Host "Esperando a que MariaDB esté listo..."
Start-Sleep -Seconds 30

# Desplegar los cinco servicios
$services = @("create", "delete", "update", "getbyid", "getall")

foreach ($service in $services) {
    kubectl apply -n parcial3 -f "kubernetes/$service-deployment.yaml"
    kubectl apply -n parcial3 -f "kubernetes/$service-service.yaml"
}

# Desplegar Ingress
kubectl apply -n parcial3 -f kubernetes/ingress.yaml

# Verificar estado de los pods y servicios
Write-Host "Verificando el estado de los pods..."
kubectl get pods -n parcial3

Write-Host "Verificando el estado de los servicios..."
kubectl get services -n parcial3

Write-Host "Todos los servicios han sido desplegados correctamente en el namespace 'parcial3'."
