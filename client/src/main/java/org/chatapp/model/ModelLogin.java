package org.chatapp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelLogin {
    String username;
    String password;
    public JSONObject toJSONOBJECT() {

        try {
            JSONObject obj = new JSONObject();
            obj.put("username", username);
            obj.put("password", password);
            return obj;
        } catch (JSONException e) {
            return null;
        }

    }
}
