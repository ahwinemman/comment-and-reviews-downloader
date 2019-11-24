package com.globalaccelerex.component;

import com.globalaccelerex.exception.NoCommentsException;
import com.globalaccelerex.exception.UnsupportedUrlException;
import com.globalaccelerex.model.CsvModel;
import com.globalaccelerex.util.CsvUtil;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.CommentSnippet;
import com.google.api.services.youtube.model.CommentThread;
import com.google.api.services.youtube.model.CommentThreadListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Component
public class YoutubeCommentHandler {
    
    private YouTube youTube;
    
    private String SNIPPET = "snippet";
    private String PLAIN_TEXT ="plainText";
    
    @Autowired
    public YoutubeCommentHandler(YouTube youTube) {
        this.youTube = youTube;
    }
    
    private String getVideoId(URL url) throws IOException{
        String queryString = url.getQuery();
        String[] querySplit = queryString.split("=");

        String videoId = "";

        if (querySplit[0].equalsIgnoreCase("v")) {
            videoId = querySplit[1];
        } else {
            throw new MalformedURLException();
        }
        
        return videoId;
    }
    
    @Async("threadPoolTaskExecutor")
    public Future<String> parseComments(String externalUrl) throws IOException {
        
        URL url = new URL(externalUrl);
        if (url == null) {
            throw new UnsupportedUrlException();    
        }
        
        String videoId = getVideoId(url);        

        CommentThreadListResponse videoCommentsListResponse = youTube.commentThreads()
                .list(SNIPPET).setMaxResults(100L).setVideoId(videoId).setModerationStatus("published").setTextFormat(PLAIN_TEXT).execute();

        List<CsvModel> csvModels = new ArrayList<>();
        
        while (true) {
                        
            csvModels.addAll(handleCommentsThreads(videoCommentsListResponse.getItems()));

            String nextPageToken = videoCommentsListResponse.getNextPageToken();
            
            if (nextPageToken == null) {
                break;
            }

            videoCommentsListResponse = youTube.commentThreads()
                    .list(SNIPPET).setMaxResults(100L).setVideoId(videoId).setTextFormat(PLAIN_TEXT)
                    .setPageToken(nextPageToken).execute();
            
        }
        
        return CompletableFuture.completedFuture(CsvUtil.createCSVFile(csvModels));
    }
    
    private List<CsvModel> handleCommentsThreads(List<CommentThread> videoComments) {
        if (videoComments.isEmpty()) {
            throw new NoCommentsException();
        }

        List<CsvModel> csvModels = new ArrayList<>();

        for (CommentThread videoComment : videoComments) {
            CommentSnippet snippet = videoComment.getSnippet().getTopLevelComment()
                    .getSnippet();

            CsvModel csvModel = CsvModel.builder()
                    .userName(snippet.getAuthorDisplayName())
                    .commentsOrReviews(snippet.getTextDisplay())
                    .date(snippet.getPublishedAt().toString())
                    .build();

            csvModels.add(csvModel);
        }
        return csvModels;
    }
}
