package com.tinkoff.timetable.model;

public enum CourseCategory {
    CATEGORY(null),
        TECHNICAL(CATEGORY),
            PROGRAMMING(TECHNICAL),
                JAVA(PROGRAMMING),
                C_PLUS_PLUS(PROGRAMMING),
                DEVOPS_TOOLS(TECHNICAL),
            MATH(TECHNICAL),
                GEOMETRY(MATH),
                ALGEBRA(MATH),
        HUMANITIES(CATEGORY),
                HISTORY(HUMANITIES),
                    RUSSIAN(HISTORY),
                    WORLD(HISTORY);

    private CourseCategory parent = null;

    CourseCategory(CourseCategory category) {
        this.parent = category;
    }

    public boolean is(CourseCategory other) {
        if (other == null) {
            return false;
        }

        for (CourseCategory t = this; t != null; t = t.parent) {
            if (other == t) {
                return true;
            }
        }
        return false;
    }
}
