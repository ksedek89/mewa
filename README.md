skrypty bazodanowe:

create schema mewa;
use mewa;

create table threshold_value
(
    id int not null AUTO_INCREMENT,
    value1 float not null,
    value2 float not null,
    value3 float not null,
    unit1 varchar(1) not null,
    unit2 varchar(1) not null,
    unit3 varchar(1) not null,

        primary key (id)
);

INSERT INTO mewa.threshold_value (id, value1, value2, value3, unit1, unit2, unit3) VALUES (1, 1, 5, 25, 'U', 'U', 'U');


Przykładowe ramki:
$PCARCC,230144,1,U,5,U,25,U*7a



docker run -p 127.0.0.1:3306:3306 --restart always  --name mewa-db -e MYSQL_ROOT_PASSWORD=test -d mariadb:latest

docker update --restart always/no mariadb

mysql -u root -p 


docker run -p 8085:8085 -p 8000:8000 --restart always  -d mewa-be:0.0.1 
