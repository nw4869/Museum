package com.nightwind.museum.bean;

/**
 * Created by nightwind on 15/3/21.
 */
public class Article {

    private String title;

    private long dateTime;

    private String content;

    private int classify;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClassify() {
        return classify;
    }

    public void setClassify(int classify) {
        this.classify = classify;
    }

    public Article() {
    }

    public Article(String title, String content) {
        this.content = content;
        this.title = title;
    }
}
