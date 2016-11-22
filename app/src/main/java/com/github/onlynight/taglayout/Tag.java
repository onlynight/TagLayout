package com.github.onlynight.taglayout;

/**
 * Created by lion on 2016/11/22.
 */

public class Tag {

    private int id;
    private String content;

    public Tag() {
    }

    public Tag(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
