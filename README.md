# Student Complaint Management System — Java Full-Stack Application

A comprehensive, local Java web application built with **Spring Boot 3**, **Spring Security 6**, **Spring Data JPA / Hibernate**, **Thymeleaf**, **Bootstrap 5**, and **MySQL**.

---

## 🛠️ Technology Stack
- **Backend Framework:** Java 17/21 + Spring Boot 3.2.5
- **Security:** Spring Security 6 (BCrypt Password Encoding, Role-Based Authorization, Custom Dual Login Handlers)
- **Persistence / ORM:** Spring Data JPA + Hibernate
- **Database:** MySQL 8.x (`student_complaint_system`)
- **Frontend Template Engine:** Thymeleaf
- **UI Styling & Icons:** Bootstrap 5.3 + Font Awesome 6.4 + Custom Responsive CSS
- **Build Tool:** Maven (Includes Maven Wrapper `mvnw.cmd` / `./mvnw`)

---

## 📋 Features Overview

### 1. Student Module
- **Registration & Authentication:** Account self-registration and secure login.
- **Student Dashboard:** Summary statistics (Total, Pending, In Progress, Resolved, Rejected complaints) and recent complaint timeline.
- **Raise Complaint:** Submit complaints across 10 categories (*Academic, Infrastructure, Hostel, Library, Transport, Examination, Fees, Faculty, IT / Internet, Other*) with priority setting (*LOW, MEDIUM, HIGH*).
- **My Complaints & Details:** Track complaint status with color-coded badges (*Yellow for PENDING, Blue for IN_PROGRESS, Green for RESOLVED, Red for REJECTED*) and view official Admin remarks.
- **Strict Backend Security:** Students can **only** access their own complaints. Access to unauthorized complaint IDs via direct URL editing is strictly denied with a 403 error.

### 2. Admin Module
- **Admin Dashboard:** High-level overview cards tracking all complaint counts and system activity.
- **All Complaints View:** Comprehensive tabular listing of all student complaints across the college.
- **Advanced Filtering & Search:** Filter by Status, Category, Priority, and search by Complaint ID, Student Name, Roll Number, or Complaint Title.
- **Status & Remarks Workflow:** Admins can update complaint status (`PENDING` → `IN_PROGRESS` → `RESOLVED` / `REJECTED`) and provide detailed resolution remarks.

---

## ⚙️ Prerequisites
1. **Java JDK 17 or higher** installed (`java -version`).
2. **MySQL Server 8.x** running locally on port `3306`.

---

## 🚀 Setup & Execution Guide

### Step 1: Configure MySQL Database
Open `src/main/resources/application.properties` and update your MySQL username and password if they differ from default (`root`/`root`):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_complaint_system?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

*(Note: `createDatabaseIfNotExist=true` will automatically create the `student_complaint_system` database if it doesn't exist yet).*

---

### Step 2: Build & Run the Application

Open a terminal or PowerShell in the project directory:

#### Using Maven Wrapper (Windows):
```cmd
mvnw.cmd spring-boot:run
```

#### Using Installed Maven:
```bash
mvn spring-boot:run
```

The application will initialize, create the required database tables, seed the demo accounts, and start embedded Tomcat on **http://localhost:8080**.

---

## 🔑 Official Admin Account

The application initializes the default college administrator account on first run:

### 🛡️ College Administrator Account
- **Email:** `admin@college.edu`
- **Password:** `admin123`

Students register their official college accounts via the **[Student Registration Page](http://localhost:8080/register)** (`/register`).


---

## 📡 REST API Endpoints

The application also exposes secure REST API endpoints:

| HTTP Method | Endpoint | Authorization | Description |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/complaints` | `ROLE_STUDENT` | Submit a new complaint |
| `GET` | `/api/complaints/my` | `ROLE_STUDENT` | List logged-in student's complaints |
| `GET` | `/api/complaints/{id}` | `ROLE_STUDENT` | Get specific complaint details (Ownership checked) |
| `GET` | `/api/admin/complaints` | `ROLE_ADMIN` | Filter & search all complaints |
| `PUT` | `/api/admin/complaints/{id}/status` | `ROLE_ADMIN` | Update complaint status & admin remarks |

---

## 🧪 Testing Checklist

1. Navigate to `http://localhost:8080/login`.
2. Login with `student@test.com` / `student123`.
3. Raise a new complaint under `Infrastructure` category with `HIGH` priority.
4. Check **My Complaints** list to see your new complaint in `PENDING` status.
5. Log out and log in with `admin@test.com` / `admin123`.
6. Open **All Complaints**, locate the new complaint, and click **Review**.
7. Update status to `IN_PROGRESS` or `RESOLVED` and type admin remarks.
8. Log back in as `student@test.com` to see the updated status badge and remarks!
