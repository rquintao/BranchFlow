package objects;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class GitBlob extends GitObject {

    public GitBlob(String file) throws DataFormatException, IOException {
        super(file);
    }

    public GitBlob() {
        super();
    }

    @Override
    protected String getType() {
        return "blob";
    }

}
