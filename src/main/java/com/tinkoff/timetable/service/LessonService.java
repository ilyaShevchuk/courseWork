package com.tinkoff.timetable.service;

import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.entity.Lesson;
import com.tinkoff.timetable.model.mapper.LessonMapper;
import com.tinkoff.timetable.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;
    private final TeacherService teacherService;

    public Lesson getById(long id) {
        return lessonRepository.findById(id).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("Lesson with id=%d not found",id));
        });
    }

    @Transactional
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
        LessonDto addedLesson = lessonMapper.fromEntity(lessonRepository.save(lessonMapper.fromDto(dto)));
        log.info(String.format("Lesson id=%d created", addedLesson.getId()));
        return addedLesson;
    }

    @Transactional
    public LessonDto updateLesson(long id, LessonDto dto) {
        var old = getById(id);
        old.setTime(dto.getTime());
        old.setName(dto.getName());
        if (dto.getTeacherId() != null) {
            old.setTeacher(teacherService.getById(dto.getTeacherId()));
        }
        old.setDescription(dto.getDescription());
        log.info(String.format("Lesson id=%d updated", id));
        return lessonMapper.fromEntity(lessonRepository.save(old));
    }

    public void deleteLesson(long id) {
        Lesson byId = getById(id);
        lessonRepository.delete(byId);
        log.info(String.format("Lesson id=%d deleted", id));
    }

    @Transactional
    public LessonDto updateTeacher(long id, long teacherId) {
        Lesson lesson = getById(id);
        lesson.setTeacher(teacherService.getById(teacherId));
        log.info(String.format("Set to Lesson with id=%d Teacher with id%d", id, teacherId));
        return lessonMapper.fromEntity(lessonRepository.save(lesson));
    }
}
