package com.zcj.wxpro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wxconfig")
public class WXConfig {
    private String appId;
    private String appSecret;
    private String grantType;
    private String loginUrl;
    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    @Override
    public String toString() {
        return "WXConfig{" +
                "appId='" + appId + '\'' +
                ", appSecret='" + appSecret + '\'' +
                ", grantType='" + grantType + '\'' +
                ", loginUrl='" + loginUrl + '\'' +
                '}';
    }
}
