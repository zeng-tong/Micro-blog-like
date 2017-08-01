package com.zengtong.Async.handler;


import com.zengtong.Async.EventHandler;
import com.zengtong.Async.EventModel;
import com.zengtong.Async.EventType;
import com.zengtong.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component

public class MessageHandler implements EventHandler {


    @Autowired
    private MessageService messageService;

    @Override
    public void doHandler(EventModel model) {


        messageService.addMessage("您有一个新的粉丝",model.getFrom_id(),model.getTo_id());

    }

    @Override
    public List<EventType> getSupportType() {
        return Arrays.asList(EventType.MESSAGE);
    }
}
