# CodeDrill – Software Increment 1

CSIT321 G4 · Team **Dream Team**

Web application with a **Spring Boot** backend and a **React** frontend.
This increment covers **CRUD of the User entity** (from the ERD), a **login page**, and a **landing page**.

| Part | Details |
|---|---|
| Group | `com.csit321g4.dreamteam` |
| Artifact | `codedrill` |
| Backend | Spring Boot 3.3 · Java 17 · Maven · Spring Data JPA · MySQL |
| Frontend | React 18 · Vite · React Router |

## What you need installed
- JDK 17 or newer
- Maven (or open `backend` in IntelliJ / VS Code / Eclipse, which bundle it)
- Node.js 18 or newer
- XAMPP (start **MySQL** from the XAMPP Control Panel)

## How to run

### 1. Backend (port 8080)
```
cd backend
mvn spring-boot:run
```
- The database `codedrill_db` and the `users` table are created automatically.
- On the first run a demo account is created: **demo@codedrill.com / demo123**
- If your MySQL user or password is not `root` with an empty password, edit
  `backend/src/main/resources/application.properties`.

### 2. Frontend (port 5173)
```
cd frontend
npm install
npm run dev
```
Open http://localhost:5173 and log in with the demo account.

## What to show
1. **Login page** – log in with the demo account (wrong password shows an error).
2. **Landing page** – shows the logged-in user and the Users table.
3. **CRUD** – add, edit, and delete users from the landing page. Passwords are stored as BCrypt hashes.

## API endpoints
| Method | URL | Description |
|---|---|---|
| POST | `/api/users` | Create a user |
| GET | `/api/users` | List all users |
| GET | `/api/users/{id}` | Get one user |
| PUT | `/api/users/{id}` | Update a user (blank password keeps the old one) |
| DELETE | `/api/users/{id}` | Delete a user |
| POST | `/api/auth/login` | Log in (used by the login page) |

Example body for `POST /api/users` (Postman):
```json
{
  "fullName": "Juan Dela Cruz",
  "email": "juan@example.com",
  "password": "secret123",
  "program": "BSIT"
}
```
`program` must be `BSIT` or `BSCS`. Responses never include the password hash.

## Users table (from the ERD)
`user_id` (PK), `full_name`, `email` (unique), `password_hash`, `program`, `created_at`
The table is named `users` because `user` is a reserved word in some databases.

## Project structure
```
codedrill/
├── backend/   Spring Boot (model, repository, dto, service, controller, config)
└── frontend/  React (pages/Login.jsx, pages/Landing.jsx, api.js)
```
