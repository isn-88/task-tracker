INSERT INTO groups (name)
VALUES ('administrator'),
       ('manager'),
       ('developer'),
       ('engineer'),
       ('tester');

INSERT INTO account (email, username, password, role)
VALUES ('admin@tracker.com', 'admin', '{noop}pass', 'ADMIN');
INSERT INTO profile (account_id, lastname, firstname, gender)
VALUES ((SELECT id FROM account WHERE username = 'admin'), 'Петров', 'Сергей', null);

INSERT INTO account (email, username, password, role)
VALUES ('dev@tracker.com', 'dev', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev'), 'Иванов', 'Михаил', 'Николаевич', 'MALE');

INSERT INTO account (email, username, password, role)
VALUES ('eng@tracker.com', 'engineer', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'engineer'), 'Смирнов', 'Иван', 'Петрович', 'MALE');

INSERT INTO account (email, username, password, role)
VALUES ('test@tracker.com', 'test', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, gender)
VALUES ((SELECT id FROM account WHERE username = 'test'), 'Иванова', 'Марина', 'FEMALE');

INSERT INTO account (email, username, password, role)
VALUES ('user@tracker.com', 'user', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname)
VALUES ((SELECT id FROM account WHERE username = 'user'), 'Сидоров', 'Семён', 'Валерьевич');

INSERT INTO account (email, username, password, role)
VALUES ('block@tracker.com', 'block', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname)
VALUES ((SELECT id FROM account WHERE username = 'block'), 'Ветров', 'Георгий', 'Петрович');

INSERT INTO account (email, username, password, role)
VALUES ('mngr@tracker.com', 'manager', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, gender)
VALUES ((SELECT id FROM account WHERE username = 'manager'), 'Васильева', 'Наталья', 'FEMALE');

INSERT INTO account (email, username, password, role)
VALUES ('dev-lead@tracker.com', 'dev-lead', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev-lead'), 'Синицын', 'Максим', 'Эдуардович', 'MALE');

INSERT INTO account (email, username, password, role)
VALUES ('dev-java@tracker.com', 'dev-java', '{noop}pass', 'USER');
INSERT INTO profile (account_id, lastname, firstname, surname, gender)
VALUES ((SELECT id FROM account WHERE username = 'dev-java'), 'Борисова', 'Ольга', 'Евгеньевна', 'FEMALE');

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

INSERT INTO project_groups(project_id, group_id)
VALUES ((SELECT id FROM project WHERE name = 'task-tracker'), (SELECT id FROM groups WHERE name = 'administrator')),
       ((SELECT id FROM project WHERE name = 'task-tracker'), (SELECT id FROM groups WHERE name = 'manager')),
       ((SELECT id FROM project WHERE name = 'task-tracker'), (SELECT id FROM groups WHERE name = 'developer')),
       ((SELECT id FROM project WHERE name = 'task-tracker'), (SELECT id FROM groups WHERE name = 'engineer')),
       ((SELECT id FROM project WHERE name = 'task-tracker'), (SELECT id FROM groups WHERE name = 'tester'));

INSERT INTO task (parent_id, project_id, owner_id, category_id, assigned_account_id, type, title, status, priority, description)
VALUES (null, (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'manager'),
        'FEATURE', 'Разработка сервиса TaskTracker','NEW', 'NORMAL',
        'Корневая задача по разработке сервиса отслеживания задач');

INSERT INTO task (parent_id, project_id, owner_id, category_id, assigned_account_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'dev'),
        'FEATURE', 'Создание maven проекта', 'NEW', 'NORMAL',
        'Реализовать двухмодульную структуру проекта: common и service, который зависит от common');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Схема БД', 'NEW', 'NORMAL',
        'Создать схему базы данных и основные сущности (без ассоциаций)');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Маппинг ассоциаций', 'NEW', 'NORMAL',
        'Добавить маппинг ассоциаций. Написать тесты на каждую сущность (H2 или Docker)');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Querydsl и Criteria', 'NEW', 'NORMAL',
        'Написать запрос filter через Querydsl и Criteria. Оптимизировать его с помощью EntityGraph');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Реализовать DAO', 'NEW', 'NORMAL',
        'Реализовать DAO для всех сущностей	');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Spring Beans', 'NEW', 'NORMAL',
        'Все объекты DAO должны быть Spring Beans. Никаких xml в коде. В тестах использовать Spring Context для получения DAO');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Spring Boot', 'NEW', 'NORMAL',
        'Создать Spring Boot приложение, настроить IT, добавить JPA Starter и удалить ненужные зависимости и бины (SessionFactory)');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Spring Repository', 'NEW', 'NORMAL',
        'Переписать DAO на Spring Repository. Добавить Liquibase');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Написать CRUD', 'NEW', 'NORMAL',
        'Spring Web. Написать CRUD операции для одной сущности Controller -> Service -> Repository + Thymeleaf');

INSERT INTO task (parent_id, project_id, owner_id, category_id, assigned_group_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM groups WHERE name = 'developer'),
        'FEATURE', 'REST Controller', 'NEW', 'NORMAL',
        'Добавить REST Controller на одну сущность. Протестировать через Swagger. Добавлять функционал для других сущностей (Controller)');

INSERT INTO task (parent_id, project_id, owner_id, category_id, assigned_account_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'dev-lead'),
        'FEATURE', 'Spring Security', 'NEW', 'NORMAL',
        'Spring Security. Добавить аутентификацию и авторизацию');

INSERT INTO task (parent_id, project_id, owner_id, category_id, assigned_account_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'dev-java'),
        'FEATURE', 'Интернационализация', 'NEW', 'NORMAL',
        'Добавить интернационализацию (как минимум для 1 страницы). Дополнить функционал проекта');

INSERT INTO task (parent_id, project_id, owner_id, category_id, type, title, status, priority, description)
VALUES ((SELECT id FROM task WHERE title = 'Разработка сервиса TaskTracker'),
        (SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        'FEATURE', 'Логирование', 'NEW', 'NORMAL',
        'Добавить логирование входных параметров и возвращаемого значения всех методов уровня service. Реализовать оставшийся функционал.');

INSERT INTO task (project_id, owner_id, category_id, assigned_account_id, type, title, status, priority, description)
VALUES ((SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM account WHERE username = 'admin'),
        'SUPPORT', 'Регистрация новой учётной записи', 'NEW', 'NORMAL',
        'Необходимо зарегистрировать учётную запись для нового сотрудника' ||E'\n'||
        'ФИО: Иванов Иван Иванович' ||E'\n'||
        'Имя пользователя: i.ivanov' ||E'\n'||
        'Email: i.ivanov@tracker.ru' ||E'\n'||E'\n'||
        'Добавить в группу: tester');

INSERT INTO task (project_id, owner_id, category_id, assigned_group_id, start_date, end_date, type, title, status, priority, description)
VALUES ((SELECT id FROM project where name = 'task-tracker'),
        (SELECT id FROM account WHERE username = 'manager'),
        (SELECT id FROM category WHERE name = 'general'),
        (SELECT id FROM groups WHERE name= 'administrator'),
        now(), now(),
        'SUPPORT', 'Заблокировать учётную запись', 'NEW', 'URGENT',
        'В связи с увольнением сотрудника необходимо заблокировать его учётную запись.'||E'\n'||
        'Имя пользователя: block');