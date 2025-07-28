# Social Network Prototype 

This project is a RESTful API for a social networking platform, built using the Spring Boot framework. The application allows users to register, log in, create and manage posts, add comments, and like various posts.

## Features

* **User Management**: Registration, authentication, and logout functionalities.
* **Post Management**: Create, read, update, and delete posts.
* **Token-based Sessions**: Uses a token (`X-Session-Token`) for securing API endpoints after login.
* **Comment Management**: Add, update, and delete comments on posts.
* **Like Management**: Like and unlike posts.
* **External Service Integration**: Imports users from the [reqres.in](https://reqres.in/) service upon application startup.
* **API Documentation**: Integrated with Swagger/OpenAPI for easy viewing and testing of API endpoints.

## Technologies

* **Java 17**
* **Spring Boot 3.5.3**
* **Spring Web**: For building RESTful web services.
* **Spring Data JPA**: For database interaction (PostgreSQL).
* **Spring Security**: For handling authentication and authorization.
* **Spring Cloud OpenFeign**: For a declarative REST client to connect with `reqres.in`.
* **PostgreSQL**: As the relational database.
* **Lombok**: To reduce boilerplate code.
* **Springdoc OpenAPI (Swagger)**: For automatic API documentation generation.
* **Maven**: For project build and dependency management.

## API Endpoints

The complete API documentation is available at `/swagger-ui.html` after the application is started.

### Users (`/users`)

* `POST /users/register`: Registers a new user.
* `POST /users/login`: Authenticates a user and returns a session token.
* `POST /users/logout`: Invalidates the user's session.
* `GET /users`: Retrieves a paginated list of registered users.

### Posts (`/posts`)

* `POST /posts`: Creates a new post.
* `PUT /posts/{id}`: Updates an existing post.
* `DELETE /posts/{id}`: Deletes a post.
* `GET /posts/{id}`: Retrieves a specific post.
* `GET /posts`: Retrieves a list of all posts.
* `GET /posts/by-user/{username}`: Retrieves posts by a specific user.
* `POST /posts/{id}/like`: Likes a post.
* `DELETE /posts/{id}/like`: Unlikes a post.

### Comments (`/comments`)

* `POST /comments`: Adds a new comment to a post.
* `PUT /comments/{id}`: Updates a comment.
* `DELETE /comments/{id}`: Deletes a comment.
* `GET /comments/by-post/{postId}`: Retrieves comments for a specific post.
* `GET /comments/by-user/{username}`: Retrieves comments by a specific user.

## Setup

### Prerequisites

* Java 17 or newer.
* Maven.
* A running instance of PostgreSQL.

### Configuration

1.  Clone the repository.
2.  Update the database configuration in `src/main/resources/application.yaml`:
    ```yaml
    spring:
      datasource:
        url: jdbc:postgresql://localhost:5432/your_database
        driver-class-name: org.postgresql.Driver
        username: your_username
        password: your_password
    ```
3.  The JPA `ddl-auto` property is currently set to `create`, which means the database schema will be recreated on every startup. For a production environment, it is recommended to change this to `validate` or `update`.
