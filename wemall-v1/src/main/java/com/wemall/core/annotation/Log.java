package com.wemall.core.annotation;


import com.wemall.foundation.domain.LogType;

import java.lang.annotation.*;

@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Log {
    public abstract String title();

    public abstract String entityName();

    public abstract LogType type();

    public abstract String description();

    public abstract String ip();
}