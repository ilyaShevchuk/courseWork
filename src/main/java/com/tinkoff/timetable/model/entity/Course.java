package com.tinkoff.timetable.model.entity;

import com.tinkoff.timetable.model.CourseCategory;
import com.tinkoff.timetable.model.FormatType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course {

    @GeneratedValue
//            (strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "category")
//    @Enumerated(EnumType.ORDINAL)
//    @Enumerated(EnumType.STRING)
    private CourseCategory category;

    @Column(name = "members")
    private Integer studentCount;

    @Column(name = "type")
    private FormatType type;

    @ManyToOne
    private Teacher teacher;

    @OneToMany
    private Set<Lesson> lessons;

    public Course() {
    }
}
