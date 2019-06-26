package com.second.practiceproject2.async;

import com.alibaba.fastjson.JSONObject;
import com.second.practiceproject2.util.JedisAdapter;
import com.second.practiceproject2.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel) {
        try {
            //把事件保存到队列里面
            //序列化与反序列化
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();

            jedisAdapter.lpush(key, json);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
