# Bank Microservices

## Description
This project implements a REST API for handling bank accounts using Java and the Spring Framework. It is designed as a microservice with multi-currency support and core banking operations such as depositing and debiting money, retrieving account balances, and performing currency exchanges using fixed conversion rates.

### Features

- **Multi-Currency Support**: Manage balances in multiple currencies (EUR, USD, SEK, RUB).
- **Deposit Money**: Add funds to an account in a specified currency.
- **Debit Money**: Withdraw funds in a specific currency (without automatic currency exchange).
- **Retrieve Balances**: Get account balances across all supported currencies.
- **Currency Exchange**: Exchange currency balances using fixed conversion rates.
- **Microservices**: the project is divided into 2 microservices: Account Service and Currency Exchange Service, each can run independently, in its own Docker container.
- **Persistence**: Account and transaction data are stored in an H2 embedded database.
- **Security**: account actions only for authenticated users
- **External System Simulation**: implemented as REST API request for currency exchange  from Account Service to Currency Exchange Service
- **External System Simulation**: OpenAPI with swagger UI
- **Preceeded data**: Some test data are preseeded, you can use account numbers 1 and 2. 

### Limitations
- No tests
- Not all validations, only basic ones
- Logging only to the console
- In-memory basic authentication

## Setup Instructions

### Prerequisites
- Java 21 or higher
- Maven 3.8+
- Docker (optional, for containerized setup)

### Steps to Set Up and Run the Application

#### 1. Clone the Repository
```bash
git clone <repository-url>
cd bank-microservices
```

#### 2. Build the Project
```bash
mvn clean install
```

#### 3. Run the Application
```bash
mvn spring-boot:run
```

The application will start at `http://localhost:8080`.


#### Optional: Run in Docker
Build the Docker image:
   ```bash
   docker-compose up --build
   ```
This will build and start two Docker containers, one for Account Service and the second for Currency Exchange Service

## API Documentation

### Base URL
```
http://localhost:8080/
```

### Endpoints
#### Default configuration
- Public is only /ping endpoint, to check if service is up
- For account operations use authenticated requests with username 'johndoe' and password 'password1'
- Project is preceeded with some test data. You can use accountId 1 and 2
- If you want to change preseeded data change account-service/src/main/resources/data.sql 

#### 1. Add Money to Account
- **POST** `/accounts/{accountId}/deposit`
- Content-Type : application/json
- **Request Body**:
  ```json
  {
    "amount": 11,
    "currency": "USD"
  }
  ```
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Deposit successful",
    "transaction": {
        "transactionId": 1,
        "amount": 11,
        "currency": "USD",
        "timestamp": "2025-01-14T17:59:10.158079694",
        "accountId": 1,
        "transactionType": "DEPOSIT"
    }
  }
  ```

#### 2. Debit Money from the Account
- **POST** `/accounts/{accountId}/withdraw`
- Content-Type : application/json
- **Request Body**:
  ```json
  {
    "amount": 10.00,
    "currency": "USD"
  }
  ```
- **Response**:
  ```json
  {
    "status": "success",
    "message": "Withdrawal successful",
    "transaction": {
        "transactionId": 2,
        "amount": 10.00,
        "currency": "USD",
        "timestamp": "2025-01-14T18:00:18.660477379",
        "accountId": 1,
        "transactionType": "WITHDRAW"
    }
  }
  ```

#### 3. Get Account Balance
- **GET** `/accounts/{accountId}/balances`
- **Response**:
  ```json
  [
    {
        "currency": "EUR",
        "amount": 500.00
    },
    {
        "currency": "USD",
        "amount": 1001.00
    }
  ]
  
  ```

#### 4. Exchange Currency
- **POST** `/accounts/{accountId}/exchange`
- Content-Type : application/json
- **Request Body**:
  ```json
  {
    "fromCurrency": "EUR",
    "toCurrency": "SEK",
    "amount": 1
  }
  ```
- **Response**:
  ```json
  {
    "transactions": [
        {
            "transactionId": 3,
            "amount": 1,
            "currency": "EUR",
            "timestamp": "2025-01-14T18:03:35.847578612",
            "accountId": 1,
            "transactionType": "WITHDRAW"
        },
        {
            "transactionId": 4,
            "amount": 11.259,
            "currency": "SEK",
            "timestamp": "2025-01-14T18:03:35.848499838",
            "accountId": 1,
            "transactionType": "DEPOSIT"
        }
    ],
    "status": "success",
    "message": "Currency exchange completed successfully"
  }
  ```
#### 5. Check if the service is up
- **GET** `/ping`
- **Response**:
  `pong`

#### 6. SwaggerUI fro OpenAPI
- `http://localhost:8080/api/swagger-ui/index.html`

### Endpoints for Currency Exchange Service
#### Base URL
```
http://localhost:8081/api
```
#### Default configuration
- You can communicate with Currency Exchange Service without Account Service
- All endpoints are public
- Currency exchange rates are seeded from currency-exchange-service/src/main/resources/rates.json


#### 1. Convert currency
- **POST** `/convert`
- Content-Type : application/json
- **Request Body**:
  ```json
  {
    "fromCurrency": "EUR",
    "toCurrency": "USD",
    "amount": 10000
  }
  ```
- **Response**:
  ```json
  {
    "toCurrency": "USD",
    "convertedAmount": 10036.0
  }
  ```
#### 2. Show all supported currencies
- **GET** `/currencies`
- **Response**:
  ```json
  [
    "EUR",
    "USD",
    "SEK",
    "RUB"
  ]
  
  ```
#### 3. Check if the service is up
- **GET** `/ping`
- **Response**:
  `pong`

#### 4. SwaggerUI fro OpenAPI
- `http://localhost:8081/swagger-ui`
