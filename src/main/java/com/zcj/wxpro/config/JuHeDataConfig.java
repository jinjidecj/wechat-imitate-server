package com.zcj.wxpro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "juhe")
public class JuHeDataConfig {
    private String newsAppKey;
    private String newsUrl;

    public String getNewsAppKey() {
        return newsAppKey;
    }

    public void setNewsAppKey(String newsAppKey) {
        this.newsAppKey = newsAppKey;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
}
