package edu.iit.xwu64.hw6_news_gateway;

/**
 * Created by xiaoliangwu on 2017/4/16.
 */

public class NewsSource {
    private String id;
    private String name;
    private String url;
    private String category;

    public NewsSource(String id, String name, String url, String category) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }
}
