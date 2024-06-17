package com.javamicroservice.repositories;


import com.javamicroservice.utils.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface FileRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findAllByUserEmail(String userEmail, Sort sort);
}

