# Skills

## Built With
1. Spring Boot 1.5.12.RELEASE
2. Maven
3. Java Jdk 1.8
4. H2 in-memory database

## External Tools Used
Postman - API Development Environment

## Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the com.management.skills.SkillsApplication class from your IDE.

- Download the zip or clone the Git repository.
- Unzip the zip file (if you downloaded one)
- Open Command Prompt and Change directory (cd) to folder containing pom.xml
- Open IDE
- File -> Import -> Existing Maven Project -> Navigate to the folder where you unzipped the zip
- Select the project
- Choose the Spring Boot Application file (search for @SpringBootApplication)
- Right Click on the file and Run as Java Application

## Security
```
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-security</artifactId>
</dependency> 
```

Spring Boot Starter Security default username is user and password is password
For testing with Postman, the following header needs to be used:
```
Authorization Basic dXNlcjpwYXNzd29yZA==
```

## URLs

| URL  | Method | Json |
| ------------- | ------------- | ------------- |
| http://localhost:9000/skills/add-skill/Postgres  | POST  | |
| http://localhost:9000/skills/add-people-skill/2  | POST  | {"skill":"HTML","level":"Working"} |
| http://localhost:9000/skills/find-people/ross.geller@email.com | GET | |
| http://localhost:9000/skills/display-all-people | GET | |
| http://localhost:9000/skills/display-all-skills | GET | |
| http://localhost:9000/skills/update-skill-level/2 | PUT | {"skill":"Java","level":"Expert"} |
| http://localhost:9000/skills/remove-people-skill/2 | DELETE | {"skill":"Javascript"} |
| http://localhost:9000/skills/remove-people/2 | DELETE | |
| http://localhost:9000/skills/remove-skill/Java | DELETE | |
