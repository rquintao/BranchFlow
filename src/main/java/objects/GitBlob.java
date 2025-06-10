package objects;

public class GitBlob extends GitObject {

    @Override
    protected String getType() {
        return "blob";
    }

}
