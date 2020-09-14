# hop
## Backend part
Project by Łukasz Jankowski.
Shop type application.

### Prerequisites
- JDK 14
- Maven
### Build in:
- Spring Boot
- Spring Data JPA
- H2 Database
- Spring Security and JWT
- IntelliJ IDEA 2020.1 Ultimate Edition

This is the back of the application running in Angular: [Shop - FrontEnd part!](https://github.com/Lukas-max/shop-frontend).
Data loading is done by Class implementing CommandLineRunner to in memory database. So `the whole backend code is ready to be copied/downloaded and run from the compiler.`

### Features
```
- loading data to memory - products, categories and users (with one admin account).
- Spring security - basic and JSON Web Token auth (However the active implem. is now JWT auth)
- REST - providing endpoints for frontend app. 
 * Product categories endpoint  [ GET ]
 * Products endpoint [ GET, DELETE, POST, PUT ]
 * User endpoint. [ GET, POST ]
- User registration.
- Admin - adding, deleting, upgrading products.
```
