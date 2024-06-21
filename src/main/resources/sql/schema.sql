DROP TABLE IF EXISTS public.user CASCADE;
DROP TABLE IF EXISTS public.role CASCADE;
DROP TABLE IF EXISTS public.product CASCADE;
DROP TABLE IF EXISTS public.user_product CASCADE;

create table if not exists public.role
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

create table if not exists public.user
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    firstName VARCHAR(255) NOT NULL,
    lastName VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL REFERENCES role (id)
);

create table if not exists public.product
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    quantity BIGINT NOT NULL,
    number_of_sold BIGINT NOT NULL,
    price DOUBLE PRECISION NOT NULL
);

create table if not exists public.user_product
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES public.user (id),
    product_id BIGINT NOT NULL REFERENCES public.product (id)
);
