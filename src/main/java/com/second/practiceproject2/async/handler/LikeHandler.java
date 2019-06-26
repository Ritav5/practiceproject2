package com.second.practiceproject2.async.handler;

import com.second.practiceproject2.async.EventHandler;
import com.second.practiceproject2.async.EventModel;
import com.second.practiceproject2.async.EventType;
import com.second.practiceproject2.model.Message;
import com.second.practiceproject2.model.User;
import com.second.practiceproject2.service.MessageService;
import com.second.practiceproject2.service.UserService;
import com.second.practiceproject2.util.AnswerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Component
public class LikeHandler implements EventHandler {
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel model) {
        Message message = new Message();
        message.setFromId(AnswerUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        message.setContent("用户" + user.getName()
                + "赞了你的评论,http://127.0.0.1:8080/question/" + model.getExt("questionId"));

        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
