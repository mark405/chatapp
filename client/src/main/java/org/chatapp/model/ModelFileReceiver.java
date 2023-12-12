package org.chatapp.model;

import io.socket.client.Ack;
import io.socket.client.Socket;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chatapp.event.EventFileReceiver;
import org.chatapp.service.Service;
import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelFileReceiver {
    public ModelFileReceiver(int fileID, Socket socket, EventFileReceiver event) {
        this.fileID = fileID;
        this.socket = socket;
        this.event = event;
    }

    private int fileID;
    private File file;
    private long fileSize;
    private String fileExtention;
    private RandomAccessFile accFile;
    private Socket socket;
    private EventFileReceiver event;
    private final String PATH_FILE = "/Users/markzavgorodniy/IdeaProjects/ChatAppl/client/client_data/";

    public void initReceive() {
        socket.emit("get_file", fileID, (Ack) os -> {
            if (os.length > 0) {
                try {
                    fileExtention = os[0].toString();
                    fileSize = (int) os[1];
                    file = new File(PATH_FILE + fileID + fileExtention);
                    accFile = new RandomAccessFile(file, "rw");
                    event.onStartReceiving();
                    //  start save file
                    startSaveFile();
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void startSaveFile() throws IOException, JSONException {
        ModelRequestFile data = new ModelRequestFile(fileID, accFile.length());
        socket.emit("request_file", data.toJsonObject(), (Ack) os -> {
            try {
                if (os.length > 0) {
                    byte[] b = (byte[]) os[0];
                    writeFile(b);
                    event.onReceiving(getPercentage());
                    startSaveFile();
                } else {
                    close();
                    event.onFinish(new File(PATH_FILE + fileID + fileExtention));
                    //  remove list
                    Service.getInstance().fileReceiveFinish(ModelFileReceiver.this);
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        });
    }

    private synchronized long writeFile(byte[] data) throws IOException {
        accFile.seek(accFile.length());
        accFile.write(data);
        return accFile.length();
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
}
