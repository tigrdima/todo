create table if not exists users (
    id serial primary key,
    name varchar,
    email varchar,
    password varchar,
    constraint email_unique unique (email)
);

create table if not exists items (
    id serial primary key,
    name varchar,
    description varchar,
    done bool,
    author varchar,
    created text,
    user_id int not null references users(id)
);