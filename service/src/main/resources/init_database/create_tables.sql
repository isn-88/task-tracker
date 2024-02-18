CREATE TABLE category
(
    id SERIAL PRIMARY KEY ,
    name VARCHAR(64) NOT NULL UNIQUE ,
    CONSTRAINT check_name_con CHECK ( name <> '' )
);

CREATE TABLE groups
(
    id UUID PRIMARY KEY ,
    name VARCHAR(64) NOT NULL UNIQUE
);

CREATE TABLE account
(
    id UUID PRIMARY KEY ,
    group_id UUID REFERENCES groups (id),
    email VARCHAR(128) NOT NULL UNIQUE ,
    login VARCHAR(128) NOT NULL UNIQUE ,
    password VARCHAR(64) NOT NULL ,
    role VARCHAR(8) NOT NULL ,
    CONSTRAINT check_role_con CHECK ( role IN ('USER', 'ADMIN') )
);

CREATE TABLE person
(
    id UUID PRIMARY KEY ,
    lastname VARCHAR(32) NOT NULL ,
    firstname VARCHAR(32) NOT NULL ,
    surname VARCHAR(32) ,
    gender VARCHAR(6) ,
    CONSTRAINT check_gender_con CHECK ( gender IN ('MALE', 'FEMALE') )
);

CREATE TABLE project
(
    id UUID PRIMARY KEY ,
    name VARCHAR(128) NOT NULL UNIQUE ,
    description text
);

CREATE TABLE task
(
    id BIGSERIAL PRIMARY KEY ,
    parent_id BIGINT REFERENCES task (id) ,
    project_id UUID NOT NULL REFERENCES project (id) ,
    type SMALLINT NOT NULL ,
    title VARCHAR(32) NOT NULL ,
    status VARCHAR(32) NOT NULL ,
    priority VARCHAR(32) NOT NULL ,
    assigned UUID REFERENCES account (id),
    category_id INT REFERENCES category (id),
    create_date DATE NOT NULL DEFAULT now() ,
    end_date DATE ,
    progress SMALLINT NOT NULL DEFAULT 0 ,
    description TEXT ,
    CONSTRAINT check_title_con CHECK ( task.title <> '' )
)


