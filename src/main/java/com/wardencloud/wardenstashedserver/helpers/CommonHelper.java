package com.wardencloud.wardenstashedserver.helpers;

import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;

public class CommonHelper {
    public void moveJSONObjectEntries(JSONObject source, JSONObject target) {
        Set<Entry<String, Object>> entries = source.entrySet();
        Iterator<Entry<String, Object>> iterator = entries.iterator();
        while(iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            target.put(entry.getKey(), entry.getValue());
        }
    }
}
