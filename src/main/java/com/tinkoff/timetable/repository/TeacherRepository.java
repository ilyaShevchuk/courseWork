package com.tinkoff.timetable.repository;

import com.tinkoff.timetable.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByLoginIgnoreCase(String login);
}

