# Bank API - Account Management and Fund Transfer System

A modern REST API for account management and fund transfers built with Java 21, Spring Boot 3, and PostgreSQL.

## Project Overview

This application provides a complete solution for managing bank accounts and processing fund transfers with proper validation, security, and observability features.

## Features

- Account management (create, read, update, delete)
- Fund transfer between accounts
- JWT-based authentication and authorization
- Comprehensive error handling
- Database transaction management
- Prometheus metrics and health checks
- Docker support for containerized deployment

## Technologies Used

- Java 21
- Spring Boot 3.5.5
- Spring Data JPA
- PostgreSQL
- Spring Security
- Spring Actuator
- Maven

## Getting Started

### Prerequisites

- Java 21
- Docker (for containerized deployment)

### Running Locally

1. Clone the repository
2. Copy `.env.example` file to `.env` and configure values
3. Run `mvn spring-boot:run` to start the application

### Building

```bash
mvn clean package
```

### Testing

```bash
mvn test
```

## API Endpoints

- **Accounts**: `GET /api/accounts/{id}`, `POST /api/accounts`, `PUT /api/accounts/{id}`, `DELETE /api/accounts/{id}`
- **Transfers**: `POST /api/transfers`

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
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── AccountNotFoundException.java
│   └── InsufficientFundsException.java
└── util
    └── ValidationUtils.java
```

## Observability

The system haves a Grafana service  for observability purposes.
Import dashboard definition from `./observability/grafana_dashboard.json` In Grafana (accesible at: http://localhost:3000 [default credentials]) with preconfigured metrics.

## Deployment

The application can be containerized using the provided Dockerfile and docker-compose.yml files.

## License

This project is licensed under the MIT License.