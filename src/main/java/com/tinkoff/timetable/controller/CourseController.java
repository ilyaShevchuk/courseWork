package com.tinkoff.timetable.controller;

import com.tinkoff.timetable.model.dto.CourseDto;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.response.CourseResponse;
import com.tinkoff.timetable.response.GetCourseResponse;
import com.tinkoff.timetable.response.IResponse;
import com.tinkoff.timetable.response.LessonResponse;
import com.tinkoff.timetable.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/creation")
    public ResponseEntity<IResponse> addCourse(@Valid @RequestBody CourseDto requestBody) {
        CourseDto course = courseService.addCourse(requestBody);
        return new ResponseEntity<>(
                new CourseResponse(course.getId(), course.getName()),
                HttpStatus.CREATED);
    }

    @PostMapping("/{course_id}/copy")
    public ResponseEntity<IResponse> addCourseFromCopy(@PathVariable("course_id") Long courseId,
                                                        @Valid @RequestBody Long teacherId) {
        CourseDto course = courseService.createFromCopy(courseId, teacherId);
        return new ResponseEntity<>(
                new CourseResponse(course.getId(), course.getName()),
                HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<IResponse>> getAllCourses() {
        return new ResponseEntity<>(
                courseService.getAll().stream()
                .map(GetCourseResponse::new)
                .collect(Collectors.toList()),
                HttpStatus.CREATED);
    }

    @GetMapping("/{course_id}")
    public ResponseEntity<IResponse> getCourse(@PathVariable("course_id") Long courseId) {
        CourseDto course = courseService.getDtoById(courseId);
        return new ResponseEntity<>(
                new GetCourseResponse(course),
                HttpStatus.OK);
    }

    @PreAuthorize("courseBelongsToUser(#courseId)")
    @PostMapping("/{course_id}")
    public ResponseEntity<IResponse> updateCourse(@PathVariable("course_id") Long courseId,
                                                  @Valid @RequestBody CourseDto requestBody) {
        CourseDto course = courseService.updateCourse(courseId, requestBody);
        return new ResponseEntity<>(
                new CourseResponse(course.getId(), course.getName()),
                HttpStatus.CREATED);

    }

    @DeleteMapping("/{course_id}")
    public ResponseEntity<IResponse> deleteCourse(@PathVariable("course_id") Long courseId) {
        courseService.deleteCourse(courseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{course_id}/lesson")
    public ResponseEntity<List<IResponse>> getCourseLessons(@PathVariable("course_id") Long courseId){
        return new ResponseEntity<>(courseService.getCourseLessons(courseId).stream()
                .map(lsn -> new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(), lsn.getTime(), lsn.getTeacherId()))
                .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @PostMapping("/{course_id}/lesson")
    @PreAuthorize("courseBelongsToUser(#courseId)")
    public ResponseEntity<IResponse> addLesson(@PathVariable("course_id") Long courseId,
                                               @Valid @RequestBody LessonDto requestBody) {
        LessonDto lsn = courseService.addLessonToCourse(courseId, requestBody);
        return new ResponseEntity<>(
                new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(), lsn.getTime(),lsn.getTeacherId()),
                HttpStatus.CREATED);
    }
}
