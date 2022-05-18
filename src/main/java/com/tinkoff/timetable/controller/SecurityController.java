package com.tinkoff.timetable.controller;


import com.tinkoff.timetable.model.request.RegistrationRequest;
import com.tinkoff.timetable.response.IResponse;
import com.tinkoff.timetable.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class SecurityController {

    private final TeacherService teacherService;

    @PostMapping("/register")
    public ResponseEntity<IResponse> registerTeacher(@RequestBody @Valid RegistrationRequest registrationRequest) {
        teacherService.registerTeacher(registrationRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/admin/register")
    public ResponseEntity<IResponse> registerAdmin(@RequestBody @Valid RegistrationRequest registrationRequest) {

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}