package objects;

import java.io.IOException;
import java.util.zip.DataFormatException;

public class GitCommit extends GitObject {

    public GitCommit(String file) throws DataFormatException, IOException {
        super(file);
    }

    public GitCommit() {
        super();
    }

    @Override
    protected String getType() {
        return "commit";
    }

}
