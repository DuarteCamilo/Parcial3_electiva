# Script para desplegar la aplicación en Kubernetes

# Construir la imagen Docker de la aplicación
Write-Host "Construyendo imagen Docker..."
docker build -t car-microservice:latest .

# Crear directorio kubernetes si no existe
if (-not (Test-Path -Path "kubernetes")) {
    New-Item -ItemType Directory -Path "kubernetes"
}

# Desplegar en Kubernetes
Write-Host "Desplegando servicios en Kubernetes..."

# Aplicar configuraciones de MariaDB
kubectl apply -f kubernetes/mariadb-pv.yaml
kubectl apply -f kubernetes/mariadb-pvc.yaml
kubectl apply -f kubernetes/mariadb-deployment.yaml
kubectl apply -f kubernetes/mariadb-service.yaml

# Esperar a que MariaDB esté listo
Write-Host "Esperando a que MariaDB esté listo..."
Start-Sleep -Seconds 30

# Desplegar los cinco servicios
$services = @("create", "delete", "update", "getbyid", "getall")

foreach ($service in $services) {
    kubectl apply -f "kubernetes/$service-deployment.yaml"
    kubectl apply -f "kubernetes/$service-service.yaml"
}

# Desplegar Ingress
kubectl apply -f kubernetes/ingress.yaml

Write-Host "Verificando el estado de los pods..."
kubectl get pods

Write-Host "Verificando el estado de los servicios..."
kubectl get services

Write-Host "Todos los servicios han sido desplegados correctamente."