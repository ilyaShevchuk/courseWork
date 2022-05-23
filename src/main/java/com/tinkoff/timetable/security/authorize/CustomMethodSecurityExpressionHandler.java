package com.tinkoff.timetable.security.authorize;

import com.tinkoff.timetable.service.CourseService;
import com.tinkoff.timetable.service.LessonService;
import com.tinkoff.timetable.service.TeacherService;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomMethodSecurityExpressionHandler
        extends DefaultMethodSecurityExpressionHandler {

    @Autowired
    private TeacherService teacherService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private LessonService lessonService;


    private final AuthenticationTrustResolver trustResolver =
            new AuthenticationTrustResolverImpl();

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
            Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root =
                new CustomMethodSecurityExpressionRoot(authentication, teacherService, courseService, lessonService);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}