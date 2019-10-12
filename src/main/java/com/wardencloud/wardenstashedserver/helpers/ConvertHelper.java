package com.wardencloud.wardenstashedserver.helpers;

import java.util.ArrayList;
import java.util.List;

public class ConvertHelper {
    public static <T> List<T> castList(Class<? extends T> clazz, List<?> list) {
        List<T> castedList = new ArrayList<>(list.size());
        for (Object item : list) {
            castedList.add(clazz.cast(item));
        }
        return castedList;
    }
}
