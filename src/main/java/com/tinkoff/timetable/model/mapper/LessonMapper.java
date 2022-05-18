package com.tinkoff.timetable.model.mapper;

import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.entity.Lesson;
import com.tinkoff.timetable.repository.TeacherRepository;
import com.tinkoff.timetable.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonMapper {
    private final TeacherRepository teacherRepository;

    public LessonDto fromEntity(Lesson lesson) {
        return LessonDto.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .time(lesson.getTime())
                .teacherId((lesson.getTeacher() != null) ? lesson.getTeacher().getId() : null)
                .extraInfo(lesson.getExtraInfo())
                .type(lesson.getType())
                .build();
    }

    public Lesson fromDto(LessonDto dto) {
        Lesson lesson = new Lesson();
        lesson.setName(dto.getName());
        lesson.setDescription(dto.getDescription());
        lesson.setTime(dto.getTime());
        lesson.setType(dto.getType());
        lesson.setExtraInfo(dto.getExtraInfo());
        if (dto.getTeacherId() != null) {
            lesson.setTeacher(teacherRepository.getById(dto.getTeacherId()));
        }
        return lesson;
    }
}
