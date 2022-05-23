package com.tinkoff.timetable.controller;

import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.repository.TeacherRepository;
import com.tinkoff.timetable.response.ErrorResponse;
import com.tinkoff.timetable.response.LessonResponse;
import com.tinkoff.timetable.response.TeacherResponse;
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

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TeacherControllerTest extends AbstractControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mvc;
    @Autowired
    private TeacherRepository teacherRepository;
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
    void createTeacherSuccessful() throws Exception {
        long id = 1L;
        String uri = "/teacher/creation";
        teacherDto.setId(null);
        String inputJson = mapToJson(teacherDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(2, hibernateQueryInterceptor.getQueryCount());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new TeacherResponse(id, TEACHER_NAME), mapFromJson(content, TeacherResponse.class));
        assertEquals(TEACHER_NAME, teacherRepository.getById(id).getName());
    }

    @Test
    @WithUserDetails("admin")
    void createTeacherError() throws Exception {
        String uri = "/teacher/creation";
        TeacherDto teacherWithNullName = teacherDto;
        teacherWithNullName.setName(null);
        String inputJson = mapToJson(teacherWithNullName);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (1, '2000-12-12', 'Big Gogi', 'login4', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    void getTeacherSuccessful() throws Exception {
        long id = 1L;
        String uri = "/teacher/" + id;
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(1, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        teacherDto.setId(id);
        assertEquals(new TeacherResponse(teacherDto.getId(), teacherDto.getName()),
                mapFromJson(content, TeacherResponse.class));
    }

    @Test
    @WithUserDetails("admin")
    void getTeacherNotFound() throws Exception {
        long id = 50L;
        String uri = "/teacher/" + id;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new ErrorResponse("Teacher with id=50 not found"), mapFromJson(content, ErrorResponse.class));
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (1, '2000-12-12', 'Big Gogi', 'login4', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    void updateSuccessful() throws Exception {
        long id = 1L;
        String uri = "/teacher/" + id;
        String inputJson = mapToJson(teacherDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(1, hibernateQueryInterceptor.getQueryCount());
        assertEquals(201, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new TeacherResponse(id, TEACHER_NAME), mapFromJson(content, TeacherResponse.class));
        assertEquals(TEACHER_NAME, teacherRepository.getById(id).getName());
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (1, '2000-12-12', 'Big Gogi', 'login4', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    void delete() throws Exception {
        long id = 1L;
        String uri = "/teacher/" + id;
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri)).andReturn();
        assertEquals(2, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(teacherRepository.findById(id).isEmpty());
    }

    @Test
    void deleteWithOutPermission() throws Exception {
        teacherService.registerTeacher(registrationRequest);
        long id = 1L;
        String uri = "/teacher/" + id;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri)
                .header(HttpHeaders.AUTHORIZATION, authUser))
                .andReturn();
        assertEquals(403, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES " +
            "(1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)," +
            "(2, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T10:00', 0, 0)")
    void getSchedule() throws Exception {
        long id = 0L;
        String uri = "/teacher/" + id + "/schedule";
        String inputJson = mapToJson(teacherDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(2, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        List<LessonResponse> lessons = Arrays.stream(mapFromJson(content, LessonResponse[].class)).toList();
        assertEquals(10, lessons.get(0).getTime().getHour());
        assertEquals(12, lessons.get(1).getTime().getHour());
    }


}
