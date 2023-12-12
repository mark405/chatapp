package org.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelFileSender {
    private ModelFile data;
    private File file;
    private RandomAccessFile accFile;
    private long fileSize;
    public ModelFileSender(ModelFile data, File file) throws IOException {
        this.data = data;
        this.file = file;
        this.accFile = new RandomAccessFile(file, "r");
        this.fileSize = accFile.length();
    }

    public byte[] read(long currentLength) throws IOException {
        accFile.seek(currentLength);
        if (currentLength != fileSize) {
            int max = 2000;
            long length = currentLength + max >= fileSize ? fileSize - currentLength : max;
            byte[] b = new byte[(int) length];
            accFile.read(b);
            return b;
        } else {
            return null;
        }
    }
}
