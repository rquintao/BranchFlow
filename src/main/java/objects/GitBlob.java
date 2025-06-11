package objects;

import java.util.zip.DataFormatException;

public class GitBlob extends GitObject {

    public GitBlob(byte[] data) throws DataFormatException {
        super(data);
    }

    public GitBlob() {
        super();
    }

    @Override
    protected String getType() {
        return "blob";
    }

}
