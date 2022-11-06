package com.pranshu.joke.models.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DadJokeResponseDTO {
    @JsonProperty("current_page")
    private Integer current_page;

    private Integer limit;

    @JsonProperty("next_page")
    private Integer nextPage;

    @JsonProperty("previous_page")
    private Integer previousPage;

    private List<ResultDTO> results = new ArrayList<>();

    @JsonProperty("search_term")
    private String searchTerm;

    private Integer status;

    @JsonProperty("total_jokes")
    private Integer totalJokes;

    @JsonProperty("total_pages")
    private Integer totalPages;
}
