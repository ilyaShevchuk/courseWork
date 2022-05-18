package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.model.entity.Lesson;
import com.tinkoff.timetable.model.entity.Teacher;
import com.tinkoff.timetable.model.request.RegistrationRequest;
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
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class TeacherServiceTest extends AbstractServiceTest {
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TeacherService teacherService;

    @AfterEach
    public void afterEach(){
    }

    @Test
    public void createTeacher(){
        TeacherDto dto = teacherService.addTeacher(teacherDto);
        assertEquals(teacherRepository.getById(dto.getId()).getName(), teacherDto.getName());
    }

    @Test
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (3, '2000-12-12', 'Big Gogi', 'login3', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    public void getTeacher(){
        assertEquals(teacherService.getDtoById(3).getName(), "Big Gogi");
    }

    @Test
    public void getTeacherError(){
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            teacherService.getById(50);
        });
        assertEquals(entityNotFoundException.getMessage(), "Teacher with id=50 not found");
    }

    @Test
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (4, '2000-12-12', 'Big Gogi', 'login4', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    public void deleteTeacher(){
        teacherService.deleteTeacher(4);
        assertTrue(teacherRepository.findById(4L).isEmpty());
    }
    @Test
    public void registerTeacher(){
        teacherService.registerTeacher(registrationRequest);
        assertEquals(teacherRepository.findByLoginIgnoreCase(registrationRequest.getLogin()).get().getName(),
                registrationRequest.getTeacher().getName());
    }
    @Test
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (5, '2000-12-12', 'Big Gogi', 'login5', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (6, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T14:00', 0, 5)")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (7, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 5)")
    public void getTeacherSchedule(){
        List<Teacher> all = teacherRepository.findAll();
        List<Lesson> all1 = lessonRepository.findAll();
        List<LessonDto> teacherSchedule = teacherService.getTeacherSchedule(5);
        List<LocalDateTime> dateTimes = teacherSchedule.stream().map(lesson -> lesson.getTime()).collect(Collectors.toList());
        assertEquals(dateTimes.get(0).getHour(), 12);
        assertEquals(dateTimes.get(1).getHour(), 14);
    }
    @Test
    @Sql(statements = "INSERT INTO teacher " +
            "VALUES (6, '2000-12-12', 'Big Gogi', 'login6', '$2a$12$.5iezlTbsHdGO96iQpCXBe4c/yVFstSwDJkd6vaWBJcxV4/r0u7km', 0)")
    public void getByLogin(){
        assertEquals(teacherService.getByLogin("login6").getId(), 6);
    }
}
