package org.chatapp.event;

public interface EventFileSender {
    void onSending(double percentage);

    void onStartSending();

    void onFinish();
}
