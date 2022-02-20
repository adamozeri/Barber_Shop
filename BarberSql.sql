USE barber;


create table CustomerTable(FNAME varchar(20) not null,
LNAME varchar(20) not null,
phoneNumber varchar(10),
username varchar(15) not null
,password varchar(15) not null,
primary key(username));

insert into CustomerTable values
("Netanel","Habas","0527900955","netanel","1111"),
("Adam","Ozeri","0534576210","adam","2222");

create table AppointmentTable(username varchar(15) not null,
appointment_date date,
starting_time time,
ending_time time,
Haircut_type varchar(20) not null,
primary key (appointment_date,starting_time,ending_time),
FOREIGN KEY(username) REFERENCES CustomerTable(username));

insert into AppointmentTable values
("netanel","2022-02-20","11:00","11:30","Short");

select * from AppointmentTable;

create table dateTable(date date not null primary key
,available boolean);

select * from dateTable;

-- insert into dateTable values
-- ("2022-02-20",true),("2022-02-21",true),("2022-02-22",false);

SELECT * FROM customerTable;


create table startHrTable(date date not null primary key
,startHr time
,FOREIGN KEY(date) REFERENCES dateTable(date));

-- insert into startHrTable values("2022-02-20","11:00");


create table endHrTable(date date not null primary key
,endHr time
,FOREIGN KEY(date) REFERENCES dateTable(date));

-- insert into endHrTable values("2022-02-20","11:30");


create table barberTable(username varchar(15) primary key not null,
password varchar(15) not null,
name varchar(50) not null,
phoneNumber varchar(10) not null,
address varchar(50) not null,
startTime time not null,
endTime time not null);

insert into barberTable value
("Effi","1234","Effi Barbershop","0527291300","Bnei Ephraim 1","10:00","19:00");

select * from barberTable;

create table haircutTable(name varchar(20) primary key not null,
cost int not null,
duration float not null);

insert into haircutTable values
("Shades",100,1),
("Coloring",200,2),
("Straightening", 300,1),
("Kids",50,0.5),
("Short",60,0.5),
("Tips",80,0.5);

select * from haircutTable;


select *
from barberTable;

select *
from dateTable;





