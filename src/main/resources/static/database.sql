-- DROP TABLE IF EXISTS public.user_actions;
-- DROP TABLE IF EXISTS public.actions;
-- DROP TABLE IF EXISTS public.users;
-- DROP TABLE IF EXISTS public.roles;

-- DROP SEQUENCE IF EXISTS public.user_actions_id_seq;
-- DROP SEQUENCE IF EXISTS public.actions_id_seq;
-- DROP SEQUENCE IF EXISTS public.users_id_seq;
-- DROP SEQUENCE IF EXISTS public.roles_id_seq;


-- SEQUENCE: public.roles_id_seq
CREATE SEQUENCE IF NOT EXISTS public.roles_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Table: public.roles
CREATE TABLE IF NOT EXISTS public.roles
(
    id integer NOT NULL DEFAULT nextval('roles_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
);


-- SEQUENCE: public.users_id_seq
CREATE SEQUENCE IF NOT EXISTS public.users_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Table: public.users
CREATE TABLE IF NOT EXISTS public.users
(
    id integer NOT NULL DEFAULT nextval('users_id_seq'::regclass),
    username character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password character varying(255) COLLATE pg_catalog."default" NOT NULL,
    role_id integer NOT NULL,
    deleted boolean NOT NULL DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_username_key UNIQUE (username),
    CONSTRAINT users_role_id_fkey FOREIGN KEY (role_id)
        REFERENCES public.roles (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


-- SEQUENCE: public.actions_id_seq
CREATE SEQUENCE IF NOT EXISTS public.actions_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Table: public.actions
CREATE TABLE IF NOT EXISTS public.actions
(
    id integer NOT NULL DEFAULT nextval('actions_id_seq'::regclass),
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT actions_pkey PRIMARY KEY (id)
);


-- SEQUENCE: public.user_actions_id_seq
CREATE SEQUENCE IF NOT EXISTS public.user_actions_id_seq
    INCREMENT 1
    START 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    CACHE 1;


-- Table: public.user_actions
CREATE TABLE IF NOT EXISTS public.user_actions
(
    id integer NOT NULL DEFAULT nextval('user_actions_id_seq'::regclass),
    user_id integer NOT NULL,
    action_id integer NOT NULL,
    date_time timestamp with time zone NOT NULL,
    CONSTRAINT user_actions_pkey PRIMARY KEY (id),
    CONSTRAINT user_actions_action_id_fkey FOREIGN KEY (action_id)
        REFERENCES public.actions (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT user_actions_user_id_fkey FOREIGN KEY (user_id)
        REFERENCES public.users (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);


-- Insert data
INSERT INTO roles VALUES (1, 'USER');
INSERT INTO roles VALUES (2, 'ADMIN');
INSERT INTO roles VALUES (3, 'GUEST');


INSERT INTO users VALUES (1, 'admin', '$2a$12$G9Tyz/oGx3ptSi6ZxZZrtuCZDj6wwTj5VYKfuYoHxec8MOqrQm.XS', 2, false);
INSERT INTO users VALUES (2, 'user',  '$2a$12$X.mZ.klfcVCM66Ge2fe.qeWj0wg6OF4s9r/o.yF7dRsBEJ/OhfgKm', 1, false);
INSERT INTO users VALUES (3, 'guest', '$2a$12$NDKyA3CTuW0YRvMfS00FReTAivzRw8efKjzUL59jb.SmotP.XsY.S', 3, false);


INSERT INTO actions VALUES (1, 'connect');
INSERT INTO actions VALUES (2, 'disconnect');
INSERT INTO actions VALUES (3, 'start');
INSERT INTO actions VALUES (4, 'stop');