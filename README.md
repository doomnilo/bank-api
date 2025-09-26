# Bank API - Account Management and Fund Transfer System

A modern REST API for account management and fund transfers built with Java 21, Spring Boot 3, and PostgreSQL.

## Project Overview

This application provides a complete solution for managing bank accounts and processing fund transfers with proper validation, security, and observability features.

## Features

- Account management (create, read, update, delete)
- Fund transfer between accounts with comprehensive validation
- Transfer history querying by sender account
- JWT-based authentication and authorization
- Comprehensive error handling with custom exceptions
- Database transaction management with rollback support
- Extensive test coverage (>85% across all layers)
- Prometheus metrics and health checks
- Docker support for containerized deployment
- Resilience patterns (circuit breaker, retry, rate limiting)

## Technologies Used

- Java 21
- Spring Boot 3.5.5
- Spring Data JPA
- PostgreSQL
- Spring Security
- Spring Actuator
- Resilience4j
- Liquibase
- Maven
- JUnit 5 & Mockito (Testing)
- Hibernate Validator

## Getting Started

### Prerequisites

- Java 21
- Docker (for containerized deployment)

### Running Locally

1. Clone the repository
2. Copy `.env.example` file to `.env` and configure values
3. Run the application:

```bash
# Using Maven (if installed)
mvn spring-boot:run

# Using Maven Wrapper (recommended)
./mvnw spring-boot:run
```

### Building

```bash
# Using Maven Wrapper (recommended)
./mvnw clean package

# Using Maven (if installed)
mvn clean package
```

### Testing

```bash
# Run all tests (73 total tests) - Maven Wrapper
./mvnw test

# Run specific test class
./mvnw test -Dtest=TransferControllerTest

# Run tests with verbose output
./mvnw test -Dtest=TransferServiceImplTest -X

# Using Maven (if installed)
mvn test
```

**Test Coverage:**
- 73 total tests with 100% success rate
- >85% code coverage across all application layers
- Complete controller, service, and exception testing
- Bean validation testing for DTOs and entities

## API Endpoints

### Account Management
- `GET /api/v1/accounts/{id}` - Get account by ID
- `GET /api/v1/accounts` - Get all accounts
- `POST /api/v1/accounts` - Create new account
- `PUT /api/v1/accounts/{id}` - Update account
- `DELETE /api/v1/accounts/{id}` - Delete account

### Transfer Operations
- `GET /api/v1/transfers/{id}` - Get transfer by ID
- `GET /api/v1/transfers` - Get all transfers
- `GET /api/v1/transfers/account/{accountId}` - Get transfers by sender account (NEW)
- `POST /api/v1/transfers` - Create new transfer

### Health & Monitoring
- `GET /actuator/health` - Application health status
- `GET /actuator/prometheus` - Prometheus metrics

## Project Structure

```
com.tlchallenge.bankapi
├── BankApiApplication.java
├── config
│   ├── SecurityConfig.java
│   └── WebConfig.java
├── controller
│   ├── AccountController.java
│   └── TransferController.java
├── model
│   ├── Account.java
│   ├── Transfer.java
│   └── dto
│       ├── AccountDto.java
│       ├── TransferDto.java
│       └── ErrorResponse.java
├── repository
│   ├── AccountRepository.java
│   └── TransferRepository.java
├── service
│   ├── AccountService.java
│   ├── TransferService.java
│   └── impl
│       ├── AccountServiceImpl.java
│       └── TransferServiceImpl.java
└── exception
    ├── GlobalExceptionHandler.java
    ├── AccountNotFoundException.java
    ├── TransferRejectedException.java
    └── InvalidAccountDataException.java
```

## Observability

The system has a Grafana service for observability purposes.
Import dashboard definition from `./observability/grafana_dashboard.json` in Grafana (accessible at: http://localhost:3000 [default credentials]) with preconfigured metrics.

## Deployment

The application can be containerized using the provided Dockerfile and docker-compose.yml files.

### Running Locally with Docker

1. **Standard Development Setup:**
```bash
docker-compose up -d --build
```

2. **Containerized Environment:**

Configure `.env.containerized` file with these values:
- `DATASOURCE_URL=db:5432`
- `LOKI_URL=loki:3100`

Then run:
```bash
docker-compose --env-file .env.containerized up -d --build
```

### Available Environment Files
- `.env.example` - Template for environment variables
