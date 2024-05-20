# Employee Management Application with JWT Authentication

## Overview
> The Employee Management Application is designed to manage employee records within an organization. It provides secure access to authorized users based on their roles (Admin or User). JWT is used for authentication, ensuring a secure and efficient login process.

## Features

### Authentication using JWT:

-  Users log in using their credentials (username and password).
-  Upon successful authentication, the server generates a JWT token containing user information and roles.
-  The token is sent to the client and included in subsequent requests for authorization.

### Role-Based Access Control (RBAC):

Two primary roles:
>  Admin: 
 - Has full access to manage employees (add, update, delete).
 
>  User/ Other Roles: 
 - Can view employee details but cannot modify them.
 - RBAC ensures that users can only perform actions allowed by their roles.
 
 
### End Points:

> Authentication:
 - /api/authenticate: Authenticates users and returns a JWT token. [POST]
 - /api/validate-token: Validates the provided Token. [POST]

 
> Role:
 - /api/roles: Get All User Roles. [GET]
 - /api/roles/add: Add new UserRole. [POST]
 - /api/roles/{id}: Find UserRole by Id. [GET]
 - /api/roles/{id}: Update UserRole. [PUT]
 - /api/roles/{id}: Delete UserRole by Id. [DELETE] 
 
> User:
 - /api/users: Get All users. [GET]
 - /api/users/add: Add new User. [POST]
 - /api/users/{id}: Find User by Id. [GET]
 - /api/users/{id}: Update User. [PUT]
 - /api/users/{id}: Delete User by Id. [DELETE] 

 
 
> Employee:
 - /api/employees: Get All Employees. [GET]
 - /api/employees/add: Add new Employee. [POST]
 - /api/employees/{id}: Find Employee by Id. [GET]
 - /api/employees/{id}: Update Employee. [PUT]
 - /api/employees/{id}: Delete Employee by Id. [DELETE]
 - /api/employees/search/{keyword}: Finds all the Employees who have the given Keyword in their FirstName [GET] 
 - /api/employees/sort/order?*direction*: Finds all the Employees and Sort them by give *direction* -- asc or desc [GET] 

  
### Security Flow:

 - User logs in with credentials.
 - Server validates credentials and issues a JWT token.
 - User includes the token in subsequent requests.
 - Server verifies the token and grants access based on roles.

### Benefits

 - Security: JWT ensures secure authentication and authorization.
 - Efficiency: Stateless tokens reduce server-side session management.
 - Scalability: RBAC allows easy addition of new roles and permissions.

### Summary

In summary, the Employee Management Application leverages JWT for secure authentication and RBAC to manage employee records effectively. Admins can add employees, while all users can view employee details.

### Suggested IDE

- Eclipse
- STS
- IntelliJ

## WhiteListed End Points

> Application 
- /api/authenticate
- /api/validate-token
- /api/users/add
- /api/roles/add
- /api/users
- /api/roles
- /api/h2-console/**
- /api/users/accessdenied

> Swagger Related
 - /api/v3/api-docs/**
 - /api/swagger-ui.html
 - /api/swagger-ui/**

## Authenticated Required End Points

> Application 
- /api/employees/search/{keyword} 
- /api/employees/sort 
- /api/employees

## Role Based End Points

> Application 
- /api/users/{id} 
- /api/roles/{id} 
- /api/employees/{id} 
- /api/employees/add


##### Swagger link

 - [Swagger Link](https://http://localhost:8090/api/swagger-ui/index.html) - https://http://localhost:8090/api/swagger-ui/index.html
 
## Gradle Step (if failed load/build the project properly)

![](screenshots/83.jpg)

![](screenshots/84.jpg)

## How To Start The application and Test

![](screenshots/82.jpg)

#### Look for the logs that the application is started or not

![](screenshots/80.jpg)

#### Note - Application is Running on Port : 8090

#### Once Started Open Swagger

![](screenshots/81.jpg)

##### link

 - [Swagger Link](https://http://localhost:8090/api/swagger-ui/index.html) - https://http://localhost:8090/api/swagger-ui/index.html


## Project API WalkThrough Using Swagger

### Authentication End Points and Home Page

![](screenshots/0.jpg)

### Authenticate and Get Token

![](screenshots/2.jpg)

![](screenshots/3.jpg)

![](screenshots/4.jpg)

#### Copy the Token and Add in Swagger Authorize

![](screenshots/5.jpg)

![](screenshots/6.jpg)

![](screenshots/7.jpg)

![](screenshots/8.jpg)

### Validate Token

![](screenshots/9.jpg)

![](screenshots/10.jpg)

![](screenshots/11.jpg)

### User Role End Points

![](screenshots/16.jpg)

### Get All User Roles

![](screenshots/17.jpg)
 ### Add User Role

![](screenshots/18.jpg)

![](screenshots/19.jpg)

![](screenshots/20.jpg)

#### Verify added User Role

![](screenshots/21.jpg)

### User End Points

![](screenshots/21.5.jpg)

### Get All Users

![](screenshots/23.jpg)

![](screenshots/22.jpg)

### Add User

![](screenshots/24.jpg)

![](screenshots/25.jpg)

![](screenshots/26.jpg)

#### Verify added User

![](screenshots/27.jpg)

### Employee End Points

![](screenshots/28.jpg)

## Role ADMIN Work Flow

![](screenshots/38.jpg)

![](screenshots/39.jpg)

#### Set Generated Token In Swagger Authorize Section

![](screenshots/40.jpg)

### Add Employee

![](screenshots/41.jpg)

![](screenshots/42.jpg)

![](screenshots/43.jpg)

#### Adding One More Employee

![](screenshots/44.jpg)

![](screenshots/45.jpg)

#### Adding One More Employee

![](screenshots/46.jpg)

![](screenshots/47.jpg)

### Get All Employees and Verify Added Employees

![](screenshots/48.jpg)

![](screenshots/49.jpg)

![](screenshots/50.jpg)

### Get Employee By Id

![](screenshots/51.jpg)

![](screenshots/52.jpg)

### Edit Employee By Id

![](screenshots/53.jpg)

![](screenshots/54.jpg)

![](screenshots/55.jpg)

#### Verify Edited Employee

![](screenshots/56.jpg)

### Find All Employees by Keyword in their First Name

![](screenshots/57.jpg)

![](screenshots/58.jpg)

### Find All Employees in Sorted Order based on passed 'order' Parameter

![](screenshots/59.jpg)

![](screenshots/60.jpg)

### Delete Employee By Id

#### All Employees Before Deletion

![](screenshots/62.jpg)

![](screenshots/61.jpg)

![](screenshots/63.jpg)

#### Verify Deleted Employee

![](screenshots/64.jpg)


## Role Other Than "ADMIN" Work Flow ( "USER" )

![](screenshots/29.jpg)

![](screenshots/30.jpg)

### Add Employee

![](screenshots/31.jpg)

![](screenshots/32.jpg)

![](screenshots/33.jpg)

#### Error If the Token is not Added to Swagger Authorize Section

![](screenshots/34.jpg)

#### Added Token to Swagger Authorize Section

![](screenshots/40.jpg)

#### Not Enough Privileges

![](screenshots/34.5.jpg)


### Get All Employees (Works With Role 'User')

![](screenshots/65.jpg)

### Find All Employees by Keyword in their First Name (Works With Role 'User')

![](screenshots/66.jpg)

### Find All Employees in Sorted Order based on passed 'order' Parameter (Works With Role 'User')

![](screenshots/67.jpg)

### Delete Employee, Edit Employee, FInd Employee By Id (Does NOT Works With Role 'User')

#### Example of Delete Employee End Point

![](screenshots/35.jpg)

![](screenshots/36.jpg)

![](screenshots/37.jpg)


## Other Corner Error Cases

### If No JWT Token Is Provided

![](screenshots/76.jpg)

![](screenshots/77.jpg)

### If Expired JWT Token Is Provided

![](screenshots/91.jpg)

![](screenshots/92.jpg)
