package com.tinkoff.timetable.model.mapper;

import com.tinkoff.timetable.model.dto.TeacherDto;
import com.tinkoff.timetable.model.entity.Teacher;
import org.springframework.stereotype.Component;

@Component
public class TeacherMapper {
    public TeacherDto fromEntity(Teacher teacher) {
        return TeacherDto.builder()
                .id(teacher.getId())
                .name(teacher.getName())
                .birthday(teacher.getBirthday())
                .age(teacher.getAge())
                .build();
    }

    public Teacher fromDto(TeacherDto dto) {
        Teacher teacher = new Teacher();
        if (dto.getId() != null) {
            teacher.setId(dto.getId());
        }
        teacher.setName(dto.getName());
        teacher.setBirthday(dto.getBirthday());
        return teacher;
    }

}
