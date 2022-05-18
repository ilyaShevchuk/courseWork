package com.tinkoff.timetable.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CourseResponse implements IResponse {

    @JsonProperty("id")
    private long id;

    @JsonProperty("name")
    private String name;

    public CourseResponse(long id, String name) {
        this.id = id;
        this.name = name;
    }
}

