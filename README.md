# Travel and Tourism

A database coursework project with two parts.

- `dbmslab1/` contains a static travel brochure with pages for India, Egypt and the Maldives.
- `src/main/` contains a Spring Boot application with Thymeleaf views and database-backed travel, booking and account flows.

## Preview the brochure

From the repository root run `python3 -m http.server 8000 --directory dbmslab1` and open http://localhost:8000/. The brochure currently references a missing shared `style.css`. Its contact and booking UI should be treated as a prototype.

## Run the Spring application locally

Install Java 21 and MySQL. Configure a local database and set these environment variables in your shell or IDE.

| Variable | Purpose |
| --- | --- |
| `SPRING_DATASOURCE_URL` | MySQL JDBC URL for your local database |
| `SPRING_DATASOURCE_USERNAME` | Database user |
| `SPRING_DATASOURCE_PASSWORD` | Database password |
| `SPRING_SECURITY_USER_PASSWORD` | Local Spring Security password |

Use `./mvnw spring-boot:run` and open http://localhost:2024/. Maven dependencies are declared in `pom.xml`. The repository does not include a clean database schema export, so a fresh database may need schema provisioning for the booking flows.
