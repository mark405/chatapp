package org.chatapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelPackageSender {
    private int fileID;
    private byte[] data;
    private boolean finish;

    public JSONObject toJSONObject() {
        try {
            JSONObject json = new JSONObject();
            json.put("fileID", fileID);
            json.put("data", data);
            json.put("finish", finish);
            return json;
        } catch (JSONException e) {
            return null;
        }
    }
}
