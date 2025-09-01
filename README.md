# 📝 Notes Application

A simple **Notes Application** where users can **create, update, and delete** their personal notes.  
This project demonstrates a full-stack implementation with authentication and CRUD operations.

---

## 🔹 Tech Stack

- **Frontend**: HTML, CSS, JavaScript  
- **Backend**: Spring Boot  
- **Security**: Spring Security with sessions
- **Database**: PostgreSQL  
- **Build Tool / Architecture**: Maven  

---

## 📖 Project Explanation

The Notes App provides a secure platform for users to manage their notes.  
Each user can register/login and perform operations like:

- ➕ **Create Notes**  
- ✏️ **Update Notes**  
- 🗑️ **Delete Notes**  
- 👀 **View Notes**  

The application uses **Spring Security** with sessions for authentication, ensuring that only logged-in users can access their notes. Data is persisted in **PostgreSQL**.

---

## ⚙️ How to Clone and Run

1. Clone the repository:
   ```bash
   git clone <your-repo-link>
2. Open the project in your IDE (e.g., IntelliJ, Eclipse, VS Code).

3. Load the Maven dependencies.

4. Configure PostgreSQL:
    Update the application.properties file with your own credentials:
    ```bash
      spring.datasource.url=jdbc:postgresql://localhost:5432/your_database
      spring.datasource.username=your_username  
      spring.datasource.password=your_password
6. Run the Spring Boot application:
   ```bash
    mvn spring-boot:run
## 🐳 Run with Docker

If you prefer running via Docker, pull the pre-built image from Docker Hub:

```bash
docker pull medisettisrinu/java-application
```
Then run:
```bash
docker run -p 8080:8080 medisettisrinu/java-application
```
## 🚀 Features

🔐 Secure authentication with sessions

📝 CRUD operations for notes

🗄️ PostgreSQL integration

🐳 Docker-ready for deployment

## 👨‍💻 Author: Medisetti Srinu
