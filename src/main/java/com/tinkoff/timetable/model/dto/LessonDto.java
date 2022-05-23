package com.tinkoff.timetable.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tinkoff.timetable.annotation.validation.extraInfo.ValidExtraInfo;
import com.tinkoff.timetable.model.FormatType;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@ValidExtraInfo
public class LessonDto implements Comparable<LessonDto>{

    private Long id;

    @NotBlank(message = "Student name must be not null")
    private String name;

    private String description;

    @JsonProperty("teacher_id")
    private Long teacherId;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime time;

    @NotNull
    private FormatType type;

    @NotNull
    @JsonProperty(value = "extra_info")
    private String extraInfo;

    @Override
    public int compareTo(LessonDto o) {
        return time.compareTo(o.getTime());
    }
}
