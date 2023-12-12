package org.chatapp.event;

import java.io.File;

public interface EventFileReceiver {
     void onReceiving(double percentage);

     void onStartReceiving();

     void onFinish(File file);
}
