# Product Management System

This is a Spring Boot application for managing products. It provides a RESTful API to perform CRUD operations on product entities.

## Features

- Create, read, update, and delete products
- Simple and intuitive API endpoints
- Built with Spring Boot and JPA

## Project Structure

```
product-catalog
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── product
│   │   │               ├── ProductManagementSystemApplication.java
│   │   │               ├── controller
│   │   │               │   └── ProductController.java
│   │   │               ├── model
│   │   │               │   └── Product.java
│   │   │               ├── repository
│   │   │               │   └── ProductRepository.java
│   │   │               └── service
│   │   │                   └── ProductService.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── static
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── product
│                       └── ProductManagementSystemApplicationTests.java
├── pom.xml
└── README.md
```

## Setup Instructions

1. Clone the repository:
   ```
   git clone <repository-url>
   ```

2. Navigate to the project directory:
   ```
   cd product-catalog
   ```

3. Build the project using Maven:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

## Usage

Once the application is running, you can access the API at `http://localhost:8080/api/products`. Use tools like Postman or curl to interact with the endpoints.

## API Endpoints

- `POST /api/products` - Create a new product
- `GET /api/products/{id}` - Retrieve a product by ID
- `PUT /api/products/{id}` - Update an existing product
- `DELETE /api/products/{id}` - Delete a product by ID

## License

This project is licensed under the MIT License.