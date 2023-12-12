package org.server.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModelReceiveMessage {
    public ModelReceiveMessage(int messageType, int fromUserID, String text, ModelReceiveImage dataImage) {
        this.messageType = messageType;
        this.fromUserId = fromUserID;
        this.text = text;
        this.dataImage = dataImage;
    }
    private int messageType;
    int fromUserId;
    private String text;
    private ModelReceiveImage dataImage;


}

