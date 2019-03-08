## Veterinary web project with Spring Boot and Spring Security
- How to use registering and login on spring boot and security with mysql.
- How to use insert customer and a lot of pets depending to customer on this project.
- How to use CRUD for user, customer and pets on this project.
##### Using Tools & Technologies
``` 
- Spring Boot
- Spring Security
- Jpa, Hibernate
- Mysql
- Thymeleaf
- Bootstrap
``` 
#### This sql query is not completely correct sql query below.
- You had better to look at this applicaton.properties pls. Database's table can be generated automatically, İf you write `spring.jpa.hibernate.ddl-auto=create` on application.properties.
- Your database's table can create regularly when you wrote `spring.jpa.hibernate.ddl-auto=create` on application.properties.
``` 
CREATE TABLE user(
    user_id INTEGER PRIMARY KEY  NOT NULL,
    username VARCHAR(20),
    password VARCHAR(20),
	reel_password VARCHAR(20),
    city VARCHAR(20),
    email VARCHAR(20)
);

CREATE TABLE user_role(
    user_id INTEGER PRIMARY KEY  NOT NULL,
    role_id INTEGER NOT NULL
);
CREATE TABLE role(
    role_id INTEGER PRIMARY KEY  NOT NULL,
    role VARCHAR(20)
);

CREATE TABLE customer(
    customerid INTEGER PRIMARY KEY  NOT NULL,
    firstname VARCHAR(20),
    lastname VARCHAR(20),
    phone_number VARCHAR(20),
    city VARCHAR(20),
    email VARCHAR(20)
);

CREATE TABLE pet(
    id INTEGER PRIMARY KEY  NOT NULL,
    type VARCHAR(20),
    name VARCHAR(20),
    age VARCHAR(20),
    problem VARCHAR(20),
    customerid INTEGER NOT NULL
);
``` 
