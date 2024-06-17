package com.javamicroservice.services;

import com.javamicroservice.utils.FileMetadata;
import com.javamicroservice.repositories.FileRepository;
import com.javamicroservice.utils.FileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class FileService {

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    @Autowired
    private FileRepository fileRepository;

    @Async
    public CompletableFuture<Void> uploadFiles(MultipartFile[] files, String userEmail) throws IOException {

        for (MultipartFile file : files) {
            if (!isImage(file)) {
                throw new IllegalArgumentException("File must be an image (JPG or PNG)");
            }

            if (file.getSize() > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size must be less than 10 MB");
            }

            byte[] fileData = file.getBytes();
            FileMetadata metadata = new FileMetadata();
            metadata.setFileName(file.getOriginalFilename());
            metadata.setSize(file.getSize());
            metadata.setUploadTime(LocalDateTime.now());
            metadata.setUserEmail(userEmail);
            metadata.setData(fileData);
            fileRepository.save(metadata);
        }
        return CompletableFuture.completedFuture(null);
    }

    public List<FileResponse> listFiles(String userEmail, String sortBy) {
        List<FileMetadata> files = fileRepository.findAllByUserEmail(userEmail, Sort.by(sortBy));
        return files.stream()
                .map(file -> new FileResponse(
                        file.getId(),
                        file.getFileName(),
                        file.getSize(),
                        file.getUploadTime().toString(),
                        file.getUserEmail()))
                .collect(Collectors.toList());
    }


    public FileMetadata getFileMetadata(Long id, String userEmail) throws Exception {
        FileMetadata fileMetadata = fileRepository.findById(id).orElseThrow(() -> new Exception());
        if (!fileMetadata.getUserEmail().equals(userEmail)) {
            throw new AccessDeniedException("You don't have permission to access this file");
        }
        return fileMetadata;
    }

    public List<FileResponse> listAllFiles(String sortBy) {
        List<FileMetadata> files = fileRepository.findAll(Sort.by(sortBy));
        return files.stream()
                .map(file -> new FileResponse(
                        file.getId(),
                        file.getFileName(),
                        file.getSize(),
                        file.getUploadTime().toString(),
                        file.getUserEmail()))
                .collect(Collectors.toList());
    }
    public byte[] downloadFile(FileMetadata fileMetadata) {
        return fileMetadata.getData();
    }

    private boolean isImage(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType.equals("image/jpeg") || contentType.equals("image/png");
    }

}
