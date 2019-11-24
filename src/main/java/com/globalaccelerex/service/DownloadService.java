package com.globalaccelerex.service;

import com.globalaccelerex.component.AmazonReviewHandler;
import com.globalaccelerex.component.YoutubeCommentHandler;
import com.globalaccelerex.exception.UnsupportedUrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Service
public class DownloadService {
    
    private AmazonReviewHandler amazonReviewHandler;
    private YoutubeCommentHandler youtubeCommentHandler;
    
    @Autowired
    public DownloadService(AmazonReviewHandler amazonReviewHandler, YoutubeCommentHandler youtubeCommentHandler) {
        this.amazonReviewHandler = amazonReviewHandler;
        this.youtubeCommentHandler = youtubeCommentHandler;
    }
    
    public String downloadFile(String externalUrl) throws IOException, ExecutionException, InterruptedException {
        
        if (externalUrl.contains("youtube")) {
            return youtubeCommentHandler.parseComments(externalUrl).get(); 
            
        } else if (externalUrl.contains("amazon")) {
            return amazonReviewHandler.parseReviews(externalUrl);
        
        } else {
            throw new UnsupportedUrlException();
        }
    }
}
