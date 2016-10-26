package com.rambo.sdao.annotation;

import java.lang.annotation.*;

/**
 * Create by rambo on 2016/5/10
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface TableName {
    String value();
}