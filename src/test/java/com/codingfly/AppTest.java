package com.codingfly;

import static org.junit.Assert.assertTrue;

import com.codingfly.httpclient.model.HttpConfig;
import com.codingfly.httpclient.model.HttpMethod;
import com.codingfly.httpclient.util.HttpClientUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

public class AppTest {
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void test() {
        HttpClient httpClient = HttpClients.custom().build();
        HttpConfig config = new HttpConfig()
                .setClient(httpClient)
                .setUrl("https://www.baidu.com/")
                .setMethod(HttpMethod.GET)
                .setConnectionRequestTimeout(10000)
                .setConnectTimeout(10000)
                .setSocketTimeout(24*3600*1000);

        System.out.println(HttpClientUtils.get(config).getResult());

    }
}
