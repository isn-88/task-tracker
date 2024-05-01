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
    name VARCHAR(64) NOT NULL UNIQUE ,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);
--rollback DROP TABLE groups;

--changeset s.ivaschenko:3
CREATE TABLE account
(
    id BIGSERIAL PRIMARY KEY ,
    email VARCHAR(128) NOT NULL UNIQUE ,
    username VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(256) NOT NULL ,
    role VARCHAR(8) NOT NULL ,
    enabled BOOLEAN NOT NULL DEFAULT true ,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now() ,
    last_login_at TIMESTAMP WITH TIME ZONE ,
    CONSTRAINT check_role_con CHECK ( role IN ('USER', 'ADMIN') )
);
--rollback DROP TABLE account;

--changeset s.ivaschenko:4
CREATE TABLE groups_account
(
    id BIGSERIAL PRIMARY KEY ,
    group_id INT NOT NULL REFERENCES groups (id) ,
    account_id BIGINT NOT NULL REFERENCES account (id) ,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    created_by VARCHAR(256) ,
    CONSTRAINT group_account_unq UNIQUE (group_id, account_id)
);
--rollback DROP TABLE groups_account;

--changeset s.ivaschenko:5
CREATE TABLE profile
(
    account_id BIGINT PRIMARY KEY REFERENCES account (id),
    lastname VARCHAR(32) NOT NULL ,
    firstname VARCHAR(32) NOT NULL ,
    surname VARCHAR(32) ,
    gender VARCHAR(6) ,
    about_me TEXT ,
    CONSTRAINT check_lastname_con CHECK ( lastname <> '' ),
    CONSTRAINT check_firstname_con CHECK ( firstname <> '' ),
    CONSTRAINT check_gender_con CHECK ( gender IN ('MALE', 'FEMALE') )
);
--rollback DROP TABLE profile;

--changeset s.ivaschenko:6
CREATE TABLE project
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(128) NOT NULL UNIQUE ,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now() ,
    description text
);
--rollback DROP TABLE project;

--changeset s.ivaschenko:7
CREATE TABLE task
(
    id BIGSERIAL PRIMARY KEY ,
    parent_id BIGINT REFERENCES task (id) ,
    project_id INT NOT NULL REFERENCES project (id) ,
    owner_id BIGINT NOT NULL REFERENCES account (id) ,
    category_id SMALLINT REFERENCES category (id) ,
    assigned_account_id BIGINT REFERENCES account (id) ,
    assigned_group_id INT REFERENCES groups (id) ,
    type VARCHAR(32) NOT NULL ,
    status VARCHAR(32) NOT NULL ,
    priority VARCHAR(32) NOT NULL ,
    start_date DATE ,
    end_date DATE ,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now() ,
    created_by VARCHAR(256) ,
    modified_at TIMESTAMP WITH TIME ZONE ,
    modified_by VARCHAR(256) ,
    close_at TIMESTAMP WITH TIME ZONE ,
    progress SMALLINT NOT NULL DEFAULT 0 ,
    title VARCHAR(64) NOT NULL ,
    description TEXT ,
    CONSTRAINT check_title_con CHECK ( task.title <> '' ) ,
    CONSTRAINT only_once_con CHECK ( NOT (assigned_account_id NOTNULL AND assigned_group_id NOTNULL) )
);
--rollback DROP TABLE project_groups;

--changeset s.ivaschenko:8
CREATE TABLE project_groups
(
    id BIGSERIAL PRIMARY KEY ,
    project_id INT NOT NULL REFERENCES project (id) ,
    group_id INT NOT NULL REFERENCES groups (id) ,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    created_by VARCHAR(256) ,
    CONSTRAINT project_group_unq UNIQUE (project_id, group_id)
);
--rollback DROP TABLE task;

--changeset s.ivaschenko:9
CREATE TABLE task_revision
(
    id SERIAL PRIMARY KEY ,
    timestamp BIGINT
);
--rollback DROP TABLE task_revision;

--changeset s.ivaschenko:10
CREATE TABLE task_aud
(
    id BIGINT NOT NULL ,
    rev INT NOT NULL ,
    revtype SMALLINT ,
    parent_id BIGINT ,
    project_id INT ,
    owner_id BIGINT ,
    category_id SMALLINT ,
    assigned_account_id BIGINT ,
    assigned_group_id INT ,
    type VARCHAR(32) ,
    status VARCHAR(32) ,
    priority VARCHAR(32) ,
    start_date DATE ,
    end_date DATE ,
    created_at TIMESTAMP WITH TIME ZONE ,
    created_by VARCHAR(256) ,
    modified_at TIMESTAMP WITH TIME ZONE ,
    modified_by VARCHAR(256) ,
    close_at TIMESTAMP WITH TIME ZONE ,
    progress SMALLINT ,
    title VARCHAR(64) ,
    description TEXT ,
    PRIMARY KEY (id, rev)
);
--rollback DROP TABLE task_aud;