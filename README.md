# iAccount - Account Center

iAccount is a robust, distributed account management system built on the **SOFABoot** framework (based on Spring Boot 3.3.2). It provides core financial account functionalities including account creation, balance querying, transaction recording, and history tracking.

## 🏗 Architecture Overview

The project follows a modular architecture designed for scalability and maintainability, adhering to standard SOFABoot/Ant Financial conventions.

- **`app/web`**: The entry point of the application. Contains REST controllers, security configurations, and web-related filters.
- **`app/biz`**: Contains the core business logic implementations (`service-impl`) and shared business components.
- **`app/core`**: Defines the domain model, core services, and repository interfaces.
- **`app/common`**: Shared infrastructure including:
    - `dal`: Data Access Layer using MyBatis and PostgreSQL.
    - `service/facade`: API definitions and DTOs (Data Transfer Objects).
    - `service/integration`: Integration with external systems.
    - `util`: Common utilities (logging, tenant handling, etc.).
- **`app/bootstrap`**: Application configuration and startup orchestration.

## 🛠 Tech Stack

- **Framework**: [SOFABoot 4.6.0](https://github.com/sofastack/sofa-boot) / Spring Boot 3.3.2
- **Language**: Java 1.8
- **Database**: PostgreSQL (Main Storage), MySQL (Optional/Legacy)
- **Data Access**: MyBatis with MyBatis Generator
- **Messaging**: Apache Kafka (Event-driven updates)
- **Service Discovery**: Nacos
- **RPC**: SOFA RPC (Bolt protocol)
- **Security**: Spring Security (Basic Auth)
- **Testing**: JUnit 5, Mockito, JaCoCo (Coverage)
- **Cache**: Redis

## 🚀 Getting Started

### Prerequisites

- JDK 1.8+
- Maven 3.6+
- Docker & Docker Compose
- PostgreSQL 14+
- Nacos (for service discovery)
- Redis

### Infrastructure Setup

Use the provided Docker Compose file to start Kafka and Zookeeper:

```bash
cd kafka-docker
docker-compose up -d
```

Ensure your PostgreSQL database is running and the `account` database is created.

### Configuration

Update the `app/web/src/main/resources/application.properties` file with your local environment settings:

- `spring.datasource.url`: Database connection string
- `spring.kafka.bootstrap-servers`: Kafka broker address
- `com.alipay.sofa.rpc.registry.address`: Nacos server address

### Build

Compile and package the application using Maven:

```bash
mvn clean install
```

### Run

Start the application from the `app/web` module:

```bash
mvn spring-boot:run -pl app/web
```
Or run the packaged JAR:
```bash
java -jar app/web/target/account_center-web.jar
```

## 🛣 API Documentation

The application exposes RESTful endpoints under the `/account/basic` prefix. All requests require Basic Authentication (`admin` / `devtest123` by default).

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/account/basic/createAccount.json` | POST | Create a new financial account |
| `/account/basic/queryAccountInfo.json` | POST | Query account details by account ID |
| `/account/basic/queryAccountInfoByUserId.json` | POST | Query account details by user ID |
| `/account/basic/queryTransactionRecord.json` | POST | Retrieve a specific transaction record |
| `/account/basic/queryTransactionHistory.json` | POST | Get transaction history for an account |
| `/account/basic/insertTransactionRecord.json` | POST | Manually insert a transaction record |

## 📊 Monitoring & Maintenance

- **Health Checks**: Accessible at `/actuator/health`
- **Metrics**: Standard Spring Boot Actuator endpoints enabled
- **Logging**: Logs are formatted and stored in the `logs/` directory, managed via `logback.xml`
- **Tracing**: Custom `TraceIdFilter` ensures all requests are traceable across logs via a unique trace ID.

## 🧪 Testing

Run all tests and generate coverage reports:

```bash
mvn verify
```
JaCoCo reports will be generated in `target/site/jacoco` for each module.
