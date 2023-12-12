package org.chatapp.service;

import io.socket.client.IO;
import io.socket.client.Socket;
import org.chatapp.event.EventFileReceiver;
import org.chatapp.event.PublicEvent;
import org.chatapp.model.*;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Service {

    private static Service instance;
    private Socket client;
    private final int PORT_NUMBER = 9999;
    private final String IP = "localhost";
    private ModelUserAccount user;
    private List<ModelFileSender> fileSender;
    private List<ModelFileReceiver> fileReceiver;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    private Service() {
        fileSender = new ArrayList<>();
        fileReceiver = new ArrayList<>();
    }

    public void startServer() {
        try {
            client = IO.socket("http://" + IP + ":" + PORT_NUMBER);
            client.on("list_user", objects -> {
                List<ModelUserAccount> modelUserAccountList = new ArrayList<>();
                for(Object o: objects) {
                    ModelUserAccount modelUserAccount = new ModelUserAccount(o);
                    if (modelUserAccount.getId() != user.getId()) {
                        modelUserAccountList.add(modelUserAccount);
                    }
//                    modelUserAccountList.add(new Model_User_Account(o));
                }
                PublicEvent.getInstance().getEventMenuLeft().newUser(modelUserAccountList);
            });
            client.on("user_status", objects -> {
                int userId = (int) objects[0];
                boolean status = (boolean) objects[1];
                if (status) {
                    PublicEvent.getInstance().getEventMenuLeft().userConnect(userId);
                } else {
                    PublicEvent.getInstance().getEventMenuLeft().userDisconnect(userId);
                }
            });
            client.on("receive_ms", objects -> {
                ModelReceiveMessage modelReceiveMessage = new ModelReceiveMessage(objects[0]);
                PublicEvent.getInstance().getEventChat().receiveMessage(modelReceiveMessage);
            });
            client.open();
        } catch (URISyntaxException e) {
            error(e);
        }
    }
    public ModelFileSender addFile(File file, ModelSendMessage message) throws IOException {
        ModelFileSender data = new ModelFileSender(file, client, message);
        message.setFile(data);
        fileSender.add(data);
        //  For send file one by one
        if (fileSender.size() == 1) {
            data.initSend();
        }
        return data;
    }

    public void fileSendFinish(ModelFileSender data) throws IOException {
        fileSender.remove(data);
        if (!fileSender.isEmpty()) {
            //  Start send new file when old file sending finish
            fileSender.get(0).initSend();
        }
    }
    public void addFileReceiver(int fileID, EventFileReceiver event) throws IOException {
        ModelFileReceiver data = new ModelFileReceiver(fileID, client, event);
        fileReceiver.add(data);
        if (fileReceiver.size() == 1) {
            data.initReceive();
        }
    }

    public Socket getClient() {
        return client;
    }

    private void error(Exception e) {
        System.err.println(e);
    }

    public void setUser(ModelUserAccount user) {
        this.user = user;
    }

    public ModelUserAccount getUser() {
        return user;
    }

    public void fileReceiveFinish(ModelFileReceiver data) throws IOException {
        fileReceiver.remove(data);
        if (!fileReceiver.isEmpty()) {
            fileReceiver.get(0).initReceive();
        }
    }
}
