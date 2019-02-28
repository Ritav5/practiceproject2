package com.second.practiceproject2.model;

import java.util.HashMap;
import java.util.Map;

//ViewObject是传递对象和视图中间的对象
// 通过key set进来，通过get出去,与HomeController连接
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();

    public void set(String key , Object value){
        objs.put(key,value);
    }

    public Object get(String key){
        return objs.get(key);
    }
}
