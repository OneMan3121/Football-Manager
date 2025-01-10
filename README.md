# Team and Player Management API

This project implements a RESTful API for managing **Users**, **Teams**, and **Players** using Jakarta EE, Spring Data JPA, Spring MVC, and other modern Java tools.

---

## Features

- **User Management**:
    - Create, retrieve, update, delete users.
    - Fetch all users.
- **Team Management**:
    - Create teams for users.
    - Retrieve teams for a specific user.
    - Update, delete, and soft-delete teams.
- **Player Management**:
    - Add players to teams.
    - List players in a specific team.
    - Update, delete, and transfer players between teams.
- Includes secured endpoints using JWT for authentication.

---

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring MVC**
- **Jakarta EE**
- **Lombok**
- **Hibernate**
- **Postman** (for API testing)
- **Maven** (build tool)

---

## Quick Start Guide

### Prerequisites
1. Install **Java 17**.
2. Install **Maven**.
3. Set up a database (e.g., `MySQL`, `PostgreSQL`, etc.).
4. Ensure you have **Postman** for API testing (optional).

### How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repository.git
   cd your-repository
   ```

2. Configure application properties:
    - Open `src/main/resources/application.properties` and set up your database configuration:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/team_player_db
      spring.datasource.username=your-username
      spring.datasource.password=your-password
      spring.jpa.hibernate.ddl-auto=update
      ```
    - Customize any additional settings (e.g., JWT secret, server port).

3. Compile and run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application should run on:
   ```
   http://localhost:8080
   ```

---
