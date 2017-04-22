package edu.iit.xwu64.hw6_news_gateway;

import java.io.Serializable;

/**
 * Created by xiaoliangwu on 2017/4/19.
 */

public class Article implements Serializable {
    private String author;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishedAt;
    private int total;
    private int index;

    public Article(String author, String title, String description, String url, String urlToImage, String publishedAt, int total, int index) {
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.total = total;
        this.index = index+1;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public int getTotal() {
        return total;
    }

    public int getIndex() {
        return index;
    }
}
