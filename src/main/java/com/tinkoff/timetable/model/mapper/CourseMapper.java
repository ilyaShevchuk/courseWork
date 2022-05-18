package com.tinkoff.timetable.model.mapper;

import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.entity.Course;
import com.tinkoff.timetable.model.entity.Teacher;
import com.tinkoff.timetable.service.LessonService;
import com.tinkoff.timetable.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CourseMapper {

    private final TeacherService teacherService;
    private final LessonService lessonService;

    public CourseDto fromEntity(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .teacherId((course.getTeacher() != null) ? course.getTeacher().getId() : null)
                .category(course.getCategory())
                .studentsCount(course.getStudentCount())
                .type(course.getType())
                .build();
    }

    public Course fromDto(CourseDto dto) {
        Course course = new Course();
        if (dto.getId() != null) {
            course.setId(dto.getId());
        }
        course.setName(dto.getName());
        course.setDescription(dto.getDescription());
        if (dto.getTeacherId() != null) {
            course.setTeacher(teacherService.getById(dto.getTeacherId()));
        }
        course.setStudentCount(course.getStudentCount());
        course.setCategory(dto.getCategory());
        course.setType(dto.getType());
        return course;
    }

    public Course makeCopy(Course course, Teacher teacher){
        Course newCourse = new Course();
        newCourse.setName(course.getName());
        newCourse.setDescription(course.getDescription());
        newCourse.setTeacher(teacher);
        newCourse.setStudentCount(0);
        newCourse.setCategory(course.getCategory());
        newCourse.setType(course.getType());
        newCourse.setLessons(
                course.getLessons().stream().map(
                        lesson -> {
                            LessonDto dto = lessonService.addLesson(LessonDto.builder()
                                .name(lesson.getName())
                                .description(lesson.getDescription())
                                .type(lesson.getType())
                                .extraInfo(lesson.getExtraInfo())
                                .time(null)
                                .teacherId(lesson.getTeacher().getId())
                                .build());
                            return lessonService.getById(dto.getId());})
                        .collect(Collectors.toList()));
        return newCourse;
    }
}

