# Employee Management Application with JWT Authentication

## Overview
> The Employee Management Application is designed to manage employee records within an organization. It provides secure access to authorized users based on their roles (Admin or User). JWT is used for authentication, ensuring a secure and efficient login process.

## Features

### Authentication using JWT:

> Users log in using their credentials (username and password).
> Upon successful authentication, the server generates a JWT token containing user information and roles.
> The token is sent to the client and included in subsequent requests for authorization.

### Role-Based Access Control (RBAC):

Two primary roles:
> Admin: Has full access to manage employees (add, update, delete).
> User: Can view employee details but cannot modify them.
RBAC ensures that users can only perform actions allowed by their roles.

### Endpoints:

 - /login: Authenticates users and returns a JWT token.
 - /employees: Allows Admin to add new employees.
 - /employees/{id}: Allows Admin to update or delete an employee.
 - /employees/all: Allows all users (Admin and User) to view employee details.

### Security Flow:

 - User logs in with credentials.
 - Server validates credentials and issues a JWT token.
 - User includes the token in subsequent requests.
 - Server verifies the token and grants access based on roles.

### Benefits

 - Security: JWT ensures secure authentication and authorization.
 - Efficiency: Stateless tokens reduce server-side session management.
 - Scalability: RBAC allows easy addition of new roles and permissions.

In summary, the Employee Management Application leverages JWT for secure authentication and RBAC to manage employee records effectively. Admins can add employees, while all users can view employee details.
