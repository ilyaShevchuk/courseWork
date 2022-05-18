package com.tinkoff.timetable.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinkoff.timetable.model.CourseCategory;
import com.tinkoff.timetable.model.FormatType;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class CourseDto {

    private Long id;

    @NotBlank(message = "Name can't be empty")
    private String name;

    private String description;

    private CourseCategory category;

    private Integer studentsCount = 0;

    @NotNull
    private FormatType type;

    @NotNull
    @JsonProperty("teacher_id")
    private Long teacherId;
}
