# 🚗 Ride Sharing Backend – MongoDB Queries & Analytics Extension

This project is an **extended Ride Sharing backend** implemented using **Spring Boot, MongoDB, JWT-based authentication, and MongoTemplate**.  
It focuses on **advanced ride search, filtering, sorting, pagination, and analytics using MongoDB queries and aggregation pipelines**.

> 🔀 **Branch Note**  
> This extension is implemented on branch: **`ride-sharing-extension-v1`**  
> The `main` branch contains the original submission.

---

## 🛠️ Tech Stack

- Java 17  
- Spring Boot  
- Spring Security + JWT  
- MongoDB  
- MongoTemplate (Queries & Aggregations)  
- Maven  

---

## 📦 Project Structure
```
 com.sathwikperla.demo
│
├── config
│   ├── JwtAuthFilter.java
│   └── SecurityConfig.java
│
├── controller
│   ├── AuthController.java
│   ├── RideController.java
│   ├── RideQueryController.java        // Search, filter, sort APIs
│   └── AnalyticsV1Controller.java      // Analytics APIs
│
├── dto
│   ├── CreateRideRequest.java
│   └── analytics
│       ├── DriverSummaryDto.java
│       ├── RidesPerDayDto.java
│       ├── StatusSummaryDto.java
│       └── UserSpendingDto.java
│
├── exception
│   ├── BadRequestException.java
│   ├── NotFoundException.java
│   └── GlobalExceptionHandler.java
│
├── model
│   ├── User.java
│   └── Ride.java
│
├── repository
│   ├── UserRepository.java
│   └── RideRepository.java
│
├── service
│   ├── AuthService.java
│   ├── RideService.java
│   ├── RideQueryService.java           // MongoTemplate query logic
│   └── AnalyticsService.java           // Mongo aggregation logic
│
├── util
│   ├── JwtUtil.java
│   └── SecurityUtil.java
│
└── DemoApplication.java
```

### 🧩 Data Model (Ride)
```
Ride {
  id: String
  userId: String
  driverId: String
  pickupLocation: String
  dropLocation: String
  status: REQUESTED | ACCEPTED | COMPLETED
  createdDate: LocalDate
  createdAt: Date
  distanceKm: Double
  fare: Double
}
```


---

## 🔐 Security

- JWT-based authentication
- Stateless session management
- `/api/auth/**` → public
- All other endpoints → JWT required
- Roles:
  - `ROLE_USER` (Passenger)
  - `ROLE_DRIVER` (Driver)

---

## 🔍 Ride Query APIs (`/api/v1/rides`)

| # | Endpoint | Description |
|---|---------|-------------|
| 1 | `/search?text=` | Search by pickup OR drop (case-insensitive regex) |
| 2 | `/filter-distance?min=&max=` | Filter by distance range |
| 3 | `/filter-date-range?start=&end=` | Rides between date range |
| 4 | `/sort?order=asc\|desc` | Sort rides by fare |
| 5 | `/user/{userId}` | All rides for a user |
| 6 | `/user/{userId}/status/{status}` | User rides filtered by status |
| 7 | `/filter-status?status=&search=` | Status + keyword search |
| 8 | `/advanced-search` | Keyword + status + sort + pagination |
| 9 | `/date/{date}` | Rides on a specific date |



## 🚕 Driver APIs

| Endpoint | Description |
|--------|------------|
| `/api/v1/driver/{driverId}/active-rides` | Active (ACCEPTED) rides for driver |



## 📊 Analytics APIs (`/api/v1/analytics`)

| # | Endpoint | Description |
|---|---------|-------------|
| 10 | `/rides-per-day` | Rides count grouped by date |
| 11 | `/driver/{driverId}/summary` | Driver ride statistics |
| 12 | `/user/{userId}/spending` | User total spending (COMPLETED rides) |
| 13 | `/status-summary` | Count of rides grouped by status |

All analytics are implemented using **MongoDB aggregation pipelines**.

---

## ▶️ Running the Application

### Prerequisites

- MongoDB running on `localhost:27017`
- Java 17+
- Maven

---

### Run
```
mvn spring-boot:run
Server runs at: http://localhost:8081
```

## 🧪 Sample Test Flow
```
POST /api/auth/register
POST /api/auth/login
POST /api/rides
GET /api/v1/rides/search?text=Kor
GET /api/v1/analytics/rides-per-day
```


---

## ✅ Key Concepts Demonstrated

- MongoTemplate advanced queries
- Regex search with AND / OR conditions
- Range and date filtering
- Sorting and pagination
- MongoDB aggregation pipelines
- Clean controller–service separation
- JWT-secured REST APIs

---

## 📌 Branch Information

- `main` → Original base Ride Sharing backend
- `ride-sharing-extension-v1` → MongoTemplate queries & analytics extension

---



