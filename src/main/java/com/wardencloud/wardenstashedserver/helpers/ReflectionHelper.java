package com.wardencloud.wardenstashedserver.helpers;

import java.lang.reflect.Method;

public class ReflectionHelper {
    public static boolean isGetter(Method method) {
        if (method == null || void.class.equals(method.getReturnType())) {
            return false;
        }
        if (method.getName().startsWith("get")) {
            return true;
        } else {
            return false;
        }
    }
}
