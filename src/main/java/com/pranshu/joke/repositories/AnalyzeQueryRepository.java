package com.pranshu.joke.repositories;

import com.pranshu.joke.models.entities.AnalyzeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface AnalyzeQueryRepository extends JpaRepository<AnalyzeQuery, Long> {
    AnalyzeQuery findByQuery(@NonNull String query);
}