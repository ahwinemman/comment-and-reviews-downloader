package com.globalaccelerex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CommentAndReviewsDownloaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommentAndReviewsDownloaderApplication.class, args);
	}

}
