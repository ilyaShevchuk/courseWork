package com.tinkoff.timetable.model.request;


import com.tinkoff.timetable.model.dto.TeacherDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Login can't be empty")
    private String login;

    @NotBlank(message = "Password can't be empty")
    private String password;

    @NotNull
    private TeacherDto teacher;
}

