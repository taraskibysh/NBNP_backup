# Trip To Go - Backend Service

A backend service for the **Trip To Go** application, designed to help friends plan and manage trips efficiently. The platform allows users to create and join trips, manage event schedules, split tasks, and track shared expenses, ensuring a fair and organized travel experience.

## Table of Contents
- [Introduction](#introduction)
- [Where to find front-end part of the project](#where-to-find-front-end-part-of-the-project)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Local Database Setup](#local-database-setup)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Contributing](#contributing)

## Introduction
The **Trip To Go - Backend Service** is a RESTful API that powers the **Trip To Go** application. It enables users to efficiently plan trips with friends by providing essential functionalities such as:
- Creating and managing trips
- Joining existing trips
- Viewing and modifying event schedules
- Assigning and tracking tasks among trip participants
- Managing shared expenses to avoid financial conflicts

## Where to find front-end part of the project
- **Frontend Repository**: The frontend part of this application is available at [Trip To Go - Frontend](https://github.com/anastasiakuznietsova/frontend_project_nbnp).

## Technologies
This project is built using the following technologies:
- **Java 17** - Core programming language
- **Spring Boot** - Backend framework
- **Maven** - Dependency management and build automation
- **Flyway** - Database version control and migrations
- **Docker** - Containerization of the database
- **MySQL** - Relational database
- **MapStruct** - Fast entity-to-DTO mapping
- **Spring Data JPA** - Simplifies database access

## Prerequisites
Ensure you have the following installed on your system:
- [Java 17 JDK](https://adoptium.net/) - Required to run the application
- [Maven](https://maven.apache.org/install.html) - For dependency management
- [Docker](https://docs.docker.com/engine/install/) - To run the database container

## Installation
Clone the repository and build the project:
```sh
    git clone https://github.com/your-repository.git
    cd your-project-directory
    mvn clean install
```

## Configuration
1. **Set the `SPRING_PROFILES_ACTIVE` environment variable**  
   The application uses profiles for different environments (e.g., `dev`, `prod`).

   **Once Per Terminal Session: Set it once in your terminal, and it applies to all `mvn spring-boot:run` commands in that session)**
   To set the profile, run:
   - **Linux/macOS (temporary for the session)**:
     ```sh
     export SPRING_PROFILES_ACTIVE=dev
     ```
   - **Windows (PowerShell - temporary for the session)**:
     ```powershell
     $env:SPRING_PROFILES_ACTIVE="dev"
     ```
   - **Windows (Command Prompt - temporary for the session)**:
     ```sh
     set SPRING_PROFILES_ACTIVE=dev
     ```
     Run subsequent `mvn spring-boot:run` commands without resetting — it persists until the terminal closes.

   - **Persistent setting (Linux/macOS - add to `~/.bashrc` or `~/.zshrc`)**:
     ```sh
     echo 'export SPRING_PROFILES_ACTIVE=dev' >> ~/.bashrc
     source ~/.bashrc
     ```
     *(Use `~/.zshrc` if using Zsh.)*

   - Use Cases:
     `dev`: Local development with database updates, Swagger enabled and local database.
     `prod`: Production-like settings with validation, Swagger disabled and cloud database.

   **Troubleshooting:**
   - To check if the variable is set correctly:
     ```sh
     echo $SPRING_PROFILES_ACTIVE   # macOS/Linux
     echo %SPRING_PROFILES_ACTIVE%  # Windows (cmd)
     echo $env:SPRING_PROFILES_ACTIVE  # Windows (PowerShell)
     ```
   - To remove it:
     ```sh
     unset SPRING_PROFILES_ACTIVE  # macOS/Linux
     set SPRING_PROFILES_ACTIVE=   # Windows (cmd)
     Remove-Item Env:\SPRING_PROFILES_ACTIVE  # Windows (PowerShell)
     ```
   - **Profile Not Applied**: Check logs for `"The following profiles are active"`. 
   - If it says default, the variable isn’t set. Verify with 
      ```sh
      echo $SPRING_PROFILES_ACTIVE (should output dev)
      ```
   - **Variable Lost**: If you open a new terminal or restart your system, re-run
     ```sh
     export SPRING_PROFILES_ACTIVE=dev
     ```
     
2. **Create a `.env` file** in the root directory (if not already created) by copying `.env.example`:
    ```sh
    cp .env.example .env
    ```
3. **Edit the `.env` file** and set your database credentials:
    ```plaintext
    MYSQL_ROOT_PASSWORD=yourpassword
    MYSQL_DATABASE=yourdatabase
    MYSQL_USER=yourusername
    MYSQL_PASSWORD=yourpassword
    ```
4. **Add JWT Secret Key**: Include a JWT_SECRET_KEY in your `.env` file for authentication.
Generate a secure key (minimum 32 bytes) using a tool like [jwtsecret.com/generate](https://jwtsecret.com/generate):
   ```plaintext
   JWT_SECRET_KEY=your-32-byte-secret-key-here
   ```
   - Example: If generated from **jwtsecret.com**, it might look like
      `e995c6d773054f3c5d9a161f8c0b176d6ecd0658dd09baf72feb0108677a27d2`
   - Ensure it’s at least 32 bytes (256 bits) for security with JWT signing

5. **Add S3 bucket keys**: Include a S3 keys in your `.env`.
   ```plaintext
   YOUR_ACCESS_KEY = AccessKeyForAws
   YOUR_SECRET_KEY = SecretKeyForAws
   ```
  
## Local Database Setup

### Starting the Database
1. Run the following command to start the database container:
    ```sh
    docker-compose up -d
    ```
2. If something goes wrong, manually run the Flyway migrations:
    ```sh
    docker-compose up flyway-migrations
    ```

### Running New Migrations with Flyway
1. Create migration scripts in `./flyway/sql` following the pattern `V[version]__[description].sql`.
2. Apply migrations:
    ```sh
    docker-compose up flyway-migrations
    ```

### Connecting to the Database
- **Host**: `localhost`
- **Port**: `3306`
- **Username**: `${MYSQL_USER}`
- **Password**: `${MYSQL_PASSWORD}`
- **Database**: `${MYSQL_DATABASE}`

### Stopping & Removing the Database Container
```sh
docker-compose down
```

## Running the Application

1. **Ensure the database is running**:
    ```sh
    docker-compose up -d
    ```

2. **Run the application** using Maven:
    ```sh
    mvn spring-boot:run
    ```

3. **(Optional) Run with a specific profile** (ensuring the profile is set):
    ```sh
    export SPRING_PROFILES_ACTIVE=dev && mvn spring-boot:run  # macOS/Linux
    set SPRING_PROFILES_ACTIVE=dev && mvn spring-boot:run  # Windows (cmd)
    ```
   - Must be repeated for each run if profile was not set earlier
   
4. **Check if the app is running** by accessing:
    ```
    http://localhost:8080
    ```

## Testing
Run unit tests:
```sh
mvn test
```

## Contributing
1. Fork the repository
2. Create a new branch:
    ```sh
    git checkout -b feature/your-feature
    ```
3. Commit your changes:
    ```sh
    git commit -m 'Add your feature'
    ```
4. Push to the branch:
    ```sh
    git push origin feature/your-feature
    ```
5. Create a Pull Request

