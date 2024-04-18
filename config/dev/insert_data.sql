INSERT INTO groups (name)
VALUES ('administrators'),
       ('developers'),
       ('engineers'),
       ('testers');

INSERT INTO account (group_id, email, username, password, role)
VALUES ((SELECT id FROM groups WHERE name = 'administrator'),
        'admin@email.com', 'admin', '{noop}pass', 'ADMIN');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'admin'),
        'Администратор', 'Пользователь', 'Проверочный', null);

INSERT INTO account (group_id, email, username, password, role)
VALUES ((SELECT id FROM groups WHERE name = 'developer'),
        'dev@email.com', 'dev', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev'),
        'Разработчик', 'Пользователь', 'Проверочный', 'MALE');

INSERT INTO account (group_id, email, username, password, role)
VALUES ((SELECT id FROM groups WHERE name = 'engineers'),
        'eng@email.com', 'eng', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'eng'),
        'Инженер', 'Пользователь', 'Проверочный', 'MALE');

INSERT INTO account (group_id, email, username, password, role)
VALUES ((SELECT id FROM groups WHERE name = 'testers'),
        'test@email.com', 'test', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'test'),
        'Тестировщик', 'Пользователь', 'Проверочный', 'FEMALE');


INSERT INTO category (name)
VALUES ('general');

INSERT INTO project (name, description)
VALUES ('task-tracker', 'Разработка сервиса по постановке и отслеживанию задач'),
       ('subscription', 'Сервис подписок'),
       ('auth-service', 'Сервис аутентификации');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, assigned_group_id, created_at, progress, description)
VALUES (null, (SELECT id FROM project where name = 'task-tracker'), 'FEATURE', 'Разработка сервиса TaskTracker','NEW', 'NORMAL',
        (SELECT id FROM category WHERE name = 'general'), (SELECT id FROM groups WHERE name = 'developers'), now(), 0,
        'Корневая задача по разработке сервиса отслеживания задач');


INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, assigned_account_id, created_at, progress, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Создание проекта', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'dev'), now(), 0, 'Создать SpringBoot приложение');

INSERT INTO task (parent_id, project_id, type, title, status, priority, category_id, created_at, progress, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'), (SELECT id FROM project where name = 'task-tracker'),
        'FEATURE', 'Добавить репозиторий', 'NEW', 'NORMAL', (SELECT id FROM category WHERE name = 'general'),
        now(), 0, 'Добавить UserRepository');





