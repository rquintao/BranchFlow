package objects;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.branchflow.RepositoryHelper;

public abstract class GitObject {

    private byte[] deserializedData;
    private byte[] serializedData;

    public GitObject() {
    }

    protected abstract String getType();

    public GitObject(String file) throws DataFormatException, IOException {
        deserializedData = this.decompressZlibCompressedContent(file);
    }

    public byte[] decompressZlibCompressedContent(String file) throws DataFormatException, IOException {

        byte[] allBytes = Files
                .readAllBytes(Paths.get(".git/objects/" + file.substring(0, 2) + "/" + file.substring(2)));

        Inflater inflater = new Inflater();
        inflater.setInput(allBytes);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        inflater.end();
        return outputStream.toByteArray();
    }

    public String zlibCompress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];

        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        deflater.end();
        return outputStream.toString();
    }

    public void saveToDisk(String basePath) throws IOException {
        String serialString = new String(this.serializedData, StandardCharsets.UTF_8);
        File dir = new File(basePath + "/" + serialString.substring(0, 2));
        dir.mkdirs();
        RepositoryHelper.writeToFile(dir.getPath(), this.zlibCompress(this.serializedData));
    }

    public byte[] serialize(byte[] deserializedData) throws NoSuchAlgorithmException {
        String header = this.getType() + " " + deserializedData.length + "\0";
        byte[] headerBytes = header.getBytes(StandardCharsets.UTF_8);
        byte[] fullByteArray = new byte[headerBytes.length + deserializedData.length];
        this.serializedData = computeSha1(fullByteArray);
        return this.serializedData;
    }

    @Override
    public String toString() {
        return deserializedData != null ? new String(extractContent(this.deserializedData), StandardCharsets.UTF_8)
                : null;
    }

    private static byte[] extractContent(byte[] blob) {
        int nullIndex = -1;

        // Find the position of the null byte (\0)
        for (int i = 0; i < blob.length; i++) {
            if (blob[i] == 0) {
                nullIndex = i;
                break;
            }
        }

        if (nullIndex == -1) {
            throw new IllegalArgumentException("Null byte separator not found in blob.");
        }

        // Extract the size string and parse it
        String sizeStr = new String(blob, 0, nullIndex, StandardCharsets.UTF_8);
        int size = Integer.parseInt(sizeStr.split(" ")[1]);

        // Extract the content
        int contentStart = nullIndex + 1;
        if (contentStart + size > blob.length) {
            throw new IllegalArgumentException("Declared size exceeds blob length.");
        }

        return Arrays.copyOfRange(blob, contentStart, contentStart + size);
    }

    private static byte[] computeSha1(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        return sha1.digest(data);
    }

}
