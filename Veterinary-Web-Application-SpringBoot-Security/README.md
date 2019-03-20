# Veterinary web project with Spring Boot and Spring Security
- This project is simple spring boot and security and based role project for security
- How to use registering and login on spring boot and security with mysql.
- How to use insert customer and a lot of pets depending to customer on this project.
- How to use CRUD for user, customer and pets on this project.

## How to run this project.
- 1. Download this project then open this project on eclipse.
- 2. After opening this project. right click on project then configure-> convert to maven project.
- 3. Open application.properties file. write ` spring.jpa.hibernate.ddl-auto=create ` then you can run this project on eclipse.
- 4. After writing on application.properties file Your database's table can create regularly after writing or typing
- 5. While running this project then write again ` spring.jpa.hibernate.ddl-auto=update ` because this project contain devtools for `Automatic reload` throug devtools. 
- 6. Let's start using this project.
#### Using Tools & Technologies
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
