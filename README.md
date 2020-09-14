# Shop
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

## RUN
Copy/download, and just run on the compiler. It's set and ready to go.

### Features
```
- Loading data to memory - products, categories and users (with one admin account).
- Spring security - basic and JSON Web Token auth (However the active implem. is now JWT auth)
- REST - providing endpoints for frontend app. 
 * Product categories endpoint  [ GET ]
 * Products endpoint [ GET, DELETE, POST, PUT ]
 * User endpoint. [ GET, POST ]
- User registration.
- Admin - adding, deleting, upgrading products.
```
## Endpoints
### Security 
| Method | URI | Action |
|--------|-----|--------|
| `POST` | `/user` | `JWT - generate Token` |
| `GET` | `/user` | `BASIC - send credentials` | 

### User Controller
| Method | URI | Action |
|--------|-----|--------|
| `GET` | `/api/users` | `Get a list of users` | 
| `GET` | `/api/users/user` | `Get user if exists, else send empty user` |
| `POST` | `/api/users/register` | `Resgister user with  ROLE_USER` |

### Product Controller
Paging defaults refer to the values in front end. If change, change both:
page nr: 1
size: 8
| Method | URI | Action |
|--------|-----|--------|
|  `GET` | `/api/products` | `Get page of products` |
| `GET` | `/api/products/product/{id}` | `Get product by its id` |
| `GET` | `/api/products/getByCategoryId` | `Get page of products by id` | 
| `GET` | `/api/products/name` | `Get page of products by name` |
| `POST` | `/api/products/` | `Add a new product` | 
| `PUT` | `/api/products/` | `Update a product` | 
| `DELETE` | `/api/products/{id}` | `Delete chosen product` |

### Product Category Controller
| Method | URI | Action |
|--------|-----|--------|
| `GET` | `/api/product_category` | `Get a list of product categories` |

##### More info
###### Security
Although Basic auth was first implemented now the Class is moved outside the component scan and JWT took it's place.

###### Product Category Controller
This is used to dynamically populate the categories in sidebar-menu, and HTML <option>. (Like when adding a product or updating it). 
  
###### Product Controller
