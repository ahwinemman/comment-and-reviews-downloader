package com.globalaccelerex.model;

import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class CsvModel {
    
    private String userName;
    
    private String date;
    
    private String starRating;
    
    private String commentsOrReviews;
    
    private String link;
    
}
