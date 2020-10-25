package com.example.task.configuration;

import lombok.Setter;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties("elasticsearch")
public class ElasticsearchConfiguration {

    private static final Long FIVE_SECOND = TimeUnit.SECONDS.toMillis(5L);

    @Setter
    private List<String> urls = new ArrayList<>();

    @Setter
    private String port;

    @Bean
    public RestHighLevelClient elasticSearchClient() {
        HttpHost[] hosts = new HttpHost[urls.size()];
        for (int i = 0; i < urls.size(); i++) {
            hosts[i] = new HttpHost(urls.get(i), Integer.parseInt(port));
        }
        RestClientBuilder.RequestConfigCallback requestConfigCallback = requestConfigBuilder -> requestConfigBuilder
                .setConnectionRequestTimeout(0)
                .setSocketTimeout(FIVE_SECOND.intValue())
                .setConnectTimeout(FIVE_SECOND.intValue());

        RestClientBuilder.HttpClientConfigCallback httpClientConfigCallback = httpClientBuilder -> httpClientBuilder
                .setMaxConnTotal(60)
                .setMaxConnPerRoute(20);

        RestClientBuilder builder = RestClient.builder(hosts)
                .setRequestConfigCallback(requestConfigCallback)
                .setHttpClientConfigCallback(httpClientConfigCallback);

        return new RestHighLevelClient(builder);
    }
}
