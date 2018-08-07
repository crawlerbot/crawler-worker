package io.github.crawlerbot.models;

public class FileContent {
    private String content;

    public String getContent() {
        return content;
    }

    public FileContent setContent(String content) {
        this.content = content;
        return this;
    }

    public FileContent() {
    }

    @Override
    public String toString() {
        return "FileContent{" +
                "content='" + content + '\'' +
                '}';
    }
}
