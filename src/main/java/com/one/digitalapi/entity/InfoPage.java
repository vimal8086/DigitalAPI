package com.one.digitalapi.entity;

public class InfoPage {
    private String key;
    private String title;
    private String content;

    public InfoPage() {}

    public InfoPage(String key, String title, String content) {
        this.key = key;
        this.title = title;
        this.content = content;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}