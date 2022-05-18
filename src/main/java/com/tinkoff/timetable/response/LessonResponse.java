package com.tinkoff.timetable.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LessonResponse implements IResponse {

    private Long id;

    private String name;

    private String type;

    private LocalDateTime time;

    @JsonProperty("teacher_id")
    private Long teacherId;
}
