package gitlet;

import java.io.Serializable;

public class Blob implements Serializable {
    private String name;
    private String content;

    Blob(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
