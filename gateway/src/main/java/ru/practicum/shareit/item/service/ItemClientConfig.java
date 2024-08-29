package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class ItemClientConfig {
    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public ItemClient getItemClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/items"))
                .build();
        return new ItemClient(restTemplate);
    }
}