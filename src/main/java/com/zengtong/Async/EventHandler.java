package com.zengtong.Async;

import java.util.List;

public interface EventHandler {

    public void doHandler(EventModel model);

    public List<EventType> getSupportType();


}
