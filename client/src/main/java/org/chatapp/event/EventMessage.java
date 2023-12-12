package org.chatapp.event;

import org.chatapp.model.ModelMessage;

public interface EventMessage {

    void callMessage(ModelMessage message);
}
