INSERT INTO groups (name)
VALUES ('administrator'),
       ('manager'),
       ('developer'),
       ('engineer'),
       ('tester');

INSERT INTO account (email, username, password, role)
VALUES ('admin@email.com', 'admin', '{noop}pass', 'ADMIN');
INSERT INTO profile (account_id, lastname, firstname, gender)
VALUES ((SELECT id FROM account WHERE username = 'admin'), 'Петров', 'Сергей', null);

INSERT INTO account (email, username, password, role)
VALUES ('dev@email.com', 'dev', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev'), 'Иванов', 'Михаил', 'Николаевич', 'MALE');

INSERT INTO account (email, username, password, role)
VALUES ('eng@email.com', 'engineer', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'engineer'), 'Смирнов', 'Иван', 'Иванович', 'MALE');

INSERT INTO account (email, username, password, role)
VALUES ('test@email.com', 'test', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, gender)
VALUES ((SELECT id FROM account WHERE username = 'test'), 'Иванова', 'Марина', 'FEMALE');

INSERT INTO account (email, username, password, role)
VALUES ('user@email.com', 'user', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname)
VALUES ((SELECT id FROM account WHERE username = 'user'), 'Сидоров', 'Семён', 'Валерьевич');

INSERT INTO account (email, username, password, role)
VALUES ('user2@email.com', 'user2', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname)
VALUES ((SELECT id FROM account WHERE username = 'user2'), 'Ветров', 'Георгий', 'Петрович');

INSERT INTO account (email, username, password, role)
VALUES ('mngr@email.com', 'manager', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, gender)
VALUES ((SELECT id FROM account WHERE username = 'manager'), 'Васильева', 'Наталья', 'FEMALE');

INSERT INTO account (email, username, password, role)
VALUES ('dev2@email.com', 'dev2', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev2'), 'Синицын', 'Максим', 'Эдуардович', 'MALE');

INSERT INTO account (email, username, password, role)
VALUES ('dev3@email.com', 'dev3', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev3'), 'Борисова', 'Ольга', 'Евгеньевна', 'FEMALE');

INSERT INTO groups_account (group_id, account_id)
VALUES ((SELECT id FROM groups WHERE name = 'administrator'), (SELECT id FROM account WHERE username = 'admin')),
       ((SELECT id FROM groups WHERE name = 'manager'), (SELECT id FROM account WHERE username = 'manager')),
       ((SELECT id FROM groups WHERE name = 'developer'), (SELECT id FROM account WHERE username = 'dev')),
       ((SELECT id FROM groups WHERE name = 'engineer'), (SELECT id FROM account WHERE username = 'engineer')),
       ((SELECT id FROM groups WHERE name = 'tester'), (SELECT id FROM account WHERE username = 'test'));

INSERT INTO category (name)
VALUES ('general');

INSERT INTO project (name, description)
VALUES ('task-tracker', 'Разработка сервиса по постановке и отслеживанию задач'),
       ('subscription', 'Сервис подписок'),
       ('auth-service', 'Сервис аутентификации');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, assigned_group_id, description)
VALUES (null, (SELECT id FROM project where name = 'task-tracker'), 'FEATURE', 'Разработка сервиса TaskTracker','NEW', 'NORMAL',
        (SELECT id FROM category WHERE name = 'general'), (SELECT id FROM groups WHERE name = 'developers'),
        'Корневая задача по разработке сервиса отслеживания задач');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, assigned_account_id, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Создание проекта', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'dev'), 'Создать SpringBoot приложение');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Добавить репозиторий', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        'Добавить AccountRepository');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Добавить сервис', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        'Добавить AccountService');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Добавить контроллер', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        'Добавить AccountController');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Добавить безопасность', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        'Добавить и сконфигурировать Spring Security');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Добавить тесты', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        'Написать модульные и интеграционные тесты');
