package com.tinkoff.timetable.model.entity;

import com.tinkoff.timetable.security.user.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Setter
@Getter
@Entity
@Table
public class Teacher {

    @GeneratedValue
//            (strategy = GenerationType.SEQUENCE)
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

    public int getAge() {
        return Period.between(birthday, LocalDate.now()).getYears();
    }
}
