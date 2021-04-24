package com.codingfly.httpclient.model;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import java.util.List;
import java.util.Map;

public class HttpConfig {
    private HttpClient client;
    private String url;
    private HttpMethod method;
    private Map<String, Object> urlParams;
    private Map<String, Object> bodyParams;
    private List<Header> headers;
    private String inenc;
    private String outenc;

    private int connectionRequestTimeout=3000;
    private int connectTimeout=3000;
    private int socketTimeout=3000;

    /**
    new HttpConfig()
            .setClient(client)
            .setUrl(url)
            .setMethod(HttpMethod.GET)
            .setUrlParams(urlParams)
            .setBodyParams(bodyParams)
            .setHeaders(headers)
            .setInenc(inenc)
            .setOutenc(outenc)
            .setConnectionRequestTimeout(10000)
            .setConnectTimeout(10000)
            .setSocketTimeout(24*3600*1000);
     */
    public HttpConfig() { }

    /**
     new HttpConfig(client, HttpMethod.PUT, url)
             .setUrlParams(urlParams)
             .setBodyParams(bodyParams)
             .setHeaders(headers)
             .setInenc(inenc)
             .setOutenc(outenc)
             .setConnectionRequestTimeout(10000)
             .setConnectTimeout(10000)
             .setSocketTimeout(24*3600*1000);
     */
    public HttpConfig(HttpClient client, HttpMethod method, String url) {
        this.client = client;
        this.url = url;
        this.method = method;
    }

    public HttpConfig(HttpMethod method) {
        this.method = method;
    }

    public HttpClient getClient() {
        return client;
    }

    public HttpConfig setClient(HttpClient client) {
        this.client = client;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public HttpConfig setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpConfig setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    public Map<String, Object> getUrlParams() {
        return urlParams;
    }

    public HttpConfig setUrlParams(Map<String, Object> urlParams) {
        this.urlParams = urlParams;
        return this;
    }

    public Map<String, Object> getBodyParams() {
        return bodyParams;
    }

    public HttpConfig setBodyParams(Map<String, Object> bodyParams) {
        this.bodyParams = bodyParams;
        return this;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public HttpConfig setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public String getInenc() {
        return inenc;
    }

    public HttpConfig setInenc(String inenc) {
        this.inenc = inenc;
        return this;
    }

    public String getOutenc() {
        return outenc;
    }

    public HttpConfig setOutenc(String outenc) {
        this.outenc = outenc;
        return this;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public HttpConfig setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public HttpConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public HttpConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

}