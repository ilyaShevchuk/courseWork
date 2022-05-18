package com.tinkoff.timetable.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tinkoff.timetable.TimetableApplicationTests;
import com.tinkoff.timetable.model.CourseCategory;
import com.tinkoff.timetable.model.FormatType;
import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.model.request.RegistrationRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

public class AbstractControllerTest extends TimetableApplicationTests {
    protected String COURSE_NAME = "Algebra 7 class gdz";
    protected String COURSE_DESC = "desc";
    protected CourseCategory COURSE_CATEGORY = CourseCategory.ALGEBRA;
    protected FormatType COURSE_TYPE = FormatType.OFFLINE;

    protected Long TEACHER_ID = 0L;
    protected String TEACHER_NAME = "Big Gogi";
    protected String TEACHER_BIRTHDAY = "2000-12-12";

    protected String LESSON_NAME = "lesson";
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
            .teacherId(TEACHER_ID)
            .build();
    protected CourseDto courseDto = CourseDto.builder()
            .id(TEACHER_ID)
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

    protected String authAdmin = loginPasswordEncoder("admin:admin");
    protected String authUser = loginPasswordEncoder("user321:password");

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(json, clazz);
    }

    protected String loginPasswordEncoder(String loginPass) {
        return "Basic " + Base64.getEncoder().encodeToString(loginPass.getBytes(StandardCharsets.UTF_8));
    }
}
