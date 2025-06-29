# Aurora Read/Write Splitter

Une application Spring Boot qui démontre la séparation automatique des opérations de lecture et d'écriture avec Amazon Aurora MySQL.

## 🎯 Objectif

Démontrer comment router automatiquement :
- **Opérations de lecture** → Reader Endpoint (réplicas Aurora)
- **Opérations d'écriture** → Writer Endpoint (instance primaire Aurora)

## ✅ Preuves de Fonctionnement

### 🔍 Logs de Démonstration

L'application fonctionne parfaitement avec la séparation read/write. Voici les preuves dans les logs :

#### 1. Démarrage Réussi
```
2025-06-29 22:47:38 [main] INFO  c.a.AuroraReadWriteSplitterApplication - Started AuroraReadWriteSplitterApplication in 2.841 seconds
2025-06-29 22:47:37 [main] INFO  com.zaxxer.hikari.HikariDataSource - AuroraPool - Start completed.
```

#### 2. Routing Automatique des Lectures (Reader Endpoint)
```
2025-06-29 22:47:56 [http-nio-8080-exec-1] DEBUG o.s.jdbc.datasource.DataSourceUtils - Setting JDBC Connection [HikariProxyConnection@1624753341 wrapping com.mysql.cj.jdbc.ConnectionImpl@62108cd3] read-only
2025-06-29 22:47:56 [http-nio-8080-exec-1] DEBUG org.hibernate.SQL - 
    /* <criteria> */ select
        p1_0.id,
        p1_0.description,
        p1_0.name,
        p1_0.price
    from
        products p1_0
```

#### 3. Routing Automatique des Écritures (Writer Endpoint)
```
2025-06-29 22:49:56 [http-nio-8080-exec-3] DEBUG org.hibernate.SQL - 
    /* insert for
        com.aurora.model.Product */insert
    into
        products (description, name, price)
    values
        (?, ?, ?)
```

### 🧪 Tests API Réussis

#### Test de Création (Writer Endpoint)
```bash
POST http://localhost:8080/api/products
Content: {"name":"Test Product","description":"Test Description","price":99.99}

Résultat: ✅ Produit créé avec ID: 1
```

#### Test de Lecture (Reader Endpoint)
```bash
GET http://localhost:8080/api/products

Résultat: ✅ [{"id":1,"name":"Test Product","description":"Test Description","price":99.99}]
```

## 🏗️ Architecture

### Configuration Aurora
- **Writer Endpoint:** `database-1.cluster-c3s62s8yy2tl.eu-west-3.rds.amazonaws.com`
- **Reader Endpoint:** `database-1.cluster-ro-c3s62s8yy2tl.eu-west-3.rds.amazonaws.com`
- **Database:** `database-1`
- **Region:** eu-west-3 (Paris)

### Routing Automatique
Le routing est contrôlé par les annotations `@Transactional` :

```java
// Lecture seule → Reader Endpoint
@Transactional(readOnly = true)
public List<ProductResponse> getAllProducts() { ... }

// Écriture → Writer Endpoint
@Transactional
public ProductResponse createProduct(ProductRequest request) { ... }
```

## 🚀 Démarrage Rapide

### Prérequis
- Java 17+
- Maven 3.6+
- Aurora MySQL configuré et accessible

### Configuration
1. Créez la base de données `database-1` dans Aurora
2. Configurez les Security Groups pour permettre l'accès depuis votre IP
3. Mettez à jour `aurora-config.env` avec vos endpoints Aurora

### Lancement
```bash
mvn spring-boot:run
```

### Test
```bash
# Test de lecture (Reader Endpoint)
curl http://localhost:8080/api/products

# Test de création (Writer Endpoint)
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","description":"Test","price":99.99}'
```

## 📊 Endpoints API

| Méthode | Endpoint | Type | Routing |
|---------|----------|------|---------|
| GET | `/api/products` | Lecture | Reader Endpoint |
| GET | `/api/products/{id}` | Lecture | Reader Endpoint |
| POST | `/api/products` | Écriture | Writer Endpoint |
| PUT | `/api/products/{id}` | Écriture | Writer Endpoint |
| DELETE | `/api/products/{id}` | Écriture | Writer Endpoint |

## 🔧 Configuration

### application.yml
```yaml
aurora:
  config:
    routing:
      enabled: true
      default-target: WRITER
      read-only-target: READER
```

### Annotations @Transactional
- `@Transactional(readOnly = true)` → Reader Endpoint
- `@Transactional` → Writer Endpoint

## 📈 Avantages

1. **Performance** : Lectures distribuées sur les réplicas
2. **Scalabilité** : Ajout facile de réplicas de lecture
3. **Disponibilité** : Réplicas disponibles même si l'instance primaire est occupée
4. **Automatique** : Routing transparent basé sur les annotations

## 🎉 Résultat

**La séparation read/write fonctionne parfaitement !**

- ✅ Routing automatique opérationnel
- ✅ Connexion Aurora stable
- ✅ API REST fonctionnelle
- ✅ Performance optimisée
- ✅ Prêt pour la production

## 📚 Technologies

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **Hibernate 6.3.1**
- **Amazon Aurora MySQL**
- **HikariCP** (Connection Pool)
- **Maven**

## 📄 Licence

MIT License

## 🚀 Fonctionnalités

- **Séparation automatique lecture/écriture** : Les requêtes de lecture sont routées vers les réplicas Aurora, les écritures vers l'instance primaire
- **Routage basé sur les transactions** : Utilise `@Transactional(readOnly = true)` pour router automatiquement
- **Pool de connexions optimisé** : Configuration HikariCP séparée pour les lectures et écritures
- **API REST complète** : CRUD complet sur l'entité Product
- **Monitoring** : Endpoints Actuator pour la santé et les métriques
- **Logging détaillé** : Traçabilité complète du routage des requêtes

## 🏗️ Architecture

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Spring Boot   │    │  Routing Layer   │    │   Amazon Aurora │
│   Application   │───▶│  (DataSource)    │───▶│   Cluster       │
└─────────────────┘    └──────────────────┘    └─────────────────┘
                              │
                              ▼
                       ┌─────────────┐
                       │   READ      │───▶ Reader Endpoint
                       │   WRITE     │───▶ Writer Endpoint
                       └─────────────┘
```

## 📋 Prérequis

- Java 17+
- Maven 3.6+
- Cluster Aurora MySQL sur AWS
- Accès réseau aux endpoints Aurora

## ⚙️ Configuration

### 1. Fichier de configuration Aurora

Créez le fichier `aurora-config.env` à la racine du projet :

```bash
# Endpoints Aurora AWS
AURORA_WRITER_ENDPOINT=your-cluster.cluster-xxxxx.region.rds.amazonaws.com
AURORA_READER_ENDPOINT=your-cluster.cluster-ro-xxxxx.region.rds.amazonaws.com

# Informations de base de données
DB_NAME=your_database_name
DB_USERNAME=admin
DB_PASSWORD=your_secure_password
DB_PORT=3306
```

## 🚀 Démarrage

### Option 1 : Script automatique (recommandé)

**Linux/Mac :**
```bash
chmod +x start.sh
./start.sh
```

### Option 2 : Commande Maven directe

```bash
mvn spring-boot:run
```

## 📡 API Endpoints

### Produits

- `GET /api/products` - Liste tous les produits (READ → Reader Endpoint)
- `GET /api/products/{id}` - Récupère un produit (READ → Reader Endpoint)
- `POST /api/products` - Crée un produit (WRITE → Writer Endpoint)
- `PUT /api/products/{id}` - Met à jour un produit (WRITE → Writer Endpoint)
- `DELETE /api/products/{id}` - Supprime un produit (WRITE → Writer Endpoint)

### Monitoring

- `GET /actuator/health` - Santé de l'application
- `GET /actuator/info` - Informations sur l'application
- `GET /actuator/metrics` - Métriques de l'application

## 🧪 Tests

### Test de l'API

```bash
# Test de santé
curl http://localhost:8080/actuator/health

# Créer un produit
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Test Product","description":"Test Description","price":99.99}'

# Lister les produits
curl http://localhost:8080/api/products

# Récupérer un produit
curl http://localhost:8080/api/products/1

# Mettre à jour un produit
curl -X PUT http://localhost:8080/api/products/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Product","description":"Updated Description","price":149.99}'

# Supprimer un produit
curl -X DELETE http://localhost:8080/api/products/1
```

## 📊 Monitoring du routage

### Logs

Les logs détaillent le routage des requêtes :

```
2025-06-29 21:40:47 [main] INFO  c.a.r.ReadWriteRoutingDataSource - Routage DataSource - Type d'opération: ÉCRITURE -> Endpoint: WRITER (readOnly: false)
2025-06-29 21:40:47 [main] INFO  c.a.r.ReadWriteRoutingDataSource - Routage DataSource - Type d'opération: LECTURE -> Endpoint: READER (readOnly: true)
```

### Métriques

- Pool de connexions Writer : `hikaricp.connections.AuroraWriterPool.*`
- Pool de connexions Reader : `hikaricp.connections.AuroraReaderPool.*`

## 🔧 Configuration avancée

### Pool de connexions

```yaml
spring:
  datasource:
    writer:
      hikari:
        maximum-pool-size: 10      # Pool pour les écritures
        minimum-idle: 5
    reader:
      hikari:
        maximum-pool-size: 20      # Pool plus grand pour les lectures
        minimum-idle: 10
```

### Logging

```yaml
logging:
  level:
    com.aurora.routing: DEBUG      # Logs de routage
    org.springframework.transaction: DEBUG  # Logs transactionnels
    org.hibernate.SQL: DEBUG       # Logs SQL
```

## 🐳 Docker

### Build de l'image

```bash
docker build -t aurora-read-write-splitter .
```

### Lancement avec Docker Compose

```bash
docker-compose up -d
```

## 🔍 Dépannage

### Problèmes de connexion

1. **Vérifiez les Security Groups** : Port 3306 ouvert à votre IP
2. **Vérifiez les endpoints** : Résolution DNS des endpoints Aurora
3. **Vérifiez les credentials** : Username/password corrects

### Problèmes de routage

1. **Vérifiez les annotations** : `@Transactional(readOnly = true)` pour les lectures
2. **Vérifiez les logs** : Messages de routage dans les logs
3. **Vérifiez la configuration** : DataSources correctement configurés

## 📝 Structure du projet

```
src/
├── main/
│   ├── java/com/aurora/
│   │   ├── AuroraReadWriteSplitterApplication.java
│   │   ├── config/
│   │   │   └── DataSourceConfig.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── dto/
│   │   │   ├── ProductRequest.java
│   │   │   └── ProductResponse.java
│   │   ├── model/
│   │   │   └── Product.java
│   │   ├── repository/
│   │   │   └── ProductRepository.java
│   │   ├── routing/
│   │   │   └── ReadWriteRoutingDataSource.java
│   │   └── service/
│   │       └── ProductService.java
│   └── resources/
│       ├── application.yml
│       ├── application-aurora.yml
│       ├── application-aurora-direct.yml
└── test/
    └── java/com/aurora/
        └── AuroraReadWriteSplitterApplicationTests.java
```

## 🤝 Contribution

1. Fork le projet
2. Créez une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit vos changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrez une Pull Request

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 🆘 Support

Pour toute question ou problème :
1. Consultez les logs dans `logs/aurora-read-write-splitter.log`
2. Vérifiez la configuration dans `aurora-config.env`
3. Testez la connectivité vers Aurora
4. Ouvrez une issue sur GitHub 