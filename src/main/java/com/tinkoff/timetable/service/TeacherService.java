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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherMapper teacherMapper;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;
    private final LessonRepository lessonRepository;
    private final LessonMapper lessonMapper;

    public Teacher getById(long id) {
        return teacherRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Teacher with id=" + id + " not found"));
    }

    public Teacher getByLogin(String login) {
        return teacherRepository.findByLoginIgnoreCase(login).orElseThrow(
                () -> new NoAccessException("Teacher with id login" + login + " not found"));
    }

    public TeacherDto getDtoById(long id) {
        return teacherMapper.fromEntity(getById(id));
    }

    public TeacherDto addTeacher(TeacherDto teacherDto) {
        return teacherMapper.fromEntity(teacherRepository.save(teacherMapper.fromDto(teacherDto)));
    }

    public TeacherDto updateTeacher(long id, TeacherDto teacherDto) {
        var old = getById(id);
        old.setName(teacherDto.getName());
        old.setBirthday(teacherDto.getBirthday());
        return teacherMapper.fromEntity(teacherRepository.save(old));
    }

    public void deleteTeacher(long id) {
        getById(id);
        teacherRepository.deleteById(id);
    }

    public TeacherDto registerTeacher(RegistrationRequest registrationRequest) {
        Teacher teacher = teacherMapper.fromDto(registrationRequest.getTeacher());
        teacher.setLogin(registrationRequest.getLogin());
        teacher.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        return teacherMapper.fromEntity(teacherRepository.save(teacher));
    }

    public List<LessonDto> getTeacherSchedule(long teacherId){
        getById(teacherId);
        var lessons = lessonRepository.getAllByTeacherId(teacherId).stream()
                .map(lessonMapper::fromEntity).collect(Collectors.toList());
        Collections.sort(lessons);
        return lessons;
    }
}
