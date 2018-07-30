package com.sainsbury.scraper.service;

import com.sainsbury.scraper.dto.Result;
import com.sainsbury.scraper.dto.Total;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.java.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@Log
public class Service {

    public Total calculateTotal(List<Result> productsInfo) {
        BigDecimal gross = new BigDecimal("0");
        for (Result result : productsInfo){
            gross = gross.add(result.getUnit_price());
        }
        BigDecimal vat = gross.multiply(new BigDecimal("0.2")).setScale(2, RoundingMode.CEILING);
        return new Total(gross,vat);
    }

    public List<Result> getProductsInfo(List<String> productsURL) {
        List<Result> resultList = new ArrayList<>();
        String url = "";
        for(String productUrl: productsURL){
            try {
                url = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk/" + productUrl.substring(18);
                Document doc = Jsoup.connect(url).get();
                Document docString = Jsoup.parse(doc.toString());
                resultList.add(getProductInfo(docString));
            } catch (IOException e) {
                log.warning("ERROR OCCURRED WHILE TRYING TO ACCESS ".concat(url));
            }
        }

        return resultList;
    }

    private Result getProductInfo(Document docString) {
        String title = getTitle(docString);
        BigDecimal unitPrice = getProductUnitPrice(docString);
        String description = getProductDescription(docString);
        String kcal = getProductKcal(docString);
        return new Result(title, getProductKcal(docString).equals("") ? null : new BigDecimal(kcal),unitPrice,description);
    }

    private String getProductKcal(Document docString) {
        String kcal = "";
        if( docString.select("td:contains(kcal)").first() != null){
            kcal = docString.select("td:contains(kcal)").first().childNode(0).toString();
            kcal = kcal.substring(0,kcal.lastIndexOf('k'));
        }else if(docString.select("th:contains(kcal)").first() != null){
            kcal = docString.select("th:contains(kcal)").first().parent().childNode(3).childNode(0).toString();
        }

        return kcal;
    }

    private String getProductDescription(Document docString) {
        String description = "";
        if (docString.select("p.statements").size() == 2){
            description = docString.select("p.statements").first().parentNode().childNode(3).childNode(0).toString();
        }
        else if(docString.select("div.memo").first() != null){
            description = docString.select("div.memo").first().childNode(1).childNode(0).toString();
        }
        else{
            description = docString.select("div.productText").first().childNode(1).childNode(0).toString();
        }
        return description;
    }

    private BigDecimal getProductUnitPrice(Document docString) {
        return new BigDecimal(docString.select("p.pricePerUnit").first().childNode(0).toString().substring(2));
    }

    private String getTitle(Document documentString) {
        return documentString.select("h1").html();
    }

    public List<String> getProductsURL(Document documentString) {
        List<String> urlList = new ArrayList<>();
        Elements nodes = documentString.select("h3");
        nodes.forEach(node -> urlList.add(node.childNode(1).attr("href")));
        return urlList;
    }

}
