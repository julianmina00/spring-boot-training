# Spring Boot Training
This is the example code for Spring Boot training. This training has three parts.
The first one aims to explain the basis of Sring Boot and Spring MVC. Additionally, some other topics like Maven and Swagger.
The second part is meant to explain Spring Data/JPA by adding a persistence layer to code implemented until the first part. In this part, Docker will be explained to create a PostgreSQL database. Other technologies explained in this session will be Flyway and Lombok.
The final part of this training is focused on Apache Camel and Apache Kafka. This part aims to explain how those technologies can be used to integrate Spring Boot services.


# Create Database

## Create PostgreSQL container
```
$ docker run --name=postgres -e POSTGRES_PASSWORD=docker -d -p 5432:5432 postgres
```

## Access Postgres to create database
```
# docker exec -it postgres psql -U postgres
```

## Create database scripts
```
$ create user dbuser with encrypted password 'dbuser';
$ create database listdb owner dbuser;
```

## Create tables scripts
```
CREATE TABLE public.lists (
	id bigserial NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(500) NOT NULL,
	CONSTRAINT lists_PK PRIMARY KEY (id)
);
CREATE TABLE public.items (
	id bigserial NOT NULL,
	list bigserial NOT NULL,
	name varchar(100) NOT NULL,
	description varchar(500) NOT NULL,
	CONSTRAINT items_PK PRIMARY KEY (id),
	CONSTRAINT items_lists_FK FOREIGN KEY (list) REFERENCES lists(id) ON DELETE RESTRICT ON UPDATE RESTRICT
);
```
## Postgres Commands (Cheat Sheet)
* Listing databases: \list or \l
* Switching Databases: \connect or \c
* Listing Tables: \dt
