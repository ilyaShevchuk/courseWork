package com.tinkoff.timetable.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinkoff.timetable.model.dto.CourseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetCourseResponse implements IResponse {

    private long id;

    private String name;

    private String description;

    private Integer members;

    private String type;

    private String category;

    @JsonProperty("teacher_id")
    private long teacherId;

    public GetCourseResponse(CourseDto courseDto){
        this.id = courseDto.getId();
        this.name = courseDto.getName();
        this.description = courseDto.getDescription();
        this.members = courseDto.getStudentsCount();
        this.category = courseDto.getCategory().name();
        this.teacherId = courseDto.getTeacherId();
        this.type = courseDto.getType().name();
    }

}


