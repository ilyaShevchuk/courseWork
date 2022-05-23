package com.tinkoff.timetable.model.entity;

import com.tinkoff.timetable.security.user.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;

@Setter
@Getter
@Entity
@Table
public class Teacher {

    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.PERSIST)
    private Set<Course> courses;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.PERSIST)
    private Set<Lesson> lessons;

    @PreRemove
    private void preRemove(){
        for(Lesson lesson : lessons){
            lesson.setTeacher(null);
        }
        for(Course course : courses){
            course.setTeacher(null);
        }
    }

    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
