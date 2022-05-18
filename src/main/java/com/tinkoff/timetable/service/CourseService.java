package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.entity.Course;
import com.tinkoff.timetable.model.mapper.CourseMapper;
import com.tinkoff.timetable.model.mapper.LessonMapper;
import com.tinkoff.timetable.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final TeacherService teacherService;
    private final LessonService lessonService;
    private final LessonMapper lessonMapper;

    private Course getById(long id) {
        return courseRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Course with id=" + id + " not found"));
    }

    public List<CourseDto> getAll() {
        return courseRepository.findAll().stream()
                .map((courseMapper::fromEntity))
                .collect(Collectors.toList());
    }

    public List<LessonDto> getCourseLessons(long courseId){
        Course course = getById(courseId);
        return course.getLessons().stream()
                .map(lessonMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public CourseDto getDtoById(long id) {
        return courseMapper.fromEntity(getById(id));
    }

    public CourseDto addCourse(CourseDto courseDto) {
        return courseMapper.fromEntity(courseRepository.save(courseMapper.fromDto(courseDto)));
    }

    public CourseDto updateCourse(Long id, CourseDto courseDto) {
        var old = getById(id);
        old.setCategory(courseDto.getCategory());
        old.setDescription(courseDto.getDescription());
        old.setName(courseDto.getName());
        old.setStudentCount(courseDto.getStudentsCount());
        old.setTeacher(teacherService.getById(courseDto.getTeacherId()));
        return courseMapper.fromEntity(courseRepository.save(old));
    }

    public void deleteCourse(Long id) {
        getById(id);
        courseRepository.deleteById(id);
    }

    public LessonDto addLessonToCourse(long courseId, LessonDto lessonDto) {
        var course = getById(courseId);
        var addedLesson = lessonService.addLesson(lessonDto);
        course.getLessons().add(lessonService.getById(addedLesson.getId()));
        courseRepository.save(course);
        return addedLesson;
    }

    public CourseDto createFromCopy(long courseId, long teacherId) {
        return courseMapper.fromEntity(
                courseRepository.save(courseMapper.makeCopy(getById(courseId), teacherService.getById(teacherId))));
    }
}
