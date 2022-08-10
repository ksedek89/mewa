mysql -u root -p

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

create table device(
                       id int not null AUTO_INCREMENT,
                       moxa_number int not null,
                       moxa_id int not null,
                       device_type varchar(10) not null,
                       direction_angle int,
                       threshold_pressure int ,
                       active varchar(1),
                       primary key (id)
);

alter table device add column device_id int;

alter table device add column type  varchar(4);

insert into mewa.device (id, moxa_number, moxa_id, device_type, direction_angle, threshold_pressure, active, device_id, type)
values  (25, 1, 1, 'PRESS', null, 500, 'N', 1, 'REAL'),
        (26, 1, 2, 'PRESS', null, 500, 'N', 2, 'REAL'),
        (27, 1, 3, 'PRESS', null, 500, 'N', 3, 'REAL'),
        (28, 1, 4, 'PRESS', null, 500, 'N', 4, 'REAL'),
        (29, 2, 1, 'PRESS', null, 100, 'N', 10, 'REAL'),
        (30, 2, 2, 'PRESS', null, 500, 'N', 6, 'REAL'),
        (31, 2, 3, 'PRESS', null, 100, 'N', 9, 'REAL'),
        (32, 2, 4, 'PRESS', null, 300, 'N', 8, 'REAL'),
        (33, 2, 5, 'PRESS', null, 500, 'N', 7, 'REAL'),
        (34, 2, 6, 'PRESS', null, 500, 'N', 5, 'REAL'),
        (35, 3, 1, 'PRESS', null, 500, 'N', 11, 'REAL'),
        (36, 3, 2, 'PRESS', null, 500, 'N', 12, 'REAL'),
        (37, 3, 3, 'PRESS', null, 500, 'N', 14, 'REAL'),
        (38, 3, 4, 'PRESS', null, 500, 'N', 13, 'REAL'),
        (39, 1, 1, 'OXY', null, null, 'N', 1, 'REAL'),
        (40, 1, 11, 'DPO', null, null, 'N', 1, 'SYM'),
        (41, 1, 11, 'DPO', null, null, 'N', 2, 'SYM'),
        (42, 1, 10, 'DPO', null, null, 'T', 3, 'REAL'),
        (43, 1, 7, 'VENT', null, null, 'N', 1, 'REAL'),
        (44, 1, 12, 'DIR', 90, null, 'N', 5, 'REAL'),
        (45, 1, 13, 'DIR', 45, null, 'N', 2, 'REAL'),
        (46, 1, 14, 'DIR', 180, null, 'N', 4, 'REAL'),
        (47, 1, 15, 'DIR', -45, null, 'N', 3, 'REAL'),
        (48, 1, 16, 'DIR', -90, null, 'N', 1, 'REAL');
