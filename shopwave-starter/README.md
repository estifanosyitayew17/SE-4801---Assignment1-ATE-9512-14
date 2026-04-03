# ShopWave Starter - Enterprise Application Development Assignment 1

## Student Information
- **Name**: [Your Name]
- **Student Number**: [Your Student Number]
- **Course**: SE 4801 - Enterprise Application Development
- **Assignment**: Assignment 1 - Java Fundamentals & Enterprise Web Applications
- **Date**: April 2026

---

## AI Usage Disclosure

As required by the Academic Integrity Declaration, I hereby disclose the use of AI-assisted tools in completing this assignment.

### AI Tools Used
- **GitHub Copilot** / **ChatGPT** / **[Specify the tool you used]**

### Sections Where AI Was Used

| Section | Description | Level of Assistance |
|---------|-------------|---------------------|
| C2 - Domain Model | Entity classes (Category, Product, Order, OrderItem, Status enum) | Heavy assistance - code structure and JPA annotations |
| C3 - Repository & Service Layer | ProductRepository, ProductService with @Transactional | Heavy assistance - business logic and Spring patterns |
| C4 - REST Controller | ProductController, GlobalExceptionHandler, ErrorResponse | Heavy assistance - REST endpoints and exception handling |
| C5 - Testing | RepositoryTest, ServiceTest (Mockito), ControllerTest (MockMvc) | Heavy assistance - test structure and mocking |

### How AI Was Used
1. **Code Generation**: Generated boilerplate code for entities, repositories, services, and controllers
2. **Error Resolution**: Helped debug compilation errors (Java 21 installation, Maven dependencies)
3. **Best Practices**: Provided guidance on Spring Boot conventions (DTOs, mappers, @Transactional)
4. **Testing**: Generated unit and integration test templates

### Verification Process
All AI-generated code was:
- ✅ Reviewed and understood before implementation
- ✅ Tested with `mvn test` (all tests passing)
- ✅ Run successfully with `mvn spring-boot:run`
- ✅ Manually tested API endpoints using curl
- ✅ Modified where necessary to fit specific requirements

### AI Tool Limitations Encountered
- Java 21 installation on Termux required manual troubleshooting
- Testcontainers bonus skipped due to Docker unavailability in Termux
- Some validation dependencies needed manual addition to pom.xml

### Statement of Original Work
I confirm that:
1. I understand all code I am submitting
2. The architecture and design decisions are my own
3. AI was used as a learning tool and code assistant, not as a substitute for understanding
4. This work has not been submitted for assessment in any other course

---

## Project Overview

ShopWave Starter is a Spring Boot e-commerce backend application demonstrating:
- RESTful API design
- JPA entity relationships (OneToMany, ManyToOne)
- Layered architecture (Controller → Service → Repository → Database)
- Global exception handling
- Unit and integration testing

## Technology Stack

| Technology | Version |
|------------|---------|
| Java | 21 |
| Spring Boot | 3.x |
| Database | H2 (in-memory) |
| Build Tool | Maven |
| Testing | JUnit 5, Mockito, MockMvc |
| Lombok | Latest |

## Dependencies

- Spring Web
- Spring Data JPA
- H2 Database
- Lombok
- Spring Boot Actuator
- Spring Boot Validation

---

## Setup Instructions

### Prerequisites
- Java 21 (JDK)
- Maven 3.6+
- Git

### Clone Repository
```bash
git clone [your-github-repo-url]
cd shopwave-starter