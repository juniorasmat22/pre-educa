# 🎓 Pre-Educa API - Sistema de Preparación Académica

Backend oficial de la plataforma **Pre-Educa**, un sistema diseñado para la gestión académica, aprendizaje adaptativo y
simulacros de examen para postulantes universitarios.

La aplicación está construida siguiendo principios de **Clean Architecture** y **Domain-Driven Design (DDD)**,
garantizando escalabilidad, mantenibilidad y separación clara de responsabilidades.

---

## 🚀 Tecnologías Principales

- Java 17
- Spring Boot 3
- Spring Security + JWT
- PostgreSQL
- MongoDB
- Lombok
- MapStruct
- Docker & Docker Compose
- Gradle

---

## 🏗️ Arquitectura del Proyecto

El proyecto implementa una arquitectura por capas:

    📦 pre-educa-api
     ┣ 📂 domain
     ┃ ┣ 📜 entidades
     ┃ ┣ 📜 repositorios (interfaces)
     ┃ ┗ 📜 excepciones
     ┣ 📂 application
     ┃ ┣ 📜 controladores (REST)
     ┃ ┣ 📜 servicios
     ┃ ┗ 📜 casos de uso
     ┣ 📂 adapters
     ┃ ┣ 📜 persistencia (PostgreSQL / MongoDB)
     ┃ ┣ 📜 configuración de seguridad
     ┃ ┗ 📜 utilitarios externos

### 🔹 Domain

Contiene las entidades de negocio, reglas del dominio y contratos de repositorio.

### 🔹 Application

Orquesta los casos de uso, servicios y controladores REST.

### 🔹 Adapters

Implementaciones técnicas como bases de datos, seguridad y configuraciones externas.

---

## 📦 Módulos y Funcionalidades

### 1️⃣ Gestión Académica (Maestros)

- Administración de Áreas
- Gestión de Carreras
- Organización de Cursos
- Gestión de Temas

---

### 2️⃣ Contenido de Aprendizaje

- 📚 Teoría
- 🧠 Flashcards
- ❓ Banco de Preguntas por dificultad
- 📊 Filtrado dinámico de contenido

MongoDB es utilizado para almacenar contenido educativo dinámico y de alto volumen.

---

### 3️⃣ Sistema de Simulacros y Evaluación

- 📝 Generador automático de exámenes personalizados
- ⏱️ Guardado de progreso en tiempo real
- 📊 Cálculo automático de puntajes
- 📈 Resultados detallados por tema
- 🎯 Evaluación basada en área de estudio

---

### 4️⃣ Analítica y Progreso

- 🔎 Análisis de debilidades
- 🛤️ Ruta de aprendizaje personalizada
- 🏆 Rankings globales y por área
- 📈 Historial de evolución del usuario

---

### 5️⃣ Seguridad y Auditoría

- 🔐 Registro y Login con JWT
- 🔄 Refresh Token
- 🚪 Logout seguro
- 📋 Auditoría de cambios en entidades críticas

---

## 🛠️ Configuración y Ejecución

### ✅ Requisitos

- JDK 17
- Docker
- Docker Compose
- Gradle

---

### 1️⃣ Levantar infraestructura (PostgreSQL + MongoDB)

```bash
docker-compose up -d
```

---

### 2️⃣ Configuración

Revisar el archivo:

    src/main/resources/application.yml

Configurar credenciales de:

- PostgreSQL
- MongoDB
- JWT Secret
- Perfiles (dev / prod)

---

### 3️⃣ Ejecutar la aplicación

```bash
./gradlew bootRun
```

La API estará disponible en:

    http://localhost:8080

---

## 📍 Endpoints Principales

### 🔐 Autenticación

| Método | Endpoint              | Descripción         |
|--------|-----------------------|---------------------|
| POST   | /api/v1/auth/login    | Login de usuario    |
| POST   | /api/v1/auth/register | Registro de usuario |
| POST   | /api/v1/auth/refresh  | Renovar token       |

---

### 📝 Simulacros

| Método | Endpoint                   | Descripción     |
|--------|----------------------------|-----------------|
| GET    | /api/v1/simulacros/generar | Generar examen  |
| POST   | /api/v1/simulacros/evaluar | Evaluar examen  |
| GET    | /api/v1/simulacros/{id}    | Obtener detalle |

---

### 📚 Contenido

| Método | Endpoint                                | Descripción        |
|--------|-----------------------------------------|--------------------|
| GET    | /api/v1/contenido/temas/{id}/teoria     | Obtener teoría     |
| GET    | /api/v1/contenido/temas/{id}/flashcards | Obtener flashcards |
| GET    | /api/v1/preguntas                       | Listar preguntas   |

---

### 📊 Progreso

| Método | Endpoint                       | Descripción      |
|--------|--------------------------------|------------------|
| GET    | /api/v1/progreso/usuarios/{id} | Consultar avance |
| GET    | /api/v1/rankings               | Ver ranking      |

---

## 📈 Base de Datos

### PostgreSQL

Almacena:

- Usuarios
- Áreas
- Carreras
- Cursos
- Resultados
- Rankings

### MongoDB

Almacena:

- Teoría
- Flashcards
- Preguntas detalladas

---

## 🧪 Buenas Prácticas Implementadas

- Principios SOLID
- Clean Architecture
- DDD
- DTO Pattern
- Repository Pattern
- Seguridad Stateless con JWT
- Manejo global de excepciones

---

## 👨‍💻 Autor

Desarrollado como parte del sistema **Pre-Educa**.

---

## 📄 Licencia

Proyecto de uso privado y educativo.
