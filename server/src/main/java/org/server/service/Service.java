package org.server.service;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import org.server.MessageType;
import org.server.model.*;

import javax.swing.JTextArea;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Service {

    private static Service instance;
    private SocketIOServer server;

    public List<ModelClient> getClients() {
        return clients;
    }

    private ServiceUser serviceUser;
    private ServiceFile serviceFile;
    private List<ModelClient> clients;
    private JTextArea textArea;
    private final int PORT_NUMBER = 9999;

    public static Service getInstance(JTextArea textArea) {
        if (instance == null) {
            instance = new Service(textArea);
        }
        return instance;
    }

    private Service(JTextArea textArea) {
        this.textArea = textArea;
        serviceUser = new ServiceUser();
        serviceFile = new ServiceFile();
        clients = new ArrayList<>();
    }

    public void startServer() {
        Configuration config = new Configuration();
        config.setPort(PORT_NUMBER);
        server = new SocketIOServer(config);
        server.addConnectListener(sioc -> textArea.append("One client connected\n"));
        server.addEventListener("register", ModelRegister.class, (sioc, t, ar) -> {
            ModelMessage message = serviceUser.register(t);
            ar.sendAckData(message.isAction(), message.getMessage(), message.getData());
            if (message.isAction()) {
                textArea.append("User has Register :" + t.getUserName() + " Pass :" + t.getPassword() + "\n");
                server.getBroadcastOperations().sendEvent("list_user", message.getData());
                addClient(sioc, (ModelUserAccount) message.getData());
            }
        });
        server.addEventListener("list_user", Integer.class, (socketIOClient, id, ackRequest) -> {
            List<ModelUserAccount> list = serviceUser.getUser(id);
            socketIOClient.sendEvent("list_user", list.toArray());
        });
        server.addEventListener("login", ModelLogin.class, (socketIOClient, modelLogin, ackRequest) -> {
            ModelMessage message = serviceUser.login(modelLogin);
            Object responseObject =  message.getData();
            if(responseObject != null) {
                ModelUserAccount response = (ModelUserAccount) message.getData();
                addClient(socketIOClient, response);
                userConnect(response.getId());
            }
            ackRequest.sendAckData(message.isAction(), message.getMessage(), responseObject);

        });
        server.addEventListener("send_to_user", ModelSendMessage.class, new DataListener<ModelSendMessage>() {
            @Override
            public void onData(SocketIOClient sioc, ModelSendMessage t, AckRequest ar) throws Exception {
                sendToClient(t, ar);
            }
        });
        server.addEventListener("send_file", ModelPackageSender.class, (sioc, t, ar) -> {
            try {
                serviceFile.receiveFile(t);
                if (t.isFinish()) {
                    ar.sendAckData(true);
                    ModelReceiveImage dataImage = new ModelReceiveImage();
                    dataImage.setFileID(t.getFileID());
                    ModelSendMessage message = serviceFile.closeFile(dataImage);
                    //  Send to client 'message'
                    sendTempFileToClient(message, dataImage);

                } else {
                    ar.sendAckData(true);
                }
            } catch (IOException | SQLException e) {
                ar.sendAckData(false);
                e.printStackTrace();
            }
        });
        server.addEventListener("get_file", Integer.class, (sioc, t, ar) -> {
            ModelFile file = serviceFile.initFile(t);
            long fileSize = serviceFile.getFileSize(t);
            ar.sendAckData(file.getFileExtension(), fileSize);
        });
        server.addEventListener("request_file", ModelRequestFile.class, (sioc, t, ar) -> {
            byte[] data = serviceFile.getFileData(t.getCurrentLength(), t.getFileID());
            if (data != null) {
                ar.sendAckData(data);
            } else {
                ar.sendAckData();
            }
        });
        server.addDisconnectListener(socketIOClient -> {
            int id = removeClient(socketIOClient);
            if (id != 0) {
                userDisconnect(id);
            }
        });
        server.start();
        textArea.append("Server has Start on port : " + PORT_NUMBER + "\n");
    }

    private void addClient(SocketIOClient socketIOClient, ModelUserAccount modelUserAccount) {
        clients.add(new ModelClient(modelUserAccount, socketIOClient));
    }
    private void userConnect(int userId) {
        server.getBroadcastOperations().sendEvent("user_status", userId, true);
    }
    private void userDisconnect(int userId) {
        server.getBroadcastOperations().sendEvent("user_status", userId, false);
    }
    public int removeClient(SocketIOClient socketIOClient) {
        for (ModelClient client : clients) {
            if (client.getSocketIOClient() == socketIOClient) {
                clients.remove(client);
                return client.getModelUserAccount().getId();
            }
        }

        return 0;
    }
    private void sendToClient(ModelSendMessage modelSendMessage, AckRequest ar) {
        if (modelSendMessage.getMessageType() == MessageType.IMAGE.getValue() || modelSendMessage.getMessageType() == MessageType.FILE.getValue()) {
            try {
                ModelFile file = serviceFile.addFileReceiver(modelSendMessage.getText());
                serviceFile.initFile(file, modelSendMessage);
                ar.sendAckData(file.getFileID());
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
        } else {
            for (ModelClient c : clients) {
                if (c.getModelUserAccount().getId() == modelSendMessage.getToUserID()) {
                    c.getSocketIOClient().sendEvent("receive_ms", new ModelReceiveMessage(modelSendMessage.getMessageType(), modelSendMessage.getFromUserID(), modelSendMessage.getText(), null));
                    break;
                }
            }
        }
    }
    private void sendTempFileToClient(ModelSendMessage data, ModelReceiveImage dataImage) {
        for (ModelClient c : clients) {
            if (c.getModelUserAccount().getId() == data.getToUserID()) {
                c.getSocketIOClient().sendEvent("receive_ms", new ModelReceiveMessage(data.getMessageType(), data.getFromUserID(), data.getText(), dataImage));
                break;
            }
        }
    }
}
