# Shop
## Backend part
Project by Łukasz Jankowski.
Shop type application.

### Prerequisites
- JDK 14
- Maven
- PostgreSQL database
### Build in:
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Spring Security and JWT
- IntelliJ IDEA 2020.1 Ultimate Edition

This is the back end of the application. The other part -> [Shop - FrontEnd part!](https://github.com/Lukas-max/shop-frontend).
Data loading is done by Class implementing CommandLineRunner to postgreSQL database. `So if you have postgre you just connect those those and it's ready to go`

## RUN
Copy/download, run on compiler. Only one thing to configure is you database connection. Whole configuration is in Spring application.properties.

### Features
- Loading data to memory - products, categories and users (with one admin account).
- Spring security - basic and JSON Web Token auth (However the active implem. is now JWT auth)
- REST - providing endpoints for frontend app. 
 * Product categories endpoint  [ GET ]
 * Products endpoint [ GET, DELETE, POST, PUT ]
 * User endpoint. [ GET, POST ]
 * CustomerOrder endpoint [ GET, POST ]
- User registration and login.
- Global validation of passed data.
- Admin - adding, deleting, upgrading products.

## Endpoints
### Security 
| Method | URI | Action | Active |
|--------|-----|--------|--------|
| `POST` | `/user` | `JWT - generate Token` | `ON` |
| `GET` | `/user` | `BASIC - send credentials` | `OFF` |

### User Controller
| Method | URI | Action | Security |
|--------|-----|--------|----------|
| `GET` | `/api/users` | `Get a list of users` | `ADMIN` | 
| `POST` | `/api/users/register` | `Resgister user with  ROLE_USER` | `*` |

### Product Controller
Paging defaults refer to the values in front end. If change, change both sides of the app.
Defaults:
page nr: 1
size: 8
| Method | URI | Action | Security |
|--------|-----|--------|----------|
|  `GET` | `/api/products` | `Get page of products` | `*` |
| `GET` | `/api/products/product/{id}` | `Get product by its id` | `*` |
| `GET` | `/api/products/getByCategoryId` | `Get page of products by id` | `*` | 
| `GET` | `/api/products/name` | `Get page of products by name` | `*` |
| `POST` | `/api/products/` | `Add a new product` | `ADMIN` |
| `PUT` | `/api/products/` | `Update a product` | `ADMIN` |
| `DELETE` | `/api/products/{id}` | `Delete chosen product` | `ADMIN` |

### Product Category Controller
| Method | URI | Action | Security |
|--------|-----|--------|----------|
| `GET` | `/api/product_category` | `Get a list of product categories` | `*` |

### Order Controller
| Method | URI | Action | Security |
|--------|-----|--------|----------|
| `GET` | `/api/order` | `Get a page of orders with customer data` | `ADMIN` |
| `GET` | `/api/order/{id}` | `Get order by id. No customer data.` | `*` |
| `POST` | `/api/order` | `Post order/ send order of purchase` | `*` |

##### More info
###### Security
Although Basic auth was first implemented now the Class is moved outside the component scan and JWT took it's place.

###### Product Category Controller
This is used to dynamically populate the categories in sidebar-menu, and HTML option tag. (Like when adding a product or updating it). 
  
###### Product Controller
