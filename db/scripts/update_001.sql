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

create table if not exists categories (
    id serial primary key,
    name varchar,
    constraint name_unique unique (name)
);

create table if not exists items_categories (
    items_id int not null references items(id),
    category_id int not null references categories(id)
);

