package org.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelPackageSender {
    private int fileID;
    private byte[] data;
    private boolean finish;
}
