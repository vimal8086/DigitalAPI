# DigitalAPI
This API For Bus Reservation System

# REST API for Bus Reservation System Portal 

* We have developed this REST API for a Bus Reservation System Portal Application. This API performs all the fundamental CRUD operations of any Bus Reservation Application platform with user validation at every step.

## Tech Stack

* Java
* Spring Framework
* Spring Boot
* Spring Data JPA
* Hibernate
* MySQL

## Modules

* Login Module
* User Module
* Route Module
* Bus Module
* Destination Module

## Features

* User and Admin authentication & validation with session uuid.
* Admin Features:
    * Administrator Role of the entire application
    * Only registered admins with valid session token can add/update/delete route and bus from main database
    * Admin can access the details of different users and reservations.
* User Features:
    * Registering themselves with application, and logging in to get the valid session token
    * Viewing list of available buses and booking a reservation
    * Only logged in user can access his reservations, profile updation and other features.


## Installation & Run

* Before running the API server, you should update the database config inside the [application.yml]
* Update the port number, username and password as per your local database config.

```
    url: jdbc:mysql://localhost:3306/digital
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver


```

## API Root Endpoint

`https://localhost:8888/digital/api

(http://localhost:8080/digital/api/swagger-ui/index.html)


# API Usage Guide

## User Registration
To use this API, you must first create/register a user.

### Endpoint:
```
POST http://localhost:8080/digital/api/users/register
```

### Description:
- This API is used to register a new user.
- Provide necessary user details in the request body.

## Authentication & JWT Token
After registering a user, you need a JWT token to access other API endpoints.

### Endpoint:
```
POST http://localhost:8080/digital/api/auth/login
```

### Description:
- This API is used to authenticate a user and generate a JWT token.
- Provide the registered `UserID` and `Password` in the request body.
- Upon successful authentication, you will receive a JWT token.

## Accessing Other APIs
- Use the obtained JWT token in the `Authorization` header as a Bearer token.
- You can now access all secured API endpoints.

### Example Authorization Header:
```
Authorization: Bearer <your-jwt-token>
```

Ensure you include this token in all requests to authenticate and use the API securely.


