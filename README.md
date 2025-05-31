# 🚗 Parcial 3 - Electiva: Gestión de Autos con Microservicios y Kubernetes

Este proyecto es una aplicación de microservicios desarrollada en **Spring Boot**, diseñada para gestionar información de vehículos. Utiliza **Docker** y **Kubernetes** para la orquestación de contenedores, incluyendo mecanismos de respaldo y restauración de base de datos.

---

## 🧩 Estructura del Proyecto

- 📁 src/ → Código fuente del proyecto Spring Boot
- 📁 kubernetes/ → Archivos de configuración para despliegue en Kubernetes
- 📁 respaldo/ → Carpeta de respaldos de base de datos
- 📁 .vscode/, .mvn/ → Configuraciones del entorno
- 📄 Dockerfile → Imagen Docker para la aplicación
- 📝 documentacion.docx → Documentación del proyecto
- 📄 *.ps1 → Scripts de PowerShell
- 📄 test_api.sh → Script de prueba de la API en Bash
- 📄 pom.xml → Configuración Maven


---

## 🚀 Requisitos Previos

Antes de comenzar asegúrate de tener instalado:

- [Docker](https://docs.docker.com/get-docker/)
- [Kubernetes CLI (kubectl)](https://kubernetes.io/docs/tasks/tools/)
- [Maven](https://maven.apache.org/install.html)
- PowerShell (Windows) o Bash (Linux/macOS)

---
## ⚙️ Instalación y Ejecución

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/yourusername/Parcial3_electiva.git
cd Parcial3_electiva

## 2️⃣ Compilar el proyecto con Maven
```bash
./mvnw clean install
---

## 3️⃣ Construir la imagen Docker
```bash
docker build -t parcial3-autos .
---

## 4️⃣ Desplegar en Kubernetes
```bash
./deploy-kubernetes.ps1
---

## 🧪 Pruebas de la API
```bash
test_api.sh
---

## 💾 Respaldo y Restauración
```bash
./crearRespaldo.ps1
```

## 🔁 Usar respaldo existente
```bash
./usarRespaldo.ps1
---

## 🔍 Ver datos actuales
```bash
./verDatos.ps1
---

## 📝 Documentación Adicional
- 📄 documentacion.docx – Descripción general del sistema
- 📄 Diagrama_Kubernetes.drawio – Diagrama de despliegue en Kubernetes
