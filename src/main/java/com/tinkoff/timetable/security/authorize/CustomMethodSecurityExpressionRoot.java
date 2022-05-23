package com.tinkoff.timetable.security.authorize;

import com.tinkoff.timetable.security.user.CustomUserDetails;
import com.tinkoff.timetable.security.user.Permission;
import com.tinkoff.timetable.service.CourseService;
import com.tinkoff.timetable.service.LessonService;
import com.tinkoff.timetable.service.TeacherService;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;

public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private final CourseService courseService;
    private final TeacherService teacherService;
    private final LessonService lessonService;


    public CustomMethodSecurityExpressionRoot(Authentication authentication, TeacherService teacherService, CourseService courseService, LessonService lessonService) {
        super(authentication);
        this.teacherService = teacherService;
        this.courseService = courseService;
        this.lessonService = lessonService;
    }


    private boolean checkAuthorities(CustomUserDetails principal){
        Set<SimpleGrantedAuthority> authorities = (Set<SimpleGrantedAuthority>) principal.getGrantedAuthorities();
        return authorities.contains(new SimpleGrantedAuthority(Permission.REFACTOR_ALL.getPermission()));
    }

    public boolean courseBelongsToUser(Long courseId) {
        CustomUserDetails principal = (CustomUserDetails) this.getPrincipal();
        if (checkAuthorities(principal)){
            return true;
        }
        return teacherService.getByLogin(principal.getUsername()).getId()
                .equals(courseService.getDtoById(courseId).getTeacherId());
    }
    public boolean lessonBelongsToUser(Long lessonId) {
        CustomUserDetails principal = (CustomUserDetails) this.getPrincipal();
        if (checkAuthorities(principal)){
            return true;
        }
        return teacherService.getByLogin(principal.getUsername()).getId()
                .equals(lessonService.getDtoById(lessonId).getTeacherId());
    }

    @Override
    public Object getFilterObject() {
        return null;
    }

    @Override
    public void setFilterObject(Object filterObject) {

    }

    @Override
    public Object getReturnObject() {
        return null;
    }

    @Override
    public void setReturnObject(Object returnObject) {

    }

    @Override
    public Object getThis() {
        return null;
    }
}