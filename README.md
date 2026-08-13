<div align="center">
  <img alt="Study Buddy logo" src="https://github.com/user-attachments/assets/2ca2cd71-4ec2-4aad-9695-b4bca763d0cb" width="120" />
  
  # Study Buddy

  An AI-powered study platform combining a smart academic calendar with intelligent learning tools.
</div>

---

## About

Study Buddy was born from a simple need: making studying feel less chaotic. It brings your calendar, course materials, and study tools together, with the help of AI, helping students organize their academic life without the usual chaos of scattered deadlines and materials.

This project is being built as a full-stack learning journey and portfolio piece, covering everything from relational database design to secure authentication and a responsive frontend.

## Tech Stack

**Backend**
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

**Frontend**
![React](https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=react&logoColor=black)
![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white)

**Planned**
![FastAPI](https://img.shields.io/badge/FastAPI-009688?style=for-the-badge&logo=fastapi&logoColor=white) — AI microservice (summaries, tutor chatbot, practice tests)

## Features

### Implemented

- **Authentication** - JWT-based login and registration, with encrypted password storage
- **Account management** - change password and change email (with automatic token refresh), both requiring current password confirmation
- **Courses** - create, list, update, and delete your own courses, each with a custom color
- **Events** - create, list, update status, and delete academic events (exams, assignments, study goals) tied to a specific course
- **Authorization** - every write operation verifies resource ownership before allowing changes; no user can modify another user's data
- **Centralized error handling** - consistent, typed exceptions mapped to appropriate HTTP status codes across the entire API

### Planned

- AI microservice (Python/FastAPI): automatic summaries, AI tutor chatbot, auto-generated practice tests
- Unified dashboard aggregating events across all courses
- Smart syllabus input (text or photo to automatic calendar events)
- Production deployment (environment variables, HTTPS, persistent JWT secret)

## Architecture

```
Frontend (React + TypeScript)
        |  REST API calls (JWT in Authorization header)
        v
Backend (Spring Boot)
        |  Controller -> Service -> Repository
        v
PostgreSQL (Docker container)
```

The backend follows a consistent layered architecture across all entities, with a centralized exception handler (`@RestControllerAdvice`) mapping custom exceptions to the correct HTTP responses (401, 403, 404, 409).

## Getting Started

### Prerequisites

- Java 26+
- Node.js
- Docker

### Backend

```bash
cd backend-java
docker-compose up -d          # starts PostgreSQL
./mvnw spring-boot:run        # starts the API on localhost:8080
```

### Frontend

```bash
cd frontend-react
npm install
npm run dev                   # starts on localhost:5173
```

## Project Structure

```
study-buddy/
├── backend-java/       # Spring Boot REST API
├── frontend-react/     # React + TypeScript SPA
└── docker-compose.yml  # PostgreSQL container
```

---

<div align="center">

*Personal portfolio project - feedback and suggestions welcome.*

</div>
