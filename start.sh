#!/bin/bash

# Script de démarrage pour AuroraReadWriteSplitter
# Charge les variables d'environnement depuis aurora-config.env

echo "=== Démarrage d'AuroraReadWriteSplitter ==="
echo "Chargement des variables d'environnement depuis aurora-config.env..."

# Vérification de l'existence du fichier de configuration
if [ ! -f "aurora-config.env" ]; then
    echo "ERREUR: Le fichier aurora-config.env n'existe pas!"
    echo "Veuillez créer ce fichier avec vos paramètres Aurora AWS."
    exit 1
fi

# Chargement des variables d'environnement
export $(grep -v '^#' aurora-config.env | xargs)

# Vérification des variables requises
if [ -z "$AURORA_WRITER_ENDPOINT" ] || [ -z "$AURORA_READER_ENDPOINT" ] || [ -z "$DB_USERNAME" ] || [ -z "$DB_PASSWORD" ]; then
    echo "ERREUR: Variables d'environnement manquantes dans aurora-config.env"
    echo "Vérifiez que AURORA_WRITER_ENDPOINT, AURORA_READER_ENDPOINT, DB_USERNAME et DB_PASSWORD sont définis."
    exit 1
fi

echo "Variables d'environnement chargées:"
echo "  Writer Endpoint: $AURORA_WRITER_ENDPOINT"
echo "  Reader Endpoint: $AURORA_READER_ENDPOINT"
echo "  Database: $DB_NAME"
echo "  Username: $DB_USERNAME"

# Compilation et démarrage
echo ""
echo "Compilation du projet..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "ERREUR: Échec de la compilation"
    exit 1
fi

echo "Compilation réussie!"
echo ""
echo "Démarrage de l'application avec le profil 'aurora'..."
echo "L'application sera accessible sur: http://localhost:8080"
echo "Endpoint de santé: http://localhost:8080/actuator/health"
echo "API REST: http://localhost:8080/api/products"
echo ""

# Démarrage de l'application
mvn spring-boot:run -Dspring-boot.run.profiles=aurora 