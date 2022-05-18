package com.tinkoff.timetable.model.entity;

import com.tinkoff.timetable.model.FormatType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    private Teacher teacher;

    @Column(name = "time")
    private LocalDateTime time;

    @Column(name = "type")
    private FormatType type;

    @Column(name = "extra_info")
    private String extraInfo;

    public Lesson() {
    }
}
