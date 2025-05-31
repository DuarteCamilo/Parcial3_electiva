#!/bin/bash

# Script para probar los endpoints de la API de coches
# Autor: Camilo Duarte
# Fecha: 2025-04-12

# URL base de la API
BASE_URL="http://car-api.local/api/cars"

# Colores para la salida
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Script de prueba para API de Coches ===${NC}"
echo -e "${BLUE}=== Parcial 2 - Electiva 4 ===${NC}"
echo ""

# Función para mostrar respuestas
show_response() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}Éxito:${NC} $2"
        echo "$3"
    else
        echo -e "${RED}Error:${NC} $2"
        echo "$3"
    fi
    echo ""
}

# 1. Obtener todos los coches
echo -e "${BLUE}1. Obteniendo todos los coches...${NC}"
RESPONSE=$(curl -s -w "%{http_code}" $BASE_URL-get-all)
HTTP_CODE=${RESPONSE: -3}
BODY=${RESPONSE:0:${#RESPONSE}-3}

if [ "$HTTP_CODE" -eq 200 ]; then
    show_response 0 "Lista de coches obtenida (HTTP $HTTP_CODE)" "$BODY"
else
    show_response 1 "Error al obtener coches (HTTP $HTTP_CODE)" "$BODY"
fi

# 2. Crear un nuevo coche
echo -e "${BLUE}2. Creando un nuevo coche...${NC}"
NEW_CAR='{
    "brand": "Honda",
    "model": "Civic",
    "year": 2023,
    "color": "Azul",
    "price": 28000
}'

RESPONSE=$(curl -s -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$NEW_CAR" $BASE_URL-create)
HTTP_CODE=${RESPONSE: -3}
BODY=${RESPONSE:0:${#RESPONSE}-3}

# Extraer el ID del coche recién creado
CAR_ID=""
if [ "$HTTP_CODE" -eq 200 ] || [ "$HTTP_CODE" -eq 201 ]; then
    show_response 0 "Coche creado correctamente (HTTP $HTTP_CODE)" "$BODY"
    # Extraer el ID usando expresiones regulares
    if [[ $BODY =~ \"id\":([0-9]+) ]]; then
        CAR_ID=${BASH_REMATCH[1]}
        echo -e "${BLUE}ID del coche creado: $CAR_ID${NC}"
    else
        echo -e "${RED}No se pudo extraer el ID del coche. Usando ID por defecto.${NC}"
        # Obtener el primer coche disponible como alternativa
        FIRST_CAR=$(curl -s $BASE_URL | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
        if [ -n "$FIRST_CAR" ]; then
            CAR_ID=$FIRST_CAR
            echo -e "${BLUE}Usando ID alternativo: $CAR_ID${NC}"
        else
            CAR_ID=1
            echo -e "${RED}No se encontraron coches. Usando ID 1.${NC}"
        fi
    fi
else
    show_response 1 "Error al crear coche (HTTP $HTTP_CODE)" "$BODY"
    # Intentar obtener un ID existente
    FIRST_CAR=$(curl -s $BASE_URL | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
    if [ -n "$FIRST_CAR" ]; then
        CAR_ID=$FIRST_CAR
        echo -e "${BLUE}Usando ID existente: $CAR_ID${NC}"
    else
        CAR_ID=1
        echo -e "${RED}No se encontraron coches. Usando ID 1.${NC}"
    fi
fi

echo -e "${BLUE}Operaciones siguientes usarán el ID: $CAR_ID${NC}"
echo ""

# 3. Obtener un coche por ID
echo -e "${BLUE}3. Obteniendo coche con ID $CAR_ID...${NC}"
RESPONSE=$(curl -s -w "%{http_code}" $BASE_URL-get-by-id/$CAR_ID)
HTTP_CODE=${RESPONSE: -3}
BODY=${RESPONSE:0:${#RESPONSE}-3}

if [ "$HTTP_CODE" -eq 200 ]; then
    show_response 0 "Coche obtenido correctamente (HTTP $HTTP_CODE)" "$BODY"
else
    show_response 1 "Error al obtener coche (HTTP $HTTP_CODE)" "$BODY"
fi

# 4. Actualizar un coche
echo -e "${BLUE}4. Actualizando coche con ID $CAR_ID...${NC}"
UPDATE_CAR='{
    "brand": "Honda",
    "model": "Civic",
    "year": 2023,
    "color": "Negro",
    "price": 29500
}'

RESPONSE=$(curl -s -w "%{http_code}" -X PUT -H "Content-Type: application/json" -d "$UPDATE_CAR" $BASE_URL-update/$CAR_ID)
HTTP_CODE=${RESPONSE: -3}
BODY=${RESPONSE:0:${#RESPONSE}-3}

if [ "$HTTP_CODE" -eq 200 ]; then
    show_response 0 "Coche actualizado correctamente (HTTP $HTTP_CODE)" "$BODY"
else
    show_response 1 "Error al actualizar coche (HTTP $HTTP_CODE)" "$BODY"
fi

# 5. Eliminar un coche
echo -e "${BLUE}5. Eliminando coche con ID $CAR_ID...${NC}"
RESPONSE=$(curl -s -w "%{http_code}" -X DELETE $BASE_URL-delete/$CAR_ID)
HTTP_CODE=${RESPONSE: -3}
BODY=${RESPONSE:0:${#RESPONSE}-3}

if [ "$HTTP_CODE" -eq 204 ] || [ "$HTTP_CODE" -eq 200 ]; then
    show_response 0 "Coche eliminado correctamente (HTTP $HTTP_CODE)" "No content"
else
    show_response 1 "Error al eliminar coche (HTTP $HTTP_CODE)" "$BODY"
fi

# 6. Verificar que el coche fue eliminado
echo -e "${BLUE}6. Verificando que el coche con ID $CAR_ID fue eliminado...${NC}"
RESPONSE=$(curl -s -w "%{http_code}" $BASE_URL-get-by-id/$CAR_ID)
HTTP_CODE=${RESPONSE: -3}
BODY=${RESPONSE:0:${#RESPONSE}-3}

if [ "$HTTP_CODE" -eq 404 ]; then
    show_response 0 "Verificación correcta: el coche ya no existe (HTTP $HTTP_CODE)" "$BODY"
else
    show_response 1 "Error en la verificación: el coche todavía existe (HTTP $HTTP_CODE)" "$BODY"
fi

echo -e "${BLUE}=== Pruebas completadas ===${NC}"