/****************************************************/
/* Creacion de BD                                   */
/****************************************************/

SET GLOBAL time_zone = '-3:00';
drop database utnphone;
create database utnphone;
use utnphone;

create table users
(
iduser int auto_increment not null,
user varchar(50) not null,
password varchar(256) not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,
primary key (iduser)
)Engine = InnoDB;


create table Roles
(
id int auto_increment not null,
rol varchar(50),
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,
primary key (id)
)Engine = InnoDB;


create table roles_users
(

rolid int not null,
iduser int not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,
primary key (rolid, iduser),
foreign key (rolid) references roles (id),
foreign key (iduser) references users(iduser)
)Engine=InnoDB;



create table provinces
(
id int auto_increment not null,
province varchar(50) not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,
primary key (id)

) Engine = InnoDB;

create table cities
(
id int auto_increment not null,
idprovince int not null,
city varchar(100) not null,
prefix int not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,
primary key (id),
foreign key (idprovince) references provinces (id)

)Engine = InnoDB;


create table customers
(
id int auto_increment not null,
name varchar(50) not null,
lastname varchar(50) not null,
dni varchar(10) not null,
idcity int not null,
iduser int not null,

create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,

primary key (id),
foreign key (idcity) references cities (id),
foreign key (iduser) references users(iduser)
)Engine = InnoDB;


create table lineTypes
(
idtype int auto_increment not null,
type varchar(50) not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,

primary key (idtype)
)Engine = InnoDB;

create table lines_customers
(
idline int auto_increment not null,
linenumber varchar(50) not null,
idtype int not null,
idcustomer int not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,

primary key (idline),
foreign key (idtype) references lineTypes (idtype),
foreign key (idcustomer) references customers (id)

)Engine=InnoDB;

create table rates
(
idrate int auto_increment not null,
type varchar(50) not null,
pricexmin int not null,

create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,

primary key (idrate)

)Engine = InnoDB;

create table invoices
(
idinvoice int auto_increment not null,
idcustomer int not null,
totalcalls int not null,
total int not null,
paymentdate date not null,
expiration date not null,
state tinyint not null,
create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,

primary key (idinvoice),
foreign key (idcustomer) references customers (id)

)Engine=InnoDB;

create table calls
(
idcall int auto_increment not null,
origincall int not null,
destinationcall int not null,
origincity int not null,
destinationcity int not null,
durationtime time not null,
price int not null,
total int not null,
idinvocice int,
idrate int not null,

create_at timestamp default current_timestamp,
update_at timestamp default current_timestamp,

primary key (idcall),

foreign key (idinvocice) references  invoices (idinvoice),
foreign key (idrate) references rates (idrate),
foreign key (origincall) references  lines_customers (idline),
foreign key (destinationcall) references  lines_customers (idline),
foreign key (origincity) references cities (id),
foreign key (destinationcity) references cities (id)

)Engine = InnoDB;
