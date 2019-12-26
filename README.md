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

# TODO
- add API for investor account display
- add tests for LoanEntity & other tests on services
- add overall documentation
- describe happy-path (maybe put a test in Spring)
- replace entity constructors to a Builder 
