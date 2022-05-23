package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.entity.Course;
import com.tinkoff.timetable.model.mapper.CourseMapper;
import com.tinkoff.timetable.model.mapper.LessonMapper;
import com.tinkoff.timetable.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    @Transactional(readOnly = true)
    public List<CourseDto> getAll() {
        return courseRepository.findAll().stream()
                .map((courseMapper::fromEntity))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<LessonDto> getCourseLessons(long courseId) {
        Course course = getById(courseId);
        return course.getLessons().stream()
                .map(lessonMapper::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CourseDto getDtoById(long id) {
        return courseMapper.fromEntity(getById(id));
    }

    @Transactional
    public CourseDto addCourse(CourseDto courseDto) {
        CourseDto addedCourse = courseMapper.fromEntity(courseRepository.save(courseMapper.fromDto(courseDto)));
        log.info(String.format("Course id=%d created", addedCourse.getId()));
        return addedCourse;
    }

    @Transactional
    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        var old = getById(id);
        old.setCategory(courseDto.getCategory());
        old.setDescription(courseDto.getDescription());
        old.setName(courseDto.getName());
        old.setStudentCount(courseDto.getStudentsCount());
        old.setTeacher(teacherService.getById(courseDto.getTeacherId()));
        log.info(String.format("Course id=%d updated", old.getId()));
        return courseMapper.fromEntity(courseRepository.save(old));
    }

    @Transactional
    public void deleteCourse(Long id) {
        Course byId = getById(id);
        courseRepository.delete(byId);
        log.info(String.format("Course id=%d deleted", id));
    }

    @Transactional
    public LessonDto addLessonToCourse(long courseId, LessonDto lessonDto) {
        Course course = getById(courseId);
        LessonDto addedLesson = lessonService.addLesson(lessonDto);
        course.getLessons().add(lessonService.getById(addedLesson.getId()));
        courseRepository.save(course);
        log.info(String.format("Add to Course with id=%d lesson with id=%d", courseId, addedLesson.getId()));
        return addedLesson;
    }

    @Transactional
    public CourseDto createFromCopy(long courseId, long teacherId) {
        CourseDto courseDto = courseMapper.fromEntity(
                courseRepository.save(courseMapper.makeCopy(getById(courseId), teacherService.getById(teacherId))));
        log.info(String.format("Course id=%d created from copy id=%d", courseDto.getId(), courseId));
        return courseDto;
    }

    private Course getById(long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course with id=" + id + " not found"));
    }
}
