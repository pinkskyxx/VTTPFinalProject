drop database if exists shange;

create database shange;

use shange;

create table user_init (
   userName varchar(20) not null,
   password varchar(20) not null,
   fullName varchar(20) not null,
   address varchar(256) not null,
   dateOfBirth timestamp,
   email varchar(64) not null,
   phoneNo varchar(8) not null,
   dateOfSignup timestamp,
   gender varchar(5),
   type varchar(15) not null,

   primary key(userName)
);

create table login_init (
   userName varchar(20) not null,
   password varchar(20) not null,
   type varchar(15) not null,

   primary key(userName)
);

create table profile (
   userName varchar(20) not null,
   image varchar(128) not null,
   skills varchar(64) not null,
   availability varchar(64) not null,
   pastParticipation varchar(256) not null,
   clientId varchar(128),
   clientSecret varchar(128),

   primary key(userName)
);

create table mapEventRequest (
   id int auto_increment, 
   address varchar(256) not null,
   locX double,
   locY double,
   password varchar(6) not null,
   myid varchar(20) not null,
   title varchar(64) not null,
   description varchar(256) not null,
   eventDate timestamp,
   requestDate timestamp,
   status boolean,
   reply varchar(256),

   primary key(id)
);

create table allEvent (
   id int auto_increment,
   mysqlID int,
   redisID varchar(40),
   userName varchar(20) not null,
   address varchar(256),
   title varchar(64) not null,
   description varchar(256) not null,
   eventDate timestamp,
   requestDate timestamp,
   creator varchar(20),
   request varchar(20),
   confirm varchar(20),
   
   primary key(id)
);

grant all privileges on shange.* to fred@'%';

flush privileges;