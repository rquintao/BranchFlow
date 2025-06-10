package objects;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public abstract class GitObject {

    private byte[] deserializedData;

    public GitObject() {
    }

    protected abstract String getType();

    public GitObject(byte[] data) throws DataFormatException {
        deserializedData = this.deserialize(data);
    }

    public byte[] deserialize(byte[] compressedData) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(compressedData);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        inflater.end();
        return outputStream.toByteArray();
    }

    public byte[] serialize(byte[] deserializedData) throws NoSuchAlgorithmException {
        String header = this.getType() + " " + deserializedData.length + "\0";
        byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
        byte[] fullByteArray = new byte[headerBytes.length + deserializedData.length];
        return computeSha1(fullByteArray);
    }

    @Override
    public String toString() {
        return deserializedData != null ? extractContent().toString() : null;
    }

    private byte[] extractContent() {
        int nullIndex = -1;

        // Find the position of the null byte (\0)
        for (int i = 0; i < this.deserializedData.length; i++) {
            if (this.deserializedData[i] == 0) {
                nullIndex = i;
                break;
            }
        }

        if (nullIndex == -1) {
            throw new IllegalArgumentException("Null byte separator not found in blob.");
        }

        // Extract the size string and parse it
        String sizeStr = new String(this.deserializedData, 0, nullIndex, StandardCharsets.UTF_8);
        int size = Integer.parseInt(sizeStr.split(" ")[1]);

        // Extract the content
        int contentStart = nullIndex + 1;
        if (contentStart + size > this.deserializedData.length) {
            throw new IllegalArgumentException("Declared size exceeds blob length.");
        }

        return Arrays.copyOfRange(this.deserializedData, contentStart, contentStart + size);
    }

    private static byte[] computeSha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        return sha1.digest(data);
    }

}
