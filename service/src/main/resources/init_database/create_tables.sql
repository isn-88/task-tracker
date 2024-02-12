
CREATE TABLE type
(
    id smallserial PRIMARY KEY ,
    type varchar(16) NOT NULL UNIQUE
);

CREATE TABLE status
(
    id smallserial PRIMARY KEY ,
    status varchar(32) NOT NULL UNIQUE
);

CREATE TABLE priority
(
    id smallserial PRIMARY KEY ,
    priority varchar(16) NOT NULL UNIQUE
);

CREATE TABLE category
(
    id serial PRIMARY KEY ,
    name varchar(64) NOT NULL ,
    CONSTRAINT check_name_con CHECK ( name <> '' )
);

CREATE TABLE user_group
(
    id uuid PRIMARY KEY ,
    name varchar(64) NOT NULL UNIQUE
);

CREATE TABLE account
(
    id uuid PRIMARY KEY ,
    group_id uuid ,
    email varchar(128) NOT NULL UNIQUE ,
    login varchar(128) NOT NULL UNIQUE ,
    password varchar(64) NOT NULL ,
    role varchar(8) NOT NULL ,
    CONSTRAINT check_role_con CHECK ( role IN ('USER', 'ADMIN') )
);

CREATE TABLE person
(
    id uuid PRIMARY KEY ,
    first_name varchar(32) ,
    last_name varchar(32) ,
    middle_name varchar(32) ,
    gender varchar(6),
    CONSTRAINT check_gender CHECK ( gender IN ('MALE', 'FEMALE') )
);

CREATE TABLE project
(
    id uuid PRIMARY KEY ,
    name varchar(128) NOT NULL UNIQUE ,
    description text
);

CREATE TABLE task
(
    id bigserial PRIMARY KEY ,
    parent_id bigint ,
    project_id uuid NOT NULL ,
    type_id smallint NOT NULL ,
    title varchar(128) NOT NULL ,
    status_id smallint NOT NULL ,
    priority_id smallint NOT NULL ,
    assigned uuid ,
    category_id int ,
    create_date DATE NOT NULL DEFAULT now() ,
    end_date DATE ,
    progress smallint NOT NULL DEFAULT 0 ,
    description text ,
    CONSTRAINT check_title_con CHECK ( task.title <> '' )
)


