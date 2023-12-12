package org.chatapp.model;

import org.chatapp.app.MessageType;
import org.json.JSONException;
import org.json.JSONObject;

public class ModelReceiveMessage {
    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public ModelReceiveMessage(int fromUserId, String text, MessageType messageType) {
        this.fromUserId = fromUserId;
        this.text = text;
        this.messageType = messageType;
    }

    public int getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ModelReceiveMessage() {
    }

    public ModelReceiveImage getDataImage() {
        return dataImage;
    }

    public void setDataImage(ModelReceiveImage dataImage) {
        this.dataImage = dataImage;
    }

    int fromUserId;
    private String text;
    private MessageType messageType;
    private ModelReceiveImage dataImage;

    public ModelReceiveMessage(Object json) {
        JSONObject obj = (JSONObject) json;
        try {
            messageType = MessageType.toMessageType(obj.getInt("messageType"));
            fromUserId = obj.getInt("fromUserId");
            text = obj.getString("text");
            if (!obj.isNull("dataImage")) {
                dataImage = new ModelReceiveImage(obj.get("dataImage"));
            }
        } catch (JSONException e) {
            System.err.println(e.getMessage());
        }
    }
    public JSONObject toJsonObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("messageType", messageType.getValue());
            json.put("fromUserID", fromUserId);
            json.put("text", text);
            if (dataImage != null) {
                json.put("dataImage", dataImage.toJSONObject());
            }
            return json;
        } catch (JSONException e) {
            return null;
        }
    }
}

