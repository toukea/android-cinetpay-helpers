package com.istat.cinetpay.helpers;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by istat on 09/01/17.
 */

public class Toolkit {

    public static final Class<?> getGenericTypeClass(Object obj, int genericIndex) {
        return getGenericTypeClass(obj.getClass(), genericIndex);
    }

    public static final Class<?> getGenericTypeClass(Class<?> baseClass, int genericIndex) {
        try {
            String className = ((ParameterizedType) baseClass
                    .getGenericSuperclass()).getActualTypeArguments()[genericIndex]
                    .toString().replaceFirst("class", "").trim();
            Class<?> clazz = Class.forName(className);
            return clazz;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(
                    "Class is not parametrized with generic type!!! Please use extends <> ");
        }
    }

    public static List<Field> getAllFieldFields(Class<?> klass, boolean includingPrivateAndSuper, boolean acceptStatic) {
        if (includingPrivateAndSuper) {
            return getAllFieldIncludingPrivateAndSuper(klass, acceptStatic);
        } else {
            List<Field> fields = new ArrayList<Field>();
            Field[] tmp = klass.getDeclaredFields();
            for (Field f : tmp) {
                if (f != null && (f.toString().contains("static") && !acceptStatic)) {
                    continue;
                }
                fields.add(f);
            }
            return fields;
        }
    }

    public static List<Field> getAllFieldIncludingPrivateAndSuper(Class<?> klassc) {
        return getAllFieldIncludingPrivateAndSuper(klassc, false);
    }

    public static List<Field> getAllFieldIncludingPrivateAndSuper(Class<?> klass, boolean acceptStatic) {
        List<Field> fields = new ArrayList<Field>();
        while (!klass.equals(Object.class)) {
            for (Field field : klass.getDeclaredFields()) {
                if (field != null && (field.toString().contains("static") && !acceptStatic)) {
                    continue;
                }
                fields.add(field);
            }
            klass = klass.getSuperclass();
        }
        return fields;
    }
}
