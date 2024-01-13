# Book Store Web Application

## Introduction

Welcome to the Book Store Web Application! This project was inspired by the need for a robust and secure platform to manage various aspects of a book store, including user authentication, book and category management, order processing, and shopping cart functionality.

## Technologies Used

- **Spring Boot**: Used for building the backend application.
- **Spring Security**: Ensures secure authentication and authorization.
- **Spring Data JPA**: Simplifies database interactions with the application.
- **Swagger**: Provides an interactive API documentation for easy testing.
- **H2 Database**: Used for quick development and testing purposes.
- **Lombok**: Reduces boilerplate code with annotation-based simplifications.

## Security Configuration

The application employs Spring Security to manage authentication and authorization. The security configuration can be found in the `SecurityConfig` class. It includes settings for JWT authentication, role-based access control, and secure endpoints for different user roles.

## Controllers and Functionalities

### AuthenticationController

- **POST /api/auth/registration**: Create a new user.
- **POST /api/auth/login**: Authenticate and retrieve a JWT token.

### BookController

- **GET /api/books**: Retrieve all books with pagination and sorting.
- **GET /api/books/{id}**: Retrieve a book by ID.
- **POST /api/books**: Create a new book (requires admin role).
- **PUT /api/books/{id}**: Update a book by ID (requires admin role).
- **DELETE /api/books/{id}**: Delete a book by ID (requires admin role).
- **GET /api/books/search**: Search books based on criteria with pagination and sorting.

### CategoryController

- **POST /api/categories**: Create a new category (requires admin role).
- **GET /api/categories**: Retrieve all categories with pagination and sorting.
- **GET /api/categories/{id}**: Retrieve a category by ID.
- **PUT /api/categories/{id}**: Update a category (requires admin role).
- **DELETE /api/categories/{id}**: Delete a category (requires admin role).
- **GET /api/categories/{id}/books**: Retrieve books by category ID with pagination and sorting.

### OrderController

- **POST /api/orders**: Place a new order.
- **GET /api/orders**: Retrieve user's order history.
- **PATCH /api/orders/{id}**: Update order status.

### OrderItemController

- **GET /api/orders/{orderId}/items**: Retrieve all order items for a specific order.
- **GET /api/orders/{orderId}/items/{itemId}**: Retrieve a specific order item within an order.

### ShoppingCartController

- **GET /api/cart**: Retrieve user's shopping cart by ID.
- **POST /api/cart**: Add a book to the shopping cart.
- **PUT /api/cart/cart-items/{cartItemId}**: Update quantity of a book in the shopping cart.
- **DELETE /api/cart/cart-items/{cartItemId}**: Remove a book from the shopping cart.

## Setup and Usage

1. Clone the repository.
2. Configure your database settings in `application.properties`.
3. Run the application using `./mvnw spring-boot:run` (or `./mvnw.cmd` on Windows).
4. Access the Swagger documentation at `http://localhost:8080/swagger-ui/index.html`.

## Challenges and Solutions

- **JWT Authentication**: Implementing secure JWT authentication was challenging, but Spring Security made it manageable.
- **Role-Based Access Control**: Ensuring proper authorization for different endpoints required careful configuration in the security settings.
- **DTOs and Validation**: Managing data transfer objects (DTOs) and validation annotations improved the request and response structures.

## Postman Requests

For a collection of Postman requests and examples, refer to the `postman` directory in the project repository.

Feel free to explore the API, and happy coding! 🚀
