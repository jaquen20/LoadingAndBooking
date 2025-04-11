# Load & Booking Management System

A robust backend system for managing load and booking operations built with **Spring Boot** and **MySQL**.

---

## Features

### **Load Management**
- Create, read, update, and delete loads
- Filter loads by shipper, truck type, and status
- Automatic status transitions (POSTED → BOOKED → CANCELLED)

### **Booking Management**
- Transporter booking requests
- Booking status flow: PENDING → ACCEPTED / REJECTED
- Rate negotiation system

### **Advanced Features**
- Comprehensive validation for all fields
- Custom error handling

---

## Tech Stack

- **Backend**: Spring Boot 3.4.4
- **Database**: MySQL 15
- **Build Tool**: Maven
- **Java Version**: 17

---

## Getting Started

### **Prerequisites**
- Java 17
- MySQL 15
- Maven 3.8+

### **Installation**

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/load-booking-system.git
   cd load-booking-system
   ```

2. **Set Up MySQL Database**
   ```bash
   mysql -u root -p
   ```

   Inside the MySQL shell:
   ```sql
   CREATE DATABASE bookings_db;
   CREATE USER 'lb_user'@'localhost' IDENTIFIED BY 'password123';
   GRANT ALL PRIVILEGES ON bookings_db.* TO 'lb_user'@'localhost';
   FLUSH PRIVILEGES;
   ```

3. **Configure MySQL in `application.properties`**
   Location: `src/main/resources/application.properties`
   ```properties
   spring.application.name=Bookings
   spring.datasource.url=jdbc:mysql://localhost:3306/bookings_db
   spring.datasource.username=lb_user
   spring.datasource.password=password123
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

---

## API Documentation

### Base URL
```
http://localhost:8080/api
```

---

## Endpoints for Loads

### 1. **Create Load**

**POST** `/loads`

**Request Body**
```json
{
  "shipperId": "ship123",
  "facility": {
    "loadingPoint": "darjeeling",
    "unloadingPoint": "raipur",
    "loadingDate": "2025-01-03",
    "unloadingDate": "2025-01-07"
  },
  "productType": "foods",
  "truckType": "container",
  "noOfTrucks": 3,
  "weight": 1500.00,
  "comment": "tea leaves"
}
```

**Responses**
- `201 Created` – Load created successfully
- `400 Bad Request` – Invalid data

---

### 2. **Get Loads**

**GET** `/loads`

**Query Params (Optional)**
- `shipperId`, `truckType`, `status`

**Example Request**
```
/loads?shipperId=ship123&truckType=container
```

**Success Response**
```json
{
  "id": "8b414e60-1876-423d-b93b-03d1cda9e349",
  "shipperId": "ship123",
  "facility": {
    "loadingPoint": "darjeeling",
    "unloadingPoint": "raipur",
    "loadingDate": "2025-01-03",
    "unloadingDate": "2025-01-07"
  },
  "productType": "foods",
  "truckType": "container",
  "noOfTrucks": 3,
  "weight": 1500.0,
  "comment": "tea leaves",
  "datePosted": "2025-04-10T17:53:44.072+00:00",
  "status": "POSTED"
}
```

**Error Response**
```json
{
  "error": "Unexpected error occurred"
}
```

---

### 3. **Update Load**

**PUT** `/loads/{loadId}`

**Request Body**
```json
{
  "id": "8b414e60-1876-423d-b93b-03d1cda9e349",
  "shipperId": "shipper9090",
  "facility": {
    "loadingPoint": "nagpur",
    "unloadingPoint": "raipur",
    "loadingDate": "2025-01-03",
    "unloadingDate": "2025-01-07"
  },
  "productType": "foods",
  "truckType": "container",
  "noOfTrucks": 4,
  "weight": 2500.0,
  "comment": "tea leaves",
  "status": "POSTED"
}
```

**Response**
```json
{
  "message": "Load updated successfully"
}
```

---

### 4. **Delete Load**

**DELETE** `/loads/{loadId}`

**Response**
```json
{
  "message": "Load deleted successfully"
}
```

---

## Endpoints for Bookings

### 1. **Create Booking**

**POST** `/booking`

**Request Body**
```json
{
  "loadId": "2542e431-e1b2-47fc-af40-5fa93b1025ee",
  "transporterId": "264",
  "proposedRate": 294.00,
  "comment": "steel and rods",
  "requestedAt": "2024-11-08"
}
```

**Response**
```json
{
  "id": "e0f66313-f7d6-4404-a9d0-7f9cf7328754",
  "loadId": "2542e431-e1b2-47fc-af40-5fa93b1025ee",
  "transporterId": "264",
  "proposedRate": 294.0,
  "comment": "steel and rods",
  "requestedAt": "2024-11-08T00:00:00.000+00:00",
  "status": "PENDING"
}
```

---

### 2. **Get Bookings**

**GET** `/booking`

**Query Params (Optional)**
- `transporterId`, `shipperId`, `status`

**Example Request**
```
/booking?transporterId=264&shipperId=345345
```

**Response**
```json
{
  "id": "e0f66313-f7d6-4404-a9d0-7f9cf7328754",
  "loadId": "2542e431-e1b2-47fc-af40-5fa93b1025ee",
  "transporterId": "264",
  "proposedRate": 294.0,
  "comment": "steel and rods",
  "requestedAt": "2024-11-08T00:00:00.000+00:00",
  "status": "PENDING"
}
```

---

### 3. **Get Booking by ID**

**GET** `/booking/{bookingId}`

**Response**
```json
{
  "id": "e0f66313-f7d6-4404-a9d0-7f9cf7328754",
  "loadId": "2542e431-e1b2-47fc-af40-5fa93b1025ee",
  "transporterId": "264",
  "proposedRate": 294.0,
  "comment": "steel and rods",
  "requestedAt": "2024-11-08T00:00:00.000+00:00",
  "status": "PENDING"
}
```

---

### 4. **Update Booking**

**PUT** `/booking/{bookingId}`

**Request Body**
```json
{
  "proposedRate": 310.00,
  "status": "ACCEPTED"
}
```

**Response**
```json
{
  "message": "Booking updated successfully"
}
```

---

## Notes

- All dates must be in ISO format (`yyyy-MM-dd`).
- Status transitions are handled internally and depend on booking state updates.
- Make sure all required fields are validated before sending the API request.

---

## Author

**Sandeep Kumar Verma**
GitHub: [@jaquen20](https://github.com/jaquen20)

---
