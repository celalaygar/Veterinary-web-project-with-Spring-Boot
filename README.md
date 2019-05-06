# Veterinary web project with Spring Boot and Spring Security
- This project is simple spring boot and security and based role project for security
- How to use registering and login on spring boot and security with mysql.
- How to use insert customer and a lot of pets depending to customer on this project.
- How to use CRUD for user, customer and pets on this project.

## How to run this project.
- Download this project then open this project on eclipse.
- After opening this project. right click on project then configure-> convert to maven project.
- Open application.properties file. write ` spring.jpa.hibernate.ddl-auto=create `.
- You need to make your database settings.
- You need to create a database called `auth` on wampserver & mysql. if you want to change database's name you can change by setting on application properties.
``` 
spring.datasource.url = jdbc:mysql://localhost:3306/auth?useUnicode=yes&characterEncoding=UTF-8
spring.datasource.username = root
spring.datasource.password =
``` 
- Then you can run this project on eclipse.
- Your database's table can create regularly After writing on application.properties file.
- After running this project, you can change setting ` spring.jpa.hibernate.ddl-auto=update` on application properties file.
- because this project contain devtools for `Automatic reload` throug devtools. 
- Let's start using this project.
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
