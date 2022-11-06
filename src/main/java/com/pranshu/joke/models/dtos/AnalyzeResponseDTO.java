package com.pranshu.joke.models.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnalyzeResponseDTO {
    private String status;
    private List<String> data = new ArrayList<>();
}
