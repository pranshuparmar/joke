package com.pranshu.joke.services;

import com.pranshu.joke.configurations.LogbookConfiguration;
import com.pranshu.joke.exceptions.InvalidRequestException;
import com.pranshu.joke.exceptions.ResourceUnavailableException;
import com.pranshu.joke.models.dtos.*;
import com.pranshu.joke.models.entities.AnalyzeQuery;
import com.pranshu.joke.repositories.AnalyzeQueryRepository;
import com.pranshu.joke.utils.Utility;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JokeService {

    @Value("${url.joke.dad}")
    private String dadJokeUrl;

    @Value("${url.joke.local}")
    private String localJokeUrl;

    @Autowired
    private LogbookConfiguration logbookConfiguration;

    @Autowired
    private AnalyzeQueryRepository analyzeQueryRepository;

    public SearchResponseDTO searchJoke(String accessToken, String query, Integer page) {
        if((StringUtils.isEmpty(query)) || (page != null && page <=0)) {
            log.error("Invalid query parameters");
            throw new InvalidRequestException();
        }

        StringBuilder jokeUrlBuilder = new StringBuilder(dadJokeUrl);
        jokeUrlBuilder.append("term=").append(query);
        if(page != null) {
            jokeUrlBuilder.append("&page=").append(page);
        }
        DadJokeResponseDTO dadJokeResponse = makeJokeCall(jokeUrlBuilder.toString());

        SearchResponseDTO searchResponseDto = new SearchResponseDTO();
        String nextUrl = localJokeUrl +
                "accessToken=" + accessToken +
                "&query=" + query +
                "&page=" + dadJokeResponse.getNextPage();
        searchResponseDto.setResults(dadJokeResponse.getResults());
        searchResponseDto.setNextUrl(nextUrl);
        return searchResponseDto;
    }

    public TokenDTO analyzeQuery(String query) {
        if(StringUtils.isEmpty(query)) {
            log.error("Invalid query string");
            throw new InvalidRequestException();
        }

        TokenDTO tokenDTO = new TokenDTO();
        AnalyzeQuery analyzeQuery = analyzeQueryRepository.findByQuery(query);
        if(analyzeQuery != null) {
            // Returning same token if the query already exists
            tokenDTO.setToken(String.valueOf(analyzeQuery.getId()));
        } else {
            analyzeQuery = new AnalyzeQuery();
            analyzeQuery.setQuery(query);
            analyzeQuery.setStatus("PROGRESS");
            AnalyzeQuery savedAnalyzeQuery = analyzeQueryRepository.save(analyzeQuery);

            // Fetching jokes and performing analysis asynchronously
            CompletableFuture.runAsync(() -> storeAnalysisResult(savedAnalyzeQuery));

            tokenDTO.setToken(String.valueOf(savedAnalyzeQuery.getId()));
        }
        return tokenDTO;
    }

    private void storeAnalysisResult(AnalyzeQuery analyzeQuery) {
        String jokeUrl = dadJokeUrl + "term=" + analyzeQuery.getQuery();
        DadJokeResponseDTO initDadJokeResponse = makeJokeCall(jokeUrl);
        List<ResultDTO> allJokeList = initDadJokeResponse.getResults();

        // Fetching jokes from all the remaining pages and storing in single list
        if(initDadJokeResponse.getTotalJokes() > 0 && initDadJokeResponse.getTotalPages() > 1) {
            for(int i = 2; i <= initDadJokeResponse.getTotalPages(); i++) {
                jokeUrl = dadJokeUrl + "term=" + analyzeQuery.getQuery() + "&page=" + i;
                DadJokeResponseDTO dadJokeResponse = makeJokeCall(jokeUrl.toString());
                allJokeList.addAll(dadJokeResponse.getResults());
            }
        }

        // Creating a sorted map of each word count in ascending order, removed special characters from the words
        Map<String, Long> wordCountMap = allJokeList.stream()
                .map(ResultDTO::getJoke)
                .map(joke -> joke.replaceAll("[^a-zA-Z0-9]", " "))
                .flatMap(joke -> Arrays.stream(joke.split(" ")))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new))
                ;
        String data = wordCountMap.keySet().stream().limit(10).collect(Collectors.joining(","));
        analyzeQuery.setStatus("COMPLETE");
        analyzeQuery.setData(data);
        analyzeQueryRepository.save(analyzeQuery);
    }

    public AnalyzeResponseDTO getAnalysisResult(String token) {
        Long id = null;
        try {
            id = Long.parseLong(token);
        } catch (NumberFormatException ex) {
            throw new InvalidRequestException();
        }

        Optional<AnalyzeQuery> analyzeQueryOptional = analyzeQueryRepository.findById(id);
        if(analyzeQueryOptional.isEmpty()) {
            throw new InvalidRequestException();
        }
        AnalyzeQuery analyzeQuery = analyzeQueryOptional.get();

        AnalyzeResponseDTO analyzeResponseDTO = new AnalyzeResponseDTO();
        analyzeResponseDTO.setStatus(analyzeQuery.getStatus());
        List<String> data = new ArrayList<>();
        if(!StringUtils.isEmpty(analyzeQuery.getData())) {
            data = Arrays.asList(analyzeQuery.getData().split(","));
        }
        analyzeResponseDTO.setData(data);
        return analyzeResponseDTO;
    }

    private DadJokeResponseDTO makeJokeCall(String jokeUrl) {
        RestTemplate restTemplate = logbookConfiguration.getRestTemplate();
        ResponseEntity<DadJokeResponseDTO> jokeResponseEntity = restTemplate.exchange(jokeUrl, HttpMethod.GET, Utility.getExternalRequestEntity(), DadJokeResponseDTO.class);
        if(!jokeResponseEntity.getStatusCode().equals(HttpStatus.OK)) {
            throw new ResourceUnavailableException();
        }
        return jokeResponseEntity.getBody();
    }
}
