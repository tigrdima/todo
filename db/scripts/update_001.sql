create table if not exists items (
    id serial primary key,
    name text,
    description text,
    done bool,
    created text
);
