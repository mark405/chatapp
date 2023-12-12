package org.chatapp.model;

import io.socket.client.Ack;
import io.socket.client.Socket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chatapp.event.EventFileSender;
import org.chatapp.service.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelFileSender {
    private ModelSendMessage message;
    private int fileID;
    private String fileExtensions;
    private File file;
    private long fileSize;
    private RandomAccessFile accFile;
    private Socket socket;
    private EventFileSender eventFileSender;

    public ModelFileSender(File file, Socket socket, ModelSendMessage message) throws IOException {
        accFile = new RandomAccessFile(file, "r");
        this.file = file;
        this.socket = socket;
        this.message = message;
        fileExtensions = getExtensions(file.getName());
        fileSize = accFile.length();
    }

    public void initSend() {
        System.out.println("Init file to server and wait server response back");
        socket.emit("send_to_user", message.toJsonObject(), new Ack() {
            @Override
            public void call(Object... os) {
                if (os.length > 0) {
                    int fileID = (int) os[0];
                    try {
                        startSend(fileID);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void sendingFile() throws IOException {
        ModelPackageSender data = new ModelPackageSender();
        data.setFileID(fileID);
        byte[] bytes = readFile();
        if (bytes != null) {
            data.setData(bytes);
            data.setFinish(false);
        } else {
            data.setFinish(true);
            close();
        }
        socket.emit("send_file", data.toJSONObject(), (Ack) os -> {
            if (os.length > 0) {
                boolean act = (boolean) os[0];
                if (act) {
                    try {
                        if (!data.isFinish()) {
                            if (eventFileSender != null) {
                                eventFileSender.onSending(getPercentage());
                            }
                            sendingFile();
                        } else {
                            //  File send finish
                            Service.getInstance().fileSendFinish(ModelFileSender.this);
                            if (eventFileSender != null) {
                                eventFileSender.onFinish();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public double getPercentage() throws IOException {
        double percentage;
        long filePointer = accFile.getFilePointer();
        percentage = filePointer * 100 / fileSize;
        return percentage;
    }

    public void close() throws IOException {
        accFile.close();
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    public void startSend(int fileID) throws IOException {
        this.fileID = fileID;
        if (eventFileSender != null) {
            eventFileSender.onStartSending();
        }
        sendingFile();
    }
    public synchronized byte[] readFile() throws IOException {
        long filepointer = accFile.getFilePointer();
        if (filepointer != fileSize) {
            int max = 2000;
            long length = filepointer + max >= fileSize ? fileSize - filepointer : max;
            byte[] data = new byte[(int) length];
            accFile.read(data);
            return data;
        } else {
            return null;
        }
    }

    public void addEvent(EventFileSender eventFileSender) {
        this.eventFileSender = eventFileSender;
    }
}
