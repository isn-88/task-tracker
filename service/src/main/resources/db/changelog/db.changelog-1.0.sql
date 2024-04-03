--liquibase formatted sql

--changeset s.ivaschenko:1
CREATE TABLE category
(
    id SMALLSERIAL PRIMARY KEY ,
    name VARCHAR(64) NOT NULL UNIQUE ,
    CONSTRAINT check_name_con CHECK ( name <> '' )
);
--rollback DROP TABLE category;

--changeset s.ivaschenko:2
CREATE TABLE groups
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(64) NOT NULL UNIQUE
);
--rollback DROP TABLE groups;

--changeset s.ivaschenko:3
CREATE TABLE account
(
    id BIGSERIAL PRIMARY KEY ,
    group_id INT REFERENCES groups (id) ,
    email VARCHAR(128) NOT NULL UNIQUE ,
    login VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(64) NOT NULL ,
    role VARCHAR(8) NOT NULL ,
    CONSTRAINT check_role_con CHECK ( role IN ('USER', 'ADMIN') )
);
--rollback DROP TABLE account;

--changeset s.ivaschenko:4
CREATE TABLE profile
(
    account_id BIGINT PRIMARY KEY REFERENCES account (id),
    lastname VARCHAR(32) NOT NULL ,
    firstname VARCHAR(32) NOT NULL ,
    surname VARCHAR(32) ,
    gender VARCHAR(6) ,
    CONSTRAINT check_gender_con CHECK ( gender IN ('MALE', 'FEMALE') )
);
--rollback DROP TABLE profile;

--changeset s.ivaschenko:5
CREATE TABLE project
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(128) NOT NULL UNIQUE ,
    description text
);
--rollback DROP TABLE project;

--changeset s.ivaschenko:6
CREATE TABLE task
(
    id BIGSERIAL PRIMARY KEY ,
    parent_id BIGINT REFERENCES task (id) ,
    project_id INT NOT NULL REFERENCES project (id) ,
    category_id SMALLINT REFERENCES category (id) ,
    type VARCHAR(32) NOT NULL ,
    status VARCHAR(32) NOT NULL ,
    priority VARCHAR(32) NOT NULL ,
    assigned_account_id BIGINT REFERENCES account (id) ,
    assigned_group_id INT REFERENCES groups (id) ,
    create_at TIMESTAMP WITH TIME ZONE NOT NULL ,
    close_at TIMESTAMP WITH TIME ZONE ,
    progress SMALLINT NOT NULL DEFAULT 0,
    title VARCHAR(64) NOT NULL ,
    description TEXT ,
    CONSTRAINT check_title_con CHECK ( task.title <> '' ) ,
    CONSTRAINT only_once_con CHECK ( NOT (assigned_account_id NOTNULL AND assigned_group_id NOTNULL) )
);
--rollback DROP TABLE task;

