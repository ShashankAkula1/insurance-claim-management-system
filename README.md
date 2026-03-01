# 🏦 Insurance Claim Management System

A production-style Microservices-based backend application for managing insurance claims, built using Spring Boot and Spring Cloud ecosystem.

This project demonstrates scalable service-to-service communication, centralized routing, service discovery, layered architecture, and secure REST API design aligned with BFSI domain standards.

---

## 🚀 Tech Stack

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA (Hibernate)
- MySQL
- Spring Cloud Eureka (Service Discovery)
- Spring Cloud Gateway (API Gateway)
- Maven
- RESTful APIs

---

## 🏗️ System Architecture

The system follows a distributed microservices architecture:

Client  
   ↓  
API Gateway  
   ↓  
-------------------------  
|       |               |  
User Service     Claim Service  
        ↓  
    MySQL Database  

### Microservices Included:

- **Eureka Server**
  - Handles dynamic service registration & discovery

- **API Gateway**
  - Centralized routing
  - Cross-origin configuration
  - Security enforcement

- **User Service**
  - User registration & authentication
  - Role-based access control
  - User data management

- **Claim Service**
  - Insurance claim submission
  - Claim status tracking
  - Business logic processing

---

## 🔐 Key Features

- Microservices-based architecture
- Service discovery using Eureka
- Centralized API routing via Gateway
- Role-based authorization using Spring Security
- DTO-based request/response handling
- Layered architecture (Controller → Service → Repository)
- Database integration using JPA/Hibernate
- Clean separation of concerns

---

## 📂 Project Structure

insurance-claim-management-system  
│  
├── eureka-server  
├── api-gateway  
├── user-service  
└── claim-service  

Each service follows standard layered architecture:

controller/  
service/  
repository/  
entity/  
dto/  
config/  

---

## ⚙️ How to Run the Application

### 1️⃣ Start Services in Order:

1. Start Eureka Server
2. Start User Service
3. Start Claim Service
4. Start API Gateway

### 2️⃣ Access Services

All APIs are accessed through:

http://localhost:<gateway-port>/

---

## 🧠 Architectural Highlights

- Designed using microservices principles
- Decoupled services with independent deployment capability
- Centralized routing and security
- Service-to-service communication via service discovery
- Structured exception handling and layered business logic

---

## 🔮 Future Enhancements

- JWT-based authentication
- Docker containerization
- CI/CD pipeline integration
- Cloud deployment (AWS / Azure)
- Distributed logging & monitoring (ELK)
- Circuit breaker implementation (Resilience4j)

---

## 👨‍💻 Author

Shashank Akula  
Backend Developer | Java | Spring Boot | Microservices  

---

## 📌 Project Purpose

This project was developed to demonstrate backend microservices architecture design, REST API development, secure service communication, and enterprise application structuring in the insurance domain.
