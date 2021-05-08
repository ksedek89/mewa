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

