package com.tinkoff.timetable.controller;

import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.response.IResponse;
import com.tinkoff.timetable.response.LessonResponse;
import com.tinkoff.timetable.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/lesson")
public class LessonController {
    private final LessonService lessonService;

    @PostMapping("/creation")
    public ResponseEntity<IResponse> addLesson(@Valid @RequestBody LessonDto requestBody) {
        LessonDto lsn = lessonService.addLesson(requestBody);
        return new ResponseEntity<>(
                new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(), lsn.getTime(), lsn.getTeacherId()),
                HttpStatus.CREATED);
    }


    @GetMapping("/{lesson_id}")
    public ResponseEntity<IResponse> getLesson(@PathVariable("lesson_id") Long lessonId) {
        LessonDto lsn = lessonService.getDtoById(lessonId);
        return new ResponseEntity<>(
                new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(), lsn.getTime(), lsn.getTeacherId()),
                HttpStatus.OK);
    }

    @PostMapping("/{lesson_id}")
    @PreAuthorize("lessonBelongsToUser(#lessonId)")
    public ResponseEntity<IResponse> updateLesson(@PathVariable("lesson_id") Long lessonId,
                                                  @Valid @RequestBody LessonDto requestBody) {
        LessonDto lsn = lessonService.updateLesson(lessonId, requestBody);
        return new ResponseEntity<>(
                new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(),lsn.getTime(),  lsn.getTeacherId()),
                HttpStatus.OK);

    }

    @DeleteMapping("/{lesson_id}")
    public ResponseEntity<IResponse> deleteLesson(@PathVariable("lesson_id") Long lessonId) {
        lessonService.deleteLesson(lessonId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/{lesson_id}/teacher")
    @PreAuthorize("lessonBelongsToUser(#lessonId)")
    public ResponseEntity<IResponse> appointTeacher(@PathVariable("lesson_id") Long lessonId,
                                                    @Valid @RequestBody Long teacherId) {
        LessonDto lsn = lessonService.updateTeacher(lessonId, teacherId);
        return new ResponseEntity<>(
                new LessonResponse(lsn.getId(), lsn.getName(), lsn.getType().name(), lsn.getTime(), lsn.getTeacherId()),
                HttpStatus.OK);
    }
}
