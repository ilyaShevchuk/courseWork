package com.tinkoff.timetable.controller;

import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.response.IResponse;
import com.tinkoff.timetable.response.LessonResponse;
import com.tinkoff.timetable.response.TeacherResponse;
import com.tinkoff.timetable.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {
    private final TeacherService teacherService;

    @PostMapping("/creation")
    public ResponseEntity<IResponse> addTeacher(@Valid @RequestBody TeacherDto requestBody) {
        TeacherDto teacher = teacherService.addTeacher(requestBody);
        return new ResponseEntity<>(new TeacherResponse(teacher.getId(), teacher.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{teacher_id}")
    public ResponseEntity<IResponse> getTeacher(@PathVariable("teacher_id") Long teacherId) {
        TeacherDto teacherDto = teacherService.getDtoById(teacherId);
        return new ResponseEntity<>(new TeacherResponse(teacherDto.getId(), teacherDto.getName()), HttpStatus.OK);
    }

    @PostMapping("/{teacher_id}")
    public ResponseEntity<IResponse> updateTeacher(@PathVariable("teacher_id") Long teacherId,
                                                   @Valid @RequestBody TeacherDto requestBody) {
        TeacherDto teacherDto = teacherService.updateTeacher(teacherId, requestBody);
        return new ResponseEntity<>(new TeacherResponse(teacherDto.getId(), teacherDto.getName()), HttpStatus.CREATED);

    }

    @DeleteMapping("/{teacher_id}")
    public ResponseEntity<IResponse> deleteTeacher(@PathVariable("teacher_id") Long teacherId) {
        teacherService.deleteTeacher(teacherId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{teacher_id}/schedule")
    public ResponseEntity<List<IResponse>> getSchedule(@PathVariable Long teacher_id) {
        return new ResponseEntity<>(teacherService.getTeacherSchedule(teacher_id).stream()
                .map(lsn -> new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(), lsn.getTime(), lsn.getTeacherId()))
                .collect(Collectors.toList()), HttpStatus.OK);
    }

}

