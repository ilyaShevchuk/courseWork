package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.entity.Lesson;
import com.tinkoff.timetable.model.mapper.LessonMapper;
import com.tinkoff.timetable.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final TeacherService teacherService;

    public Lesson getById(long id) {
        return lessonRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Lesson with id=" + id + " not found")
        );
    }

    public List<LessonDto> getLessonsByTeacher(long teacherId){
        teacherService.getDtoById(teacherId);
        return lessonRepository.getAllByTeacherId(teacherId).stream()
                .map(lessonMapper::fromEntity)
                .collect(Collectors.toList());
    }

    public LessonDto getDtoById(long id) {
        return lessonMapper.fromEntity(getById(id));
    }

    public LessonDto addLesson(LessonDto dto) {
        return lessonMapper.fromEntity(lessonRepository.save(lessonMapper.fromDto(dto)));
    }

    public LessonDto updateLesson(long id, LessonDto dto) {
        var old = getById(id);
        old.setTime(dto.getTime());
        old.setName(dto.getName());
        if (dto.getTeacherId() != null) {
            old.setTeacher(teacherService.getById(dto.getTeacherId()));
        }
        old.setDescription(dto.getDescription());
        return lessonMapper.fromEntity(lessonRepository.save(old));
    }

    public void deleteLesson(long id) {
        getById(id);
        lessonRepository.deleteById(id);
    }

    public LessonDto updateTeacher(long id, long teacherId) {
        Lesson lesson = getById(id);
        lesson.setTeacher(teacherService.getById(teacherId));
        return lessonMapper.fromEntity(lessonRepository.save(lesson));
    }
}
