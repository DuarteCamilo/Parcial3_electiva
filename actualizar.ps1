# Script para actualizar los servicios y deployments en Kubernetes

Write-Host "Actualizando servicios y deployments en Kubernetes..."

# Lista de componentes a actualizar
$components = @(
    "create-deployment.yaml", "create-service.yaml",
    "delete-deployment.yaml", "delete-service.yaml",
    "update-deployment.yaml", "update-service.yaml",
    "getbyid-deployment.yaml", "getbyid-service.yaml",
    "getall-deployment.yaml", "getall-service.yaml",
    "ingress.yaml"
)

foreach ($component in $components) {
    $filePath = "kubernetes\$component"
    if (Test-Path -Path $filePath) {
        Write-Host "Aplicando '$component'..."
        kubectl apply -f $filePath
    } else {
        Write-Warning "Archivo no encontrado: $filePath"
    }
}

Write-Host "Actualización completada."

# Verificar estado
Write-Host "`nEstado actual de los pods:"
kubectl get pods

Write-Host "`nEstado actual de los servicios:"
kubectl get services

Write-Host "`nReglas de Ingress:"
kubectl get ingress
