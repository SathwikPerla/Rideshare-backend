# 🚗 RideShare Backend – Spring Boot & MongoDB

A mini Ride Sharing backend application built using Spring Boot, MongoDB, and JWT Authentication. This project demonstrates a clean backend architecture with secure authentication, role-based authorization, validation, and global exception handling, designed as an academic mini project.

## 📌 Project Overview
The system supports two user roles: 👤 USER (Passenger) who can request rides and view their own rides, and 🚘 DRIVER who can view ride requests, accept rides, and complete rides. The application follows a clean layered architecture: Controller → Service → Repository, ensuring separation of concerns and maintainability.

## 🛠 Tech Stack
Java 17+, Spring Boot, Spring Security, MongoDB, JWT (JSON Web Token), Maven, Jakarta Validation.

## 📂 Project Structure
src  
└── main  
  └── java  
    └── com  
      └── sathwikperla  
        └── demo  
          ├── config  
          │   ├── JwtAuthFilter.java  
          │   └── SecurityConfig.java  
          ├── controller  
          │   ├── AuthController.java  
          │   └── RideController.java  
          ├── dto  
          │   ├── AuthResponse.java  
          │   ├── CreateRideRequest.java  
          │   ├── LoginRequest.java  
          │   └── RegisterRequest.java  
          ├── exception  
          │   ├── BadRequestException.java  
          │   ├── NotFoundException.java  
          │   └── GlobalExceptionHandler.java  
          ├── model  
          │   ├── Ride.java  
          │   └── User.java  
          ├── repository  
          │   ├── RideRepository.java  
          │   └── UserRepository.java  
          ├── service  
          │   ├── AuthService.java  
          │   ├── CustomUserDetailsService.java  
          │   └── RideService.java  
          ├── util  
          │   ├── JwtUtil.java  
          │   └── SecurityUtil.java  
          └── DemoApplication.java  

## 👥 User Roles
ROLE_USER can request rides and view their own rides. ROLE_DRIVER can view pending ride requests, accept rides, and complete rides.

## 🔐 Authentication Flow
Users register and login using credentials. On successful login, a JWT token is generated and must be sent in all secured API requests using the header: Authorization: Bearer <JWT_TOKEN>.

## 🔄 Ride Lifecycle
REQUESTED → ACCEPTED → COMPLETED. A ride is created with REQUESTED status, a driver accepts it changing the status to ACCEPTED, and upon completion the status becomes COMPLETED.

## 🌐 API Endpoints
Public: POST /api/auth/register to register users or drivers, POST /api/auth/login to login and receive JWT.  
User: POST /api/v1/rides to request a ride, GET /api/v1/user/rides to view own rides.  
Driver: GET /api/v1/driver/rides/requests to view pending requests, POST /api/v1/driver/rides/{id}/accept to accept a ride.  
User/Driver: POST /api/v1/rides/{id}/complete to complete a ride.

## 🧾 Sample Request Bodies
Register: {"username":"john","password":"1234","role":"ROLE_USER"}  
Login: {"username":"john","password":"1234"}  
Create Ride: {"pickupLocation":"Koramangala","dropLocation":"Indiranagar"}

## ⚠️ Validation & Error Handling
The project uses Jakarta Bean Validation for input validation and centralized exception handling using @ControllerAdvice. Errors are returned in a consistent JSON format.

Example Error Response:  
{"error":"BAD_REQUEST","message":"Username already exists","timestamp":"2025-12-06T16:53:00Z"}

## ⚙️ Configuration
server.port=8081  
spring.data.mongodb.uri=mongodb://localhost:27017/rideshare  
app.jwt.secret=supersecretkey_supersecretkey_123  
app.jwt.expiration-ms=86400000  

## ▶️ How to Run
Ensure MongoDB is running on localhost:27017. Clone the repository and run mvn spring-boot:run. The server starts on http://localhost:8081.

## ✅ Features Implemented
JWT-based authentication 🔑, role-based authorization 👥, ride request/accept/complete flow 🚗, MongoDB integration 🗄, DTO validation ✅, global exception handling ⚠️, and clean layered architecture 🧱.

## 👨‍💻 Author
Sathwik Perla – Spring Boot RideShare Backend Project
