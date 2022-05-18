package com.tinkoff.timetable.security.user;

import com.tinkoff.timetable.model.entity.Teacher;
import com.tinkoff.timetable.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final TeacherRepository teacherRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Teacher teacher = teacherRepository.findByLoginIgnoreCase(login).orElseThrow(() -> {
            throw new EntityNotFoundException(String.format("Student with login=%s not found", login));
        });
        return CustomUserDetails.fromTeacherToCustomUserDetails(teacher);
    }
}

