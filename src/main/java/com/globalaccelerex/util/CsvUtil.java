package com.globalaccelerex.util;

import com.globalaccelerex.model.CsvModel;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class CsvUtil {

    private static String[] HEADERS = {"UserName", "Date", "Star rating", "Review or Comment", "Link"};
    private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);


    public static String createCSVFile(List<CsvModel> csvModels) throws IOException {
        
        String fileId = UUID.randomUUID().toString();
        
        String fileName = "download" + fileId + ".csv";
        
        FileWriter out = new FileWriter(fileName);
        
        CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT
                .withHeader(HEADERS));
        csvModels.forEach((csvModel) -> {
            try {
                printer.printRecord(csvModel.getUserName(), csvModel.getDate(),
                        csvModel.getStarRating(), csvModel.getCommentsOrReviews(), csvModel.getLink());
            } catch (IOException e) {
                logger.error("Error while parsing record {} comments", csvModel.getUserName());
            }
        });
        printer.flush();
        
        return fileName;
    }
}
