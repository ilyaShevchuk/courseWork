CREATE sequence hibernate_sequence start with 1 increment by 1;

CREATE TABLE course
(
    id          BIGINT NOT NULL,
    category    INTEGER,
    description VARCHAR(255),
    name        VARCHAR(255),
    members     INTEGER,
    teacher_id  BIGINT,
    type        INTEGER,
    PRIMARY KEY (id)
);
CREATE TABLE course_lessons
(
    course_id  BIGINT NOT NULL,
    lessons_id BIGINT NOT NULL
);
CREATE TABLE lesson
(
    id          BIGINT NOT NULL,
    description VARCHAR(255),
    extra_info  VARCHAR(255),
    name        VARCHAR(255),
    time        TIMESTAMP,
    type        INTEGER,
    teacher_id  BIGINT,
    PRIMARY KEY (id)
);

CREATE INDEX teacher_index ON lesson (teacher_id);


CREATE TABLE teacher
(
    id       BIGINT NOT NULL,
    birthday date,
    name     VARCHAR(255),
    login    VARCHAR(255),
    password VARCHAR(255),
    role     INTEGER,
    PRIMARY KEY (id)
);


alter TABLE course_lessons
    add constraint UK_jmeskn382gm3c0ylx0p1bbkav unique (lessons_id);
alter TABLE course
    add constraint FKsybhlxoejr4j3teomm5u2bx1n foreign key (teacher_id) references teacher;
alter TABLE course_lessons
    add constraint FKngptislimkt60qpmpgjqdbyab foreign key (lessons_id) references lesson;
alter TABLE course_lessons
    add constraint FKpnblexnvun0cik2alnhd0to49 foreign key (course_id) references course;
alter TABLE lesson
    add constraint FK9yhaoqrjxt5gwmn6icp1lf35n foreign key (teacher_id) references teacher;

INSERT INTO teacher
VALUES (0, '2000-12-12', 'Big Q', 'admin', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0);
-- INSERT INTO teacher
-- VALUES (1, '2000-12-12', 'Small q', 'user', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 1);
