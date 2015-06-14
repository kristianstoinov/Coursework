create database mydbKursova;
use mydbKursova;

create table plants(
id int(20) unsigned auto_increment PRIMARY KEY, 
class VARCHAR(255) NOT NULL,
family VARCHAR(255) default NULL,
genus varchar(255) default null,
species varchar(255) not null ,
img LONGBLOB NULL

)ENGINE = InnoDB;

GRANT all
ON mydbKursova.*
TO 'christian'@'localhost'
IDENTIFIED BY 'password';
INSERT INTO plants(id,class,family,genus,species,img)
VALUES(null,'Magnoliopsida','Lamiaceae','Salvia','Salvia_divinorum','C://Users/kasi/Desktop/Salvia_officinalis0.jpg'),
(null, 'Magnoliopsida','Cannabaceae','Cannabis','Cannabis_sativa','C://Users/kasi/Desktop/Cannabis_sativa_leaf_diagnostic_venation_2012_01_23_0829_c'),
(null, 'Magnoliopsida','Papaveraceae','Papaver','Papaver_somniferum','C://Users/kasi/Desktop/220px-Opium_poppy.jpg');


create table classes(
class_id int(20) unsigned auto_increment Primary Key,
family VARCHAR(255) NOT NULL,
genus VARCHAR(255) default NULL,
species varchar(255) not null
)ENGINE = InnoDB;

INSERT INTO classes(class_id,family,genus,species)
Values(null,'Lamiaceae','Salvia','Salvia_divinorum'),
(null,'Cannabaceae','Cannabis','Cannabis_sativa'),
(null,'Papaveraceae','Papaver','Papaver_somniferum');

create table families(
family_id int(20) unsigned auto_increment primary key, 
genus varchar(255) default null,
species varchar(255) not null
)ENGINE = InnoDB;

INSERT INTO families(family_id,genus,species)
Values(null,'Salvia','Salvia_divinorum'),
(null,'Cannabis','Cannabis_sativa'),
(null,'Papaver','Papaver_somniferum');

create table genuses(
genus_id int(20) unsigned auto_increment primary key,
species varchar(255) not null
)ENGINE = InnoDB;

INSERT INTO genuses(genus_id,species)
Values(null,'Salvia_divinorum'),
(null,'Cannabis_sativa'),
(null,'Papaver_somniferum');

create table species(
species_id int(20) unsigned auto_increment primary key,
species_name varchar(255) not null
)ENGINE=InnoDB;

INSERT INTO species(species_id,species_name)
Values(null,'Salvia_divinorum'),
(null,'Cannabis_sativa'),
(null,'Papaver_somniferum');









