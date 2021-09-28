create table engine
(
    id            serial primary key,
    name varchar(100) not null unique
);

create table car
(
    id        serial primary key,
    name      varchar(100) not null,
    engine_id int          not null references engine (id)
);

create table driver
(
    id         serial primary key,
    first_name varchar(100) not null,
    last_name  varchar(100) not null
);

create table history_owner
(
    driver_id int not null references driver (id),
    car_id    int not null references car (id),
    primary key (driver_id, car_id)
);
