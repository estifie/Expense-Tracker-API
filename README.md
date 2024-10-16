# Expense Tracker API

## Description

The Expense Tracker API is a RESTful API that allows users to track their expenses and subscriptions. API utilizes JWT
for authentication and role-based access control. The API is built using Spring Boot.

## Installation

1. Clone the repository

```bash
git clone https://github.com/axelnt/Expense-Tracker-API.git
```

2. Navigate to the project directory

```bash
cd Expense-Tracker-API
```

3. Build the project

```bash
mvn clean install
```

4. Seed the database

```bash
mvn clean spring-boot:run -D spring-boot.run.arguments=--seeder=user
```

5. Run the application

```bash
mvn spring-boot:run
```

## Usage

To use the API, you need to have an account. When you run the seeder, a default user account is created with credentials
specified in the `application.properties` file. You can use these credentials to authenticate and access the API. Do not
forget to change the default credentials in the `application.properties` file.

### Authentication

To authenticate, send a POST request to the `/api/v1/auth/login` endpoint with the following payload:

```json
{
  "username": "admin",
  "password": "password"
}
```

If the credentials are correct, the API will return a JWT token. Here is an example response:

```json
{
  "status": "success",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdTIiOlJhZG1pbiIsImlhdCI6MTcyODY3ODA2MSwiZXhwIjoxNzMyMjc4MDYxfQ.ams4D9yjzEux0-BMi6DaEYaO778pSsFs8IZKH7ydB6w"
  }
}
```

You can use this token to access the protected endpoints. To do so, include the token in the `Authorization` header of
the request:

```
Authorization Bearer <token>
```

### Role Based Access Control

The API uses role-based access control to restrict access to certain endpoints. You can jump to the [Roles](#roles)
section to see the available roles and their descriptions.

## Endpoints

Here is a list of available endpoints:

| Method | Endpoint                          | Description                   | Role                                                    |
|--------|-----------------------------------|-------------------------------|---------------------------------------------------------|
| POST   | /v1/auth/login                    | Authenticate user             | -                                                       |
| POST   | /v1/auth/register                 | Register a new user           | -                                                       |
| GET    | /v1/users                         | Get all users                 | MANAGE_USERS OR VIEW_USERS                              | 
| GET    | /v1/users/{username}              | Get user by username          | MANAGE_USERS OR VIEW_USERS                              |
| GET    | /v1/users/{username}/permissions  | Get user permissions          | MANAGE_USERS OR VIEW_USERS                              |
| POST   | /v1/users/{username}/permissions  | Grant new permission to user  | MANAGE_PERMISSIONS OR GRANT_PERMISSION                  |
| DELETE | /v1/users/{username}/permissions  | Revoke permission from user   | MANAGE_PERMISSIONS OR REVOKE_PERMISSION                 |
| GET    | /v1/expenses                      | Get all expenses              | MANAGE_EXPENSES OR VIEW_EXPENSES                        |
| GET    | /v1/expenses/{id}                 | Get expense by id             | OWNERSHIP OR MANAGE_EXPENSES OR VIEW_EXPENSES           |
| DELETE | /v1/expenses/{id}                 | Delete expense by id          | OWNERSHIP OR MANAGE_EXPENSES                            |
| GET    | /v1/expenses/user/{username}      | Get all expenses by user      | MANAGE_EXPENSES OR VIEW_EXPENSES                        |
| POST   | /v1/expenses/user/{username}      | Create a new expense          | OWNERSHIP OR MANAGE_EXPENSES                            |
| GET    | /v1/subscriptions                 | Get all subscriptions         | MANAGE_SUBSCRIPTIONS OR VIEW_SUBSCRIPTIONS              |
| GET    | /v1/subscriptions/{id}            | Get subscription by id        | OWNERSHIP OR MANAGE_SUBSCRIPTIONS OR VIEW_SUBSCRIPTIONS |
| DELETE | /v1/subscriptions/{id}            | Delete subscription by id     | OWNERSHIP OR MANAGE_SUBSCRIPTIONS                       |
| GET    | /v1/subscriptions/user/{username} | Get all subscriptions by user | MANAGE_SUBSCRIPTIONS OR VIEW_SUBSCRIPTIONS              |
| POST   | /v1/subscriptions/user/{username} | Create a new subscription     | OWNERSHIP OR MANAGE_SUBSCRIPTIONS                       |
| PUT    | /v1/subscriptions/{id}/deactivate | Deactivate subscription by id | OWNERSHIP OR MANAGE_SUBSCRIPTIONS                       |
| PUT    | /v1/subscriptions/{id}/activate   | Activate subscription by id   | OWNERSHIP OR MANAGE_SUBSCRIPTIONS                       |

### Authentication Endpoints

#### POST /v1/auth/login

Authenticate user

##### Request

```json
{
  "username": "admin",
  "password": "password"
}
```

##### Response

```json
{
  "status": "success"
}
```

#### POST /v1/auth/register

Register a new user

##### Request

```json
{
  "username": "user",
  "password": "password"
}
```

##### Response

```json
{
  "status": "success"
}
```

### User Endpoints

#### GET /v1/users

Get all users

Requires `MANAGE_USERS` or `VIEW_USERS` role

##### Response

```json
{
  "status": "success",
  "data": [
    {
      "username": "admin",
      "roles": [],
      ...
    },
    {
      "username": "user",
      "roles": [
        "MANAGE_USERS"
      ],
      ...
    }
  ]
}
```

#### GET /v1/users/{username}

Get user by username

Requires `MANAGE_USERS` or `VIEW_USERS` role

##### Response

```json
{
  "status": "success",
  "data": {
    "username": "admin",
    "roles": [],
    ...
  }
}
```

#### GET /v1/users/{username}/permissions

Get user's permissions

Requires `MANAGE_PERMISSIONS` or `VIEW_PERMISSIONS` role

##### Response

```json
{
  "status": "success",
  "data": [
    "OWNERSHIP",
    "MANAGE_USERS",
    ...
  ]
}
```

#### POST /v1/users/{username}/permissions

Grant new permission to user

Requires `MANAGE_PERMISSIONS` or `GRANT_PERMISSION` role

##### Request

```json
{
  "role": "MANAGE_USERS"
}
```

##### Response

```json
{
  "status": "success"
}
```

#### DELETE /v1/users/{username}/permissions

Revoke permission from user

Requires `MANAGE_PERMISSIONS` or `REVOKE_PERMISSION` role

##### Request

```json
{
  "role": "MANAGE_USERS"
}
```

##### Response

```json
{
  "status": "success"
}
```

### Expense Endpoints

#### GET /v1/expenses

Get all expenses

Requires `MANAGE_EXPENSES` or `VIEW_EXPENSES` role

##### Response

```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "amount": 100.0,
      ...
    },
    {
      "id": 2,
      "amount": 200.0,
      ...
    }
  ]
}
```

#### GET /v1/expenses/{id}

Get expense by id

Requires `MANAGE_EXPENSES`, or `VIEW_EXPENSES` role if the expense is not owned by the authenticated user

##### Response

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "amount": 100.0,
    ...
  }
}
```

#### DELETE /v1/expenses/{id}

Delete expense by id

Requires `MANAGE_EXPENSES` role if the expense is not owned by the authenticated user

##### Response

```json
{
  "status": "success"
}
```

#### GET /v1/expenses/user/{username}

Get all expenses by user

Requires `MANAGE_EXPENSES` or `VIEW_EXPENSES` role

##### Response

```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "amount": 100.0,
      ...
    },
    {
      "id": 2,
      "amount": 200.0,
      ...
    }
  ]
}
```

#### POST /v1/expenses/user/{username}

Create a new expense

Requires `OWNERSHIP` or `MANAGE_EXPENSES` role

##### Request

```json
{
  "amount": 100.0,
  ...
}
```

##### Response

```json
{
  "status": "success"
}
```

### Subscription Endpoints

#### GET /v1/subscriptions

Get all subscriptions

Requires `MANAGE_SUBSCRIPTIONS` or `VIEW_SUBSCRIPTIONS` role

##### Response

```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "amount": 100.0,
      ...
    },
    {
      "id": 2,
      "amount": 200.0,
      ...
    }
  ]
}
```

#### GET /v1/subscriptions/{id}

Get subscription by id

Requires `MANAGE_SUBSCRIPTIONS`, or `VIEW_SUBSCRIPTIONS` role if the subscription is not owned by the authenticated user

##### Response

```json
{
  "status": "success",
  "data": {
    "id": 1,
    "amount": 100.0,
    ...
  }
}
```

#### DELETE /v1/subscriptions/{id}

Delete subscription by id

Requires `MANAGE_SUBSCRIPTIONS` role if the subscription is not owned by the authenticated user

##### Response

```json
{
  "status": "success"
}
```

#### GET /v1/subscriptions/user/{username}

Get all subscriptions by user

Requires `MANAGE_SUBSCRIPTIONS` or `VIEW_SUBSCRIPTIONS` role

##### Response

```json
{
  "status": "success",
  "data": [
    {
      "id": 1,
      "amount": 100.0,
      ...
    },
    {
      "id": 2,
      "amount": 200.0,
      ...
    }
  ]
}
```

#### POST /v1/subscriptions/user/{username}

Create a new subscription

Requires `MANAGE_SUBSCRIPTIONS` role if the username is the authenticated user's username

##### Request

```json
{
  "amount": 100.0,
  ...
}
```

##### Response

```json
{
  "status": "success"
}
```

#### PUT /v1/subscriptions/{id}/deactivate

Deactivate subscription by id

Requires `MANAGE_SUBSCRIPTIONS` role if the subscription is not owned by the authenticated user

##### Response

```json
{
  "status": "success"
}
```

#### PUT /v1/subscriptions/{id}/activate

Activate subscription by id

Requires `MANAGE_SUBSCRIPTIONS` role if the subscription is not owned by the authenticated user

##### Response

```json
{
  "status": "success"
}
```

### Roles

| Role                     | Description                                                           |
|--------------------------|-----------------------------------------------------------------------|
| OWNERSHIP                | Each user has this role. It allows the user to access their own data. |
| MANAGE_USERS             | Allows the user to manage other users.                                |
| VIEW_USERS               | Allows the user to view other users.                                  |
| HARD_DELETE_USER         | Allows the user to hard delete other users.                           |
| MANAGE_PERMISSIONS       | Allows the user to manage permissions.                                |
| VIEW_PERMISSIONS         | Allows the user to view permissions.                                  |
| GRANT_PERMISSION         | Allows the user to grant permissions to other users.                  |
| REVOKE_PERMISSION        | Allows the user to revoke permissions from other users.               |
| MANAGE_EXPENSES          | Allows the user to manage expenses.                                   |
| VIEW_EXPENSES            | Allows the user to view expenses.                                     |
| HARD_DELETE_EXPENSE      | Allows the user to hard delete expenses.                              |
| MANAGE_SUBSCRIPTIONS     | Allows the user to manage subscriptions.                              |
| VIEW_SUBSCRIPTIONS       | Allows the user to view subscriptions.                                |
| HARD_DELETE_SUBSCRIPTION | Allows the user to hard delete subscriptions.                         |
