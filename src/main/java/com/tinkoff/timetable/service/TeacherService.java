package com.tinkoff.timetable.service;

import com.tinkoff.timetable.exception.NoAccessException;
import com.tinkoff.timetable.model.dto.LessonDto;
import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.model.entity.Teacher;
import com.tinkoff.timetable.model.mapper.LessonMapper;
import com.tinkoff.timetable.model.mapper.TeacherMapper;
import com.tinkoff.timetable.model.request.RegistrationRequest;
import com.tinkoff.timetable.repository.LessonRepository;
import com.tinkoff.timetable.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    @Transactional(readOnly = true)
    public Teacher getById(long id) {
        return teacherRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Teacher with id=" + id + " not found"));
    }

    @Transactional(readOnly = true)
    public Teacher getByLogin(String login) {
        return teacherRepository.findByLoginIgnoreCase(login).orElseThrow(
                () -> new NoAccessException("Teacher with id login" + login + " not found"));
    }

    @Transactional(readOnly = true)
    public TeacherDto getDtoById(long id) {
        return teacherMapper.fromEntity(getById(id));
    }

    public TeacherDto addTeacher(TeacherDto teacherDto) {
        TeacherDto addedTeacher = teacherMapper.fromEntity(teacherRepository.save(teacherMapper.fromDto(teacherDto)));
        log.info(String.format("Teacher id=%d created", addedTeacher.getId()));
        return addedTeacher;
    }

    @Transactional
    public TeacherDto updateTeacher(long id, TeacherDto teacherDto) {
        var old = getById(id);
        old.setName(teacherDto.getName());
        old.setBirthday(teacherDto.getBirthday());
        log.info(String.format("Teacher id=%d updated", id));
        return teacherMapper.fromEntity(teacherRepository.save(old));
    }

    @Transactional
    public void deleteTeacher(long id) {
        Teacher byId = getById(id);
        teacherRepository.delete(byId);
        log.info(String.format("Teacher id=%d deleted", id));
    }

    public TeacherDto registerTeacher(RegistrationRequest registrationRequest) {
        Teacher teacher = teacherMapper.fromDto(registrationRequest.getTeacher());
        teacher.setLogin(registrationRequest.getLogin());
        teacher.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        log.info(String.format("Teacher id=%d registered", teacher.getId()));
        return teacherMapper.fromEntity(teacherRepository.save(teacher));
    }

    @Transactional
    public List<LessonDto> getTeacherSchedule(long teacherId) {
        getById(teacherId);
        return lessonRepository.getAllByTeacherId(teacherId).stream()
                .map(lessonMapper::fromEntity)
                .sorted()
                .collect(Collectors.toList());
    }
}

