package com.globalaccelerex.config;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.api.services.youtube.YouTube;

import java.io.IOException;

@Configuration
public class YoutubeConfig {
    
    private String API_KEY = "AIzaSyA4Z1dVVQ74jeUDrVu6gGcZmaxbeZL8wHE";
    
    HttpTransport transport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
    
    @Bean
    public YouTube youtube() {
        YouTube youtube = new YouTube.Builder(transport,jsonFactory, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("YoutubeComments")
                .setYouTubeRequestInitializer
                        (new YouTubeRequestInitializer(API_KEY)).build();

        return youtube;
    }
}
