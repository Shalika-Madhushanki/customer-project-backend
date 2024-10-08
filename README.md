# Backend Project - Customer and Project Management API

## Overview

This Spring Boot application provides a RESTful API for managing **Customers** and **Projects**. It supports basic CRUD operations for both entities and includes endpoints to manage relationships between customers and projects.

## Prerequisites

- **Docker** and **Docker Compose** to build and run the application in containers.
- **Java 17** or higher (for development).

## Building and Running the Application

### Running the Project

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd <repository-folder>
   ```
2. Build the Spring Boot Project:
    If you’re using **Gradle**, run:
      ```bash
      ./gradlew build
      ```
   This will create a `.jar` file in the `build/libs` directory (for Gradle) or `target` directory (for Maven).

3. Run the application using Docker Compose:
   ```bash
   docker-compose up --build
   ```

   This command will:
    - Build and start the Spring Boot application.
    - Start the PostgreSQL database with the initial data from `init.sql`.
    - Launch pgAdmin for database management.

4. Access the services:
    - **Backend API**: [http://localhost:8080](http://localhost:8080)
    - **pgAdmin**: [http://localhost:5050](http://localhost:5050)
        - Login with the following credentials:
            - Email: `admin@admin.com`
            - Password: `admin`

### Database Connection Details

The following environment variables configure the application’s connection to the database:

```yaml
SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/testdb
SPRING_DATASOURCE_USERNAME: testuser
SPRING_DATASOURCE_PASSWORD: testpass
```

- **Database Host**: `db` (this matches the service name in Docker Compose)
- **Database Port**: `5432`
- **Database Name**: `testdb`
- **Username**: `testuser`
- **Password**: `testpass`

These values are set within the `docker-compose.yml` file, under the `app` service's `environment` section. When the Spring Boot application starts, it will use these values to connect to the PostgreSQL database.

### Available API Endpoints

The following endpoints are available for managing **Customers** and **Projects**:

#### Customer Endpoints
- **GET** `/api/v1/customers` - Retrieve all customers
- **GET** `/api/v1/customers/{id}` - Retrieve a specific customer by ID
- **POST** `/api/v1/customers` - Create a new customer
- **PUT** `/api/v1/customers/{id}` - Update an existing customer
- **DELETE** `/api/v1/customers/{id}` - Delete a customer and their projects

#### Project Endpoints
- **GET** `/api/v1/projects` - Retrieve all projects
- **GET** `/api/v1/projects/{id}` - Retrieve a specific project by ID
- **POST** `/api/v1/projects` - Create a new project and assign it to a customer
- **PUT** `/api/v1/projects/{id}` - Update an existing project
- **DELETE** `/api/v1/projects/{id}` - Delete a specific project

#### Download Projects by Date Range
- **POST** `/api/v1/projects/download` - Download all projects created within a specified date range as a ZIP file (pass `startDate` and `endDate` in the request body)

### Troubleshooting and Additional Information

- **pgAdmin Configuration**:
    - Use the following to add a new server within pgAdmin to connect to the database:
        - **Host**: `db`
        - **Port**: `5432`
        - **Username**: `testuser`
        - **Password**: `testpass`
    
