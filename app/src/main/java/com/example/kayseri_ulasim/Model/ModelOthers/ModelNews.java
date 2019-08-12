package com.example.kayseri_ulasim.Model.ModelOthers;

public class ModelNews {
    private String image_url;
    private String title;
    private String time;
    private String content;
    private String page_content_url;

    public ModelNews(String image_url, String title, String time, String content, String page_content_url) {
        this.image_url = image_url;
        this.title = title;
        this.time = time;
        this.content = content;
        this.page_content_url = page_content_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getPage_content_url() {
        return page_content_url;
    }
}
