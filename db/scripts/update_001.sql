create table if not exists users (
    id serial primary key,
    name text,
    email text,
    password text,
    constraint email_unique unique (email)
);

create table if not exists items (
    id serial primary key,
    name text,
    description text,
    done bool,
    author text,
    created text,
    user_id int not null references users(id)
);