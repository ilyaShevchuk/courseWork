package com.tinkoff.timetable.annotation.validation.extraInfo;

import com.tinkoff.timetable.model.dto.LessonDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class ExtraInfoValidator implements ConstraintValidator<ValidExtraInfo, LessonDto> {
    @Override
    public void initialize(ValidExtraInfo constraintAnnotation) {

    }

    @Override
    public boolean isValid(LessonDto dto, ConstraintValidatorContext constraintValidatorContext) {
        String regexOnline = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern online = Pattern.compile(regexOnline);
        String regexOffline = "(?U)^ауд.+ [0-9]{1,4}, \\w+ (пр.|ул.|пер.)+, д.+[0-9]{1,4}+(, Лит.\\w)?$";
        Pattern offline = Pattern.compile(regexOffline);
        return switch (dto.getType()) {
            case ONLINE -> online.matcher(dto.getExtraInfo()).matches();
            case OFFLINE -> offline.matcher(dto.getExtraInfo()).matches();
        };
    }
}
