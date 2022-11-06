package com.pranshu.joke.models.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchResponseDTO {
    private List<ResultDTO> results = new ArrayList<>();
    private String nextUrl;
}
