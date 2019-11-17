package com.stars.controller.entity;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import jdk.nashorn.internal.ir.annotations.Ignore;

import java.lang.annotation.Annotation;
import java.util.Collection;

public class IgnoreStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        Collection<Annotation> annotations = fieldAttributes.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Ignore.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
