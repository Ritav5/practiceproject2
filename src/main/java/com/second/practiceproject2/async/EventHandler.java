package com.second.practiceproject2.async;

import java.util.List;


public interface EventHandler {
    //处理event
    void doHandle(EventModel model);

    //获得这个handler关注哪几个event，注册自己
    List<EventType> getSupportEventTypes();
}
