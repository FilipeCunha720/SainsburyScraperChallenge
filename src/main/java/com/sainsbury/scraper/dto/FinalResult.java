package com.sainsbury.scraper.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FinalResult {

    List<Result> results;
    Total total;

}
