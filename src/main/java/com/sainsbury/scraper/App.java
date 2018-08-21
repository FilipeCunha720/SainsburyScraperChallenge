package com.sainsbury.scraper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sainsbury.scraper.dto.FinalResult;
import com.sainsbury.scraper.dto.Result;
import com.sainsbury.scraper.dto.Total;
import com.sainsbury.scraper.service.Service;
import java.util.List;
import lombok.extern.java.Log;
import org.jsoup.nodes.Document;

@Log
public class App {

    private static Service service = new Service();

    public static void main(String[] args) {
        Document docString = service.obtainDocument();

        List<String> productsURL = service.getProductsURL(docString);

        List<Result> productsInfo = service.getProductsInfo(productsURL);
        Total total = service.calculateTotal(productsInfo);

        FinalResult finalResult = new FinalResult(productsInfo, total);
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
        log.info(gson.toJson(finalResult));
    }

}
