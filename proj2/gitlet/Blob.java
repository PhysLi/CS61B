package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    public String name;
    public String content;

    Blob(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
