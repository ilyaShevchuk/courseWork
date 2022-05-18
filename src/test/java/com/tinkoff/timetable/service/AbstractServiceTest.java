package com.tinkoff.timetable.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff.timetable.TimetableApplicationTests;
import com.tinkoff.timetable.model.CourseCategory;
import com.tinkoff.timetable.model.FormatType;
import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.model.request.RegistrationRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AbstractServiceTest extends TimetableApplicationTests {
    protected String COURSE_NAME = "Математика для самых глупеньких";
    protected String COURSE_DESC = "Default course description";
    protected CourseCategory COURSE_CATEGORY = CourseCategory.ALGEBRA;
    protected FormatType COURSE_TYPE = FormatType.OFFLINE;

    protected Long TEACHER_ID = 1L;
    protected String TEACHER_NAME = "Kirill Hudyakov";
    protected String TEACHER_BIRTHDAY = "2000-05-05";

    protected String LESSON_NAME = "Урок матеши";
    protected String LESSON_TIME = "2022-05-25T12:00";
    protected FormatType LESSON_TYPE = FormatType.OFFLINE;
    protected String LESSON_EXTRA_INFO = "ауд. 2304, Кронверкский пр., д.49";
    protected Long LESSON_TEACHER_ID = 0L;

    protected LessonDto lessonDto = LessonDto.builder()
            .name(LESSON_NAME)
            .time(LocalDateTime.parse(LESSON_TIME))
            .description("Random")
            .extraInfo(LESSON_EXTRA_INFO)
            .type(LESSON_TYPE)
            .build();
    protected CourseDto courseDto = CourseDto.builder()
            .name(COURSE_NAME)
            .description(COURSE_DESC)
            .category(COURSE_CATEGORY)
            .type(COURSE_TYPE)
            .studentsCount(0)
            .teacherId(LESSON_TEACHER_ID)
            .build();

    protected TeacherDto teacherDto = TeacherDto.builder()
            .name(TEACHER_NAME)
            .birthday(LocalDate.parse(TEACHER_BIRTHDAY))
            .build();

    protected RegistrationRequest registrationRequest = new RegistrationRequest("user321", "password", teacherDto);


}
