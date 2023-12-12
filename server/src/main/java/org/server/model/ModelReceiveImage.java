package org.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelReceiveImage {

    private int fileID;
    private String image;
    private int width;
    private int height;

}
