package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.LessonDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class LessonServiceTest extends AbstractServiceTest {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private LessonRepository lessonRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @AfterEach
    void afterEach() {
    }

    @Test
    public void createLesson() {
        LessonDto dto = lessonService.addLesson(lessonDto);
        assertEquals(lessonDto.getName(), lessonRepository.getById(dto.getId()).getName());
    }

    @Test
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (1, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    public void updateLesson() {
        LessonDto dto = lessonService.updateLesson(1, lessonDto);
        var x = lessonRepository.findAll().size();
        assertEquals(lessonRepository.getById(dto.getId()).getName(), lessonDto.getName());
    }

    @Test
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (2, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    public void deleteLesson() {
        lessonService.deleteLesson(2);
        assertTrue(lessonRepository.findById(2L).isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (3, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    public void getById() {
        assertEquals(lessonService.getDtoById(3).getName(), "lesson");
    }

    @Test
    public void getByError() {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class,
                () -> {
                    lessonService.getById(500);
                });
        assertEquals(entityNotFoundException.getMessage(), "Lesson with id=500 not found");
    }

    @Test
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (4, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (3, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    public void getLessonsByTeaacher() {
        List<LessonDto> lessonsByTeacher = lessonService.getLessonsByTeacher(0);
        assertEquals(2, lessonsByTeacher.size());
        assertEquals( 0, lessonsByTeacher.get(0).getTeacherId());
    }

    @Test
    @Sql(statements = "INSERT INTO lesson(id, description, extra_info, name, time, type, teacher_id) " +
            "VALUES (5, 'desc', 'ауд. 2304, Кронверкский пр., д.49', 'lesson', '2022-05-25T12:00', 0, 0)")
    public void updateTeacher() {
        LessonDto dto = lessonService.updateTeacher(5, 0);
        assertEquals(lessonRepository.getById(5L).getTeacher().getName(),
                teacherRepository.getById(dto.getTeacherId()).getName());
    }
}
