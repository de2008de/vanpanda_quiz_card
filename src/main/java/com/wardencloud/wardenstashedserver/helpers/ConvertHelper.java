package com.wardencloud.wardenstashedserver.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertHelper {
    public static <T> List<T> castList(Class<? extends T> clazz, List<?> list) {
        List<T> castedList = new ArrayList<>(list.size());
        for (Object item : list) {
            castedList.add(clazz.cast(item));
        }
        return castedList;
    }

    public static <K, V> List<Map<K, V>> castListOfMap(Class<K> keyClass, Class<V> valueClass, List<?> list) {
        List<Map<K, V>> castedList = new ArrayList<>();
        for (Object item : list) {
            Map<?, ?> map = (Map<?, ?>) item;
            Map<K, V> castedMap = new HashMap<>();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                castedMap.put(keyClass.cast(entry.getKey()), valueClass.cast(entry.getValue()));
            }
            castedList.add(castedMap);
        } 
        return castedList;
    } 
}
