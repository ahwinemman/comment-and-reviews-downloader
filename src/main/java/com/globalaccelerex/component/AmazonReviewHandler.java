package com.globalaccelerex.component;

import com.globalaccelerex.exception.NoReviewsException;
import com.globalaccelerex.model.CsvModel;
import com.globalaccelerex.util.CsvUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class AmazonReviewHandler {
    
    
 
    @Async
    public String parseReviews(String externalUrl) throws IOException {
        
        List<CsvModel> csvModels = new ArrayList<>();
        
        Document doc = Jsoup.connect(externalUrl).get();
        
        Elements elements = doc.select(".aok-relative");

        if (elements == null || elements.isEmpty()) {
            throw new NoReviewsException();
        }

        for (Element element: elements) {
            Element userNameElem = element.select(".a-profile-name").first();
            Element dateElem = element.select(".review-date").first();
            Element starRatingElem = element.select(".review-rating").first();
            Element reviewsElem = element.select(".review-text-content").first();

            String numOfStars = "0";

            Set<String> starRatingClassNames = starRatingElem.classNames();
            for (String classNam: starRatingClassNames) {
                if (classNam.startsWith("a-star")) {
                    String[] splitClassNam = classNam.split("-");
                    numOfStars = splitClassNam[3];
                }
            }

            CsvModel csvModel = CsvModel.builder()
                                    .userName(userNameElem.text())
                                    .date(dateElem.text())
                                    .starRating(numOfStars)
                                    .commentsOrReviews(reviewsElem.text())
                                    .link("")
                                    .build();

            csvModels.add(csvModel);                  

        }
        return CsvUtil.createCSVFile(csvModels);
        
    }
    
}
