package com.tinkoff.timetable.controller;


import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.repository.LessonRepository;
import com.tinkoff.timetable.response.ErrorResponse;
import com.tinkoff.timetable.response.LessonResponse;
import com.tinkoff.timetable.service.LessonService;
import com.tinkoff.timetable.service.TeacherService;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class LessonControllerTest extends AbstractControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private HibernateQueryInterceptor hibernateQueryInterceptor;


    @BeforeEach
    void setUp() {
        mvc = mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithUserDetails("admin")
    void createLessonSuccessful() throws Exception {
        long id = 1L;
        String uri = "/lesson/creation";
        lessonDto.setId(null);
        String inputJson = mapToJson(lessonDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(2, hibernateQueryInterceptor.getQueryCount());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new LessonResponse(id, LESSON_NAME, LESSON_TYPE.name(), LocalDateTime.parse(LESSON_TIME), LESSON_TEACHER_ID),
                mapFromJson(content, LessonResponse.class));
        assertEquals(lessonRepository.getById(id).getName(), LESSON_NAME);
    }

    @Test
    @WithUserDetails("admin")
    void createLessonError() throws Exception {
        String uri = "/lesson/creation";
        LessonDto lessonWithNullName = lessonDto;
        lessonWithNullName.setName(null);
        String inputJson = mapToJson(lessonWithNullName);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 1, 0)")
    void getLessonSuccessful() throws Exception {
        long id = 1L;
        String uri = "/lesson/" + id;
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(1, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        lessonDto.setId(id);
        assertEquals(new LessonResponse(id, LESSON_NAME, LESSON_TYPE.name(), LocalDateTime.parse(LESSON_TIME), LESSON_TEACHER_ID),
                mapFromJson(content, LessonResponse.class));
    }

    @Test
    @WithUserDetails("admin")
    void getLessonNotFound() throws Exception {
        long id = 50L;
        String uri = "/lesson/" + id;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new ErrorResponse("Lesson with id=50 not found"), mapFromJson(content, ErrorResponse.class));
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 1, 0)")
    void updateSuccessful() throws Exception {
        long id = 1L;
        String uri = "/lesson/" + id;
        String inputJson = mapToJson(lessonDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(3, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new LessonResponse(id, LESSON_NAME, LESSON_TYPE.name(), LocalDateTime.parse(LESSON_TIME), LESSON_TEACHER_ID),
                mapFromJson(content, LessonResponse.class));
        assertEquals(lessonRepository.getById(id).getName(), LESSON_NAME);
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    void delete() throws Exception {
        long id = 1L;
        String uri = "/lesson/" + id;
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri)).andReturn();
        assertEquals(2, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(lessonRepository.findById(id).isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    void deleteWithOutPermission() throws Exception {
        teacherService.registerTeacher(registrationRequest);
        long id = 1L;
        String uri = "/lesson/" + id;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .header(HttpHeaders.AUTHORIZATION, authUser))
                .andReturn();
        assertEquals(401, mvcResult.getResponse().getStatus());
    }

}