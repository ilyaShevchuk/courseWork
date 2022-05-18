package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.model.request.RegistrationRequest;
import com.tinkoff.timetable.repository.CourseRepository;
import com.tinkoff.timetable.repository.LessonRepository;
import com.tinkoff.timetable.repository.TeacherRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CourseServiceTest extends AbstractServiceTest {
    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private LessonRepository lessonRepository;


    @AfterEach
    void afterEach() {
    }

    @Test
    public void createCourse() {
        CourseDto dto = courseService.addCourse(courseDto);
        assertEquals(courseRepository.getById(dto.getId()).getName(), COURSE_NAME);
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (1, 0, 'desc', 'course', 0, null, 0)")
    public void getCourseById() {
        assertEquals(courseService.getDtoById(1).getName(), "course");
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (9, 0, 'desc', 'course', 0, null, 0)")
    public void updateCourse() {
        courseService.updateCourse(9L, courseDto);
        assertEquals(courseService.getDtoById(9).getName(), courseDto.getName());
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (2, 0, 'desc', 'course', 0, null, 0)")
    public void deleteCourse() {
        courseService.deleteCourse(2L);
        assertTrue(courseRepository.findById(2L).isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (3, 0, 'desc', 'course', 0, null, 0)")
    public void addLesson() {
        LessonDto lesson = courseService.addLessonToCourse(3, lessonDto);
        assertEquals(courseRepository.getById(3L).getLessons().get(0).getName(), lessonDto.getName());
    }

    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (4, 0, 'desc', 'course', 0, null, 0)")
    @Sql(statements = "INSERT INTO teacher(id, birthday, name, login, password, role) " +
            "VALUES (5, '1999-01-08', 'Good teacher', 'user1', 'user1', 0)")
    public void fromCopy(){
        CourseDto fromCopy = courseService.createFromCopy(4, 5);
        assertEquals(fromCopy.getName(), "course");
        assertEquals(fromCopy.getTeacherId(), 5);
    }

    @Test
    @Sql(statements = "INSERT INTO teacher(id, birthday, name, login, password, role) " +
            "VALUES (6, '1999-01-08', 'Good teacher', 'user1', 'user1', 0)")
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (5, 0, 'desc', 'course', 0, null, 0)")
    public void getLessons(){
        courseService.addLessonToCourse(5, lessonDto);
        courseService.addLessonToCourse(5, lessonDto);
        var lessons = courseService.getCourseLessons(5);
        assertEquals(courseRepository.getById(5L).getLessons().get(0).getName(), lessonDto.getName());
        assertEquals(courseRepository.getById(5L).getLessons().get(1).getName(), lessonDto.getName());
        assertEquals(lessons.size(), 2);
    }
    @Test
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (6, 0, 'desc', 'course', 0, null, 0)")
    @Sql(statements = "INSERT INTO course(id, category, description,name, members, teacher_id, type) " +
            "VALUES (7, 0, 'desc', 'course', 0, null, 0)")
    public void getAll(){
        var courses = courseService.getAll();
        assertEquals(courses.get(0).getName(), "course");
        assertEquals(courses.size(), 2);
    }

    @Test
    public void getCourseByIdError() {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            courseService.getDtoById(50);
        });
        assertEquals(entityNotFoundException.getMessage(), "Course with id=50 not found");
    }



}
