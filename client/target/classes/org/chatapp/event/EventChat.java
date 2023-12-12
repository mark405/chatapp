package org.chatapp.event;

import org.chatapp.model.ModelReceiveMessage;
import org.chatapp.model.ModelSendMessage;

public interface EventChat {

    void sendMessage(ModelSendMessage modelSendMessage);
    void receiveMessage(ModelReceiveMessage modelReceiveMessage);
}
