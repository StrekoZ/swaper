## Description
Project is a demonstration of a simple p2p lending platform

Application build using:
- SpringBoot 2 (REST, JPA, CDI)
- in-memory H2 Database
- Swagger 
  
## Project Launch
**Prerequisites**
- Java 8 (Oracle)
- Maven 3.2.5+

**Build project**

`mvn clean install`

**Launch application locally**

`mvn spring-boot:run`

## Work with application
Once application is launched open http://localhost:8080/ in your browser. There will be redirect to Swagger UI

Application have built-in users: `admin` (password 'admin') and `investor` (password 'investor'). New investors can be registered via REST API

### Happy-Path scenario
1. Login with `admin` user
2. Create a Loan
3. Logout
4. Register new Investor
5. Login with investor user
6. Invest into a loan
7. Logout
8. Login with `admin` user
9. Do Loan payment
10. Logout
11. Login with investor user
12. Validate investor account
13. Logout


# Application structure
## 3 Application Layers
* **REST Services** - consists of: REST endpoints & models for presentation
* **Application services** - contains business logic.
* **Persistence** layer with entities

## Security
For user authentication and authorization spring-security is used. Investor 

## Entity mapping
For entity to REST Model transformation - OrikaMapper is used

## Storage
For demo purposes H2 in-memory database is used

## Exception handling
All exceptions which are thrown by REST Services are handled by 'CustomizedResponseEntityExceptionHandler' to provide customized error message

## Logging/AuditMonitoring
Is not implemented, as wan't pointed in requirements  


# TODO
- add tests for LoanEntity & other tests on services
- add overall documentation
- BigDecimal scale is not explicitly validated during incoming request