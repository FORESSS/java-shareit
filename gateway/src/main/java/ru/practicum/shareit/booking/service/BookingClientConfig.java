package ru.practicum.shareit.booking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class BookingClientConfig {
    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public BookingClient getBookingClient(RestTemplateBuilder builder) {
        var restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + "/bookings"))
                .build();
        return new BookingClient(restTemplate);
    }
}