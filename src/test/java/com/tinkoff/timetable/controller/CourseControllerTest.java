package com.tinkoff.timetable.controller;

import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.entity.Lesson;
import com.tinkoff.timetable.repository.CourseRepository;
import com.tinkoff.timetable.response.CourseResponse;
import com.tinkoff.timetable.response.ErrorResponse;
import com.tinkoff.timetable.response.GetCourseResponse;
import com.tinkoff.timetable.response.LessonResponse;
import com.yannbriancon.interceptor.HibernateQueryInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
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
public class CourseControllerTest extends AbstractControllerTest {
    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    @Autowired
    private HibernateQueryInterceptor hibernateQueryInterceptor;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    void setUp() {
        mvc = mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithUserDetails("admin")
    void createCourseSuccessful() throws Exception {
        long id = 1L;
        String uri = "/course/creation";
        courseDto.setId(null);
        String inputJson = mapToJson(courseDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(201, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new CourseResponse(id, COURSE_NAME), mapFromJson(content, CourseResponse.class));
        assertEquals(3, hibernateQueryInterceptor.getQueryCount());
        assertEquals(COURSE_NAME, courseRepository.getById(id).getName());
    }

    @Test
    @WithUserDetails("admin")
    void createCourseError() throws Exception {
        String uri = "/course/creation";
        CourseDto courseWithNullName = courseDto;
        courseWithNullName.setName(null);
        String inputJson = mapToJson(courseWithNullName);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 8, 'desc', 'Algebra 7 class gdz', 0, 0, 1)")
    void getCourseSuccessful() throws Exception {
        long id = 1L;
        String uri = "/course/" + id;
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertEquals(1, hibernateQueryInterceptor.getQueryCount());
        String content = mvcResult.getResponse().getContentAsString();
        courseDto.setId(id);
        assertEquals(new GetCourseResponse(courseDto), mapFromJson(content, GetCourseResponse.class));
    }

    @Test
    @WithUserDetails("admin")
    void getCourseNotFound() throws Exception {
        long id = 50L;
        String uri = "/course/" + id;
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new ErrorResponse("Course with id=50 not found"), mapFromJson(content, ErrorResponse.class));
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 8, 'desc', 'Algebra 7 class gdz', 0, 0, 1)")
    void updateSuccessful() throws Exception {
        long id = 1L;
        String uri = "/course/" + id;
        String inputJson = mapToJson(courseDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        assertEquals(1, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(new CourseResponse(id, COURSE_NAME), mapFromJson(content, CourseResponse.class));
        assertEquals(courseRepository.getById(id).getName(), COURSE_NAME);
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 8, 'desc', 'Algebra 7 class gdz', 0, 0, 1)")
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (4, '2000-12-12', 'Big Gogi', 'user', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 1)")
    @WithUserDetails("user")
    void updateByUserWithoutPermission() throws Exception {
        long id = 1L;
        String uri = "/course/" + id;
        String inputJson = mapToJson(courseDto);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();
        assertEquals(403, mvcResult.getResponse().getStatus());
    }

    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 8, 'desc', 'Algebra 7 class gdz', 0, 0, 1)")
    @WithUserDetails("admin")
    @Test
    void deleteCourseSuccessful() throws Exception {
        long id = 1L;
        String uri = "/course/" + id;
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .delete(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(3, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        assertTrue(courseRepository.findById(1L).isEmpty());
    }

    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 8, 'desc', 'Algebra 7 class gdz', 0, 0, 1)")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES " +
            "(1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)," +
            "(2, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T10:00', 0, 0)")
    @Sql(statements = "INSERT INTO course_lessons " +
            "VALUES" +
            "(1, 1), (1, 2)")
    void getCourseLessons() throws Exception {
        long id = 1L;
        String uri = "/course/" + id + "/lesson";
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders
                .get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        assertEquals(2, hibernateQueryInterceptor.getQueryCount());
        assertEquals(200, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        List<LessonResponse> lessons = Arrays.stream(mapFromJson(content, LessonResponse[].class)).toList();
        assertEquals("lesson", lessons.get(0).getName());
    }


    @Test
    @WithUserDetails("admin")
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 8, 'desc', 'Algebra 7 class gdz', 0, 0, 1)")
    void addLesson() throws Exception {
        long id = 1L;
        String uri = "/course/" + id + "/lesson";
        String inputJson = mapToJson(lessonDto);
        hibernateQueryInterceptor.startQueryCount();
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson))
                .andReturn();
        assertEquals(5, hibernateQueryInterceptor.getQueryCount());
        assertEquals(201, mvcResult.getResponse().getStatus());
        String content = mvcResult.getResponse().getContentAsString();
        Lesson lesson = new Lesson();
        lesson.setId(1L);
        assertTrue(courseRepository.getById(id).getLessons().contains(lesson));
    }
}
