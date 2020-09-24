# AuctioShop
## Backend part
Project by Łukasz Jankowski.

### Prerequisites
- JDK 14
- Maven
- PostgreSQL database
### Build in:
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Spring Security and JWT
- Spring Validation
- IntelliJ IDEA 2020.1 Ultimate Edition

This is the back end of the application. The other part -> [Shop - FrontEnd part!](https://github.com/Lukas-max/shop-frontend).
Data loading is done by Class implementing CommandLineRunner to postgreSQL database. `So if you have postgre you just connect those those and it's ready to go`

## RUN
Copy/download, run on compiler. Only one thing to configure is you database connection. Whole configuration is in Spring application.properties.

### Features
- Loading data to database - products, categories and users (with one admin account).
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
| `POST` | `/api/users/register` | `Resgister user with  ROLE_USER` | `ALL` |

### Product Controller
Paging defaults refer to the values in front end. If change, change both sides of the app.
Defaults:
page: 1
size: 8
| Method | URI | Action | Security |
|--------|-----|--------|----------|
|  `GET` | `/api/products` | `Get page of products` | `ALL` |
| `GET` | `/api/products/product/{id}` | `Get product by its id` | `ALL` |
| `GET` | `/api/products/getByCategoryId` | `Get page of products by id` | `ALL` | 
| `GET` | `/api/products/name` | `Get page of products by name` | `ALL` |
| `POST` | `/api/products/` | `Add a new product` | `ADMIN` |
| `PUT` | `/api/products/` | `Update a product` | `ADMIN` |
| `DELETE` | `/api/products/{id}` | `Delete chosen product` | `ADMIN` |

### Product Category Controller
| Method | URI | Action | Security |
|--------|-----|--------|----------|
| `GET` | `/api/product_category` | `Get a list of product categories` | `ALL` |

### Order Controller
| Method | URI | Action | Security |
|--------|-----|--------|----------|
| `GET` | `/api/order` | `Get a page of orders with customer data` | `ADMIN` |
| `GET` | `/api/order/{id}` | `Get order by id. No customer data.` | `ALL` |
| `POST` | `/api/order` | `Post order/ send order of purchase` | `ALL` |

## More info
More documentation is in the code.

### Product Controller
Mostly it does just what the table above says. Just when posting new product or updating it we validate the ProductRequest and then using the service we map it to Product class 
and persist it. When adding a product without image the application will set a standard 404 not found image. If an image is sent, it's sent in base64 data. Using new FileReader().readAsDataURL. So we split the String at the ','  and decode the second part.
Also in the repository we have our own method to persist an object without persisting it's image. That happens when we are updating a product without sending a new image file.

### Product Category Controller
This is used to dynamically populate the categories in sidebar-menu, and HTML option tag. (Like when adding a product or updating it). 
  
### Order Controller
Has a method to get order by id, get all orders or persist a purchase order. Get all orders is only for administrator, cause it returns page of orders with customer address and credentials. Get order by id is for the client. It has set credentials and address to null, so the client can check his order by id, and no one will have the ability to check credentials of other orders.

When posting the order we are doing validation of the order. It's mostly equal to front end validation, plus cors config should make that no bad data would be transferred.
The service classes map dto objects to Customer and CustomerOrder class. The number of items bought are being decrement if the stock has ran to low and total price and quantity is recounted. Then the database stock is decremented. If the objects drops to 0 in stock it's set to non active.
Also if we ran out of the items that a client wants to purchase, we send him a ResponseException that we ran out of products.

### User Controller
It's mainly for registering new users. The user data validation is on the front end side and here. Before adding a user we check if the user has set id or the username and email are in the database. If so we throw a exception that is visible on the front end side.

### Exception handling
Localy we use ResponseStatusException, and besides that for validation we have our own custom ResponseEntityExceptionHandler, that overrides handleMethodArgumentNotValid with custom exception message. And OrderNotFound exception when asking for order that does not yet exist.

### Security
Although Basic auth was first implemented now the class is moved outside the component scan and JWT took it's place.
JwtAuthenticationController takes the credentials through AuthenticationRequest object and authenticates against Springs Authentication Manager. We have our own custom implementation of UserDetailsService so it validates data against our own database and if correct sends UserDetails. We then again loadTheUserByUsername and by it we create Json web token which we send to the client with some other added data for now.

