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


Przykładowe ramki:
$PCARCC,230144,1,U,5,U,25,U*7a


cd /tmp/moxa/
./mxuninst
./mxinst SP1
./mxaddsvr IP_ADDRESS 32
