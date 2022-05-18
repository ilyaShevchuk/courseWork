package com.tinkoff.timetable.repository;

import com.tinkoff.timetable.model.entity.Lesson;
import com.tinkoff.timetable.model.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> getAllByTeacherId(Long teacherId);
}



