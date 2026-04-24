#!/bin/bash
# Script para compilar y ejecutar el Proyecto 3
# Árbol Binario de Búsqueda — Tarjetas DC Comics

# Crear directorio de clases compiladas si no existe
mkdir -p out

echo "Compilando..."

# El flag -sourcepath permite que javac resuelva todas las dependencias automáticamente
javac -encoding UTF-8 -d out -sourcepath src src/proyecto3/Main.java

if [ $? -eq 0 ]; then
    echo "Compilación exitosa."
    echo "Iniciando aplicación..."
    java -cp out proyecto3.Main
else
    echo "Error de compilación. Revise los mensajes anteriores."
    exit 1
fi
