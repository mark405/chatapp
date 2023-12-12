package org.server.model;

import com.corundumstudio.socketio.SocketIOClient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelClient {
    private ModelUserAccount modelUserAccount;
    private SocketIOClient socketIOClient;
}
