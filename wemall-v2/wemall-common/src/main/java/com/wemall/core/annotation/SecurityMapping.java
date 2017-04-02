package com.wemall.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注释，用于权限校验
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.METHOD})
public @interface SecurityMapping {
    public abstract String title();// 标题

    public abstract String value();// url地址值

    public abstract String rname();// 角色名

    public abstract String rcode();// 角色代码

    public abstract int rsequence();// 角色序号

    public abstract String rgroup();// 角色组

    public abstract String rtype();// 角色类型

    public abstract boolean display();// 是否显示
}