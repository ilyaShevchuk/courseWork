**Выпускной курсовой проект Тинькофф.Финтех, специальность Javа-разработчик, весна 2022**

_TimeTable API_

1) Миграции с помощью flyway
2) Деплой на Heroku
   https://tinkoff-timetable.herokuapp.com/
3) gitlab.ci 
4) swagger
5) Покрытие тестами
6) Интеграционные тесты с подсчетом запросов

**Задание на курсовую работу:**

Тренд нового десятилетия - онлайн образование. Сотрудники Тинькофф участвуют во множестве разных программ и курсов.
Необходимо спроектировать и реализовать инструмент для создания курса, составления расписания и трекинга активности преподавателей.

 1) Спроектировать модель данных:
Курса:
 У курса обязательно должны быть:
    - название
   - описание
      - категория (например технические, которые включают в себя языки программирования, технологии и инструменты), сделать иерархию категорий на основе наследования
      - куратор (ссылка на преподавателя - куратора курса)
      - количество студентов
      - список занятий
   - тип (например онлайн или оффлайн, у каждого свой набор полей, у онлайна может быть ссылка на платформу проведения, у оффлайн название вуза и адрес)

 Остальные поля на ваше усмотрение.

Преподавателя:
    - фио
   - возраст

Занятия
   - название
   - описание
   - преподаватель
   - дата и время проведения

2) Реализовать API для управление моделью (CRUD), необходимые валидации и исключения. Необходимо иметь возможность создать курс, добавить в него занятия, назначить/поменять на каждое занятие преподавателя.
 
3) Добавляем ролевую модель. Каждый преподаватель получает роль ROLE_TEACHER при регистрации. Он может создавать курсы и занятия, редактировать и добавлять занятия ТОЛЬКО к своим курсам. Добавить пользователя с ролью ROLE_ADMIN, который может редактировать все курсы/занятия.

 4) Реализовать API для:
- создания курса копированием другого курса, при этом куратором назначается тот, кто копирует курс. Список занятий тоже копируется, но дата и время зануляются.
- получение расписания каждого преподавателя

Преподаватель: Туваев Андрей
