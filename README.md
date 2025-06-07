# MotoGP Backend API

## Project Overview

This project is a robust backend API for a MotoGP information platform, built with Spring Boot. It serves as the data backbone for managing and delivering information about MotoGP seasons, events, riders, teams, manufacturers, race sessions, results, and news articles. The API includes features like user authentication and authorization using JWT, secure file uploads for images, and comprehensive data management capabilities.

## Core Features

*   **User Authentication & Authorization:** Secure registration, login, and password management (forgot/reset password with OTP). Role-based access control (RBAC) using Spring Security and JWT.
*   **Data Management:** CRUD operations for all core MotoGP entities:
    *   Users
    *   Riders
    *   Teams
    *   Manufacturers
    *   Circuits
    *   Seasons
    *   Events (Races, Tests, etc.)
    *   Sessions (Practice, Qualifying, Race)
    *   Results (including points calculation)
    *   Categories (MotoGP, Moto2, Moto3)
    *   Contracts
    *   News Articles
*   **File Storage:** Secure handling of file uploads (e.g., rider images, news article images) with unique naming and configurable storage paths.
*   **Auditing:** Automatic tracking of entity creation and modification (created by, created date, last modified by, last modified date).
*   **Relational Data Integrity:** Proper handling of relationships between entities, including cascading updates/deletions where appropriate (e.g., when deleting a manufacturer, related teams are updated).
*   **DTO Pattern:** Utilizes Data Transfer Objects (DTOs) for API request/response, decoupling API contracts from entity models, configured with ModelMapper.
*   **CORS Configuration:** Allows cross-origin requests from specified frontend applications.

## Technologies Used

*   **Framework:** Spring Boot 3.x
*   **Language:** Java 17
*   **Security:** Spring Security (JWT for token-based authentication)
*   **Database:** Spring Data JPA with Hibernate (MySQL as the target database)
*   **Build Tool:** Apache Maven
*   **API Documentation (Implicit):** Standard Spring REST Controllers (consider adding Springdoc OpenAPI for Swagger UI)
*   **Mapping:** ModelMapper for DTO-entity conversion
*   **File Handling:** Spring MVC `MultipartFile`

## Project Structure
Workspace
Collecting workspace information

## 🏗️ Project Structure

```
motogp_b/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/motogp_b/
│   │   │       ├── config/
│   │   │       ├── controller/
│   │   │       ├── dto/
│   │   │       ├── entity/
│   │   │       ├── exception/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       │   └── impl/
│   │   │       └── MotogpBApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── static/
│   └── test/
│       └── java/
│           └── com/example/motogp_b/
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## Setup and Installation

### Prerequisites

*   Java JDK 17 or higher
*   Apache Maven 3.6+
*   MySQL Server 8.0+

### Database Setup

1.  Create a MySQL database (e.g., `moto_gp`).
2.  Update the database connection details in `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/moto_gp?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
    spring.datasource.username=your_mysql_user
    spring.datasource.password=your_mysql_password

    # JPA Properties
    spring.jpa.hibernate.ddl-auto=update # Or 'validate' for production after initial setup
    spring.jpa.show-sql=true
    spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
    ```

### Running the Application

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd motogp_b
    ```
2.  **Build the project using Maven Wrapper:**
    ```bash
    ./mvnw clean install
    ```
    (For Windows, use `mvnw.cmd clean install`)
3.  **Run the application:**
    ```bash
    ./mvnw spring-boot:run
    ```
    The application will start on the configured port (default: 9096, as seen in previous discussions).

## Configuration

Key configurations are managed in `src/main/resources/application.properties`:

*   **Server Port:**
    ```properties
    server.port=9096
    ```
*   **JWT Secret and Expiration:**
    ```properties
    jwt.secret=your-very-strong-jwt-secret-key-that-is-at-least-256-bits
    jwt.expiration=86400000 # 24 hours in milliseconds
    jwt.refresh.expiration=604800000 # 7 days in milliseconds
    ```
*   **File Upload Directory:**
    The default upload directory is `static/uploads`. This can be configured using an environment variable or application property, as handled in [`FileStorageServiceImpl.java`](motogp_b/src/main/java/com/example/motogp_b/service/impl/FileStorageServiceImpl.java).
    ```properties
    # Example for overriding in application.properties
    upload.dir=./custom_uploads_directory
    ```
    Or via environment variable `UPLOAD_DIR`.
*   **CORS Allowed Origins:**
    Configured in [`SecurityConfig.java`](motogp_b/src/main/java/com/example/motogp_b/config/SecurityConfig.java).

## API Endpoints

The API provides a comprehensive set of RESTful endpoints. Here's a general overview of the main resources:

*   **Authentication:**
    *   `POST /auth/token`: Login and receive JWT.
    *   `POST /auth/introspect`: Validate JWT.
    *   `POST /auth/refresh`: Refresh JWT.
*   **Users:**
    *   `POST /users/register`: Register a new user.
    *   `POST /users/forgot-password`: Request password reset OTP.
    *   `POST /users/reset-password`: Reset password using OTP.
    *   `GET /users/{id}`: Get user details.
    *   `PUT /users/change-password/{id}`: Change user password.
    *   `GET /users`: Get all users (Admin only).
*   **Data Entities (Riders, Teams, Events, etc.):**
    *   `GET /api/v1/{entity}`: List all items of an entity.
    *   `GET /api/v1/{entity}/{id}`: Get a specific item by ID.
    *   `POST /api/v1/{entity}`: Create a new item (Admin/Authenticated roles, depending on entity).
    *   `PUT /api/v1/{entity}/{id}`: Update an existing item (Admin/Authenticated roles).
    *   `DELETE /api/v1/{entity}/{id}`: Delete an item (Admin/Authenticated roles).

*(For a detailed API specification, consider generating Swagger/OpenAPI documentation).*

## Security Implementation

*   **JWT Authentication:** Stateless authentication using JSON Web Tokens.
*   **Spring Security:** Handles authentication and authorization.
*   **Password Encoding:** Passwords are securely hashed using `PasswordEncoder` (e.g., BCrypt).
*   **Role-Based Access Control (RBAC):** Endpoints are protected based on user roles (e.g., `ROLE_USER`, `ROLE_ADMIN`). Method-level security can also be applied using `@PreAuthorize`.
*   **CORS:** Configured in [`SecurityConfig.java`](src/main/java/com/example/motogp_b/config/SecurityConfig.java) to allow requests from specific frontend origins.
*   **Auditing:** [`appCofig.java`](src/main/java/com/example/motogp_b/config/appCofig.java) and `@EnableJpaAuditing` in [`MotogpBApplication.java`](src/main/java/com/example/motogp_b/MotogpBApplication.java) ensure user and timestamp tracking for entity changes.

## File Storage

*   File uploads are handled by [`FileStorageServiceImpl.java`](src/main/java/com/example/motogp_b/service/impl/FileStorageServiceImpl.java).
*   Uploaded files are stored with a unique `UUID` based filename to prevent collisions.
*   The base upload directory is `static/uploads` but can be configured via the `upload.dir` property or `UPLOAD_DIR` environment variable.
*   The service returns a web-accessible path for the stored file.
*   A `WebMvcConfigurer` (as discussed previously) should be implemented to serve files from this custom directory if it's outside the default static path.

## Docker Deployment (Example)

1.  **Create a `Dockerfile`:**
    ```dockerfile
    FROM openjdk:17-jdk-slim
    WORKDIR /app
    ARG JAR_FILE=target/motogp_b-0.0.1-SNAPSHOT.jar
    COPY ${JAR_FILE} app.jar
    ENV UPLOAD_DIR=/app/uploads
    RUN mkdir -p /app/uploads
    EXPOSE 9096
    ENTRYPOINT ["java", "-Dupload.dir=${UPLOAD_DIR}", "-jar", "app.jar"]
    ```
2.  **Build the Docker image:**
    ```bash
    docker build -t motogp-backend .
    ```
3.  **Run the Docker container:**
    ```bash
    docker run -p 9096:9096 \
      -e SPRING_DATASOURCE_URL=jdbc:mysql://your_db_host:3306/moto_gp \
      -e SPRING_DATASOURCE_USERNAME=your_db_user \
      -e SPRING_DATASOURCE_PASSWORD=your_db_password \
      -e JWT_SECRET=your_jwt_secret \
      -e UPLOAD_DIR=/app/data/uploads \
      -v /path/on/host/uploads:/app/data/uploads \
      motogp-backend
    ```
    *Note: Adjust volume mounts (`-v`) and environment variables (`-e`) as needed for your deployment environment.*

## Future Enhancements / Considerations

*   **API Versioning:** Implement a strategy for API versioning (e.g., URL path, custom header).
*   **Comprehensive Testing:** Add more extensive unit and integration tests.
*   **API Documentation:** Integrate Springdoc OpenAPI for interactive API documentation (Swagger UI).
*   **Caching:** Implement caching strategies for frequently accessed, rarely changing data.
*   **Logging & Monitoring:** Enhance logging (e.g., with Logback configurations) and integrate monitoring tools.
*   **Global Exception Handling:** Refine global exception handling for consistent error responses.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change. Please make sure to update tests as appropriate.

---
