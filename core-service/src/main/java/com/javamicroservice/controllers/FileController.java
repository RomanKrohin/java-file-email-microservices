package com.javamicroservice.controllers;

import com.javamicroservice.services.FileService;
import com.javamicroservice.services.UserService;
import com.javamicroservice.utils.FileMetadata;
import com.javamicroservice.utils.FileResponse;
import com.javamicroservice.utils.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/files")
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private UserService userService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files, Principal principal) {
        try {
            if (userService.isUserBlocked(principal.getName())) {
                return ResponseEntity.status(403).body("User is blocked and cannot upload files.");
            }
            long totalSize=0;
            fileService.uploadFiles(files, principal.getName());
            for (MultipartFile file : files) {
                totalSize+=file.getSize();
            }
            rabbitMQSender.sendUploadMessage(principal.getName(), "You are uploaded: "+totalSize);
            return ResponseEntity.ok("Files uploaded successfully");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error uploading files");
        }
    }

    @GetMapping
    public ResponseEntity<List<FileResponse>> listFiles(@RequestParam Optional<String> sortBy,
                                                        @RequestParam Optional<String> filterByDate,
                                                        @RequestParam Optional<Long> filterById,
                                                        @RequestParam Optional<Long> filterBySize,
                                                        Principal principal) {
        if (userService.isUserBlocked(principal.getName())) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(fileService.listFiles(principal.getName(), sortBy.orElse("uploadTime"), filterByDate, filterById, filterBySize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id,
                                                 Principal principal) throws Exception {
        if (userService.isUserBlocked(principal.getName())) {
            return ResponseEntity.status(403).body(null);
        }
        FileMetadata fileMetadata = fileService.getFileMetadata(id, principal.getName());
        ByteArrayResource resource = new ByteArrayResource(fileService.downloadFile(fileMetadata));

        String contentType = fileMetadata.getFileName().toLowerCase().endsWith("png") ? "image/png" : "image/jpeg";
        rabbitMQSender.sendDownloadMessage(principal.getName(), "You are downloaded: "+fileMetadata.getFileName()+", size: "+fileMetadata.getSize());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileMetadata.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, contentType)
                .contentLength(fileMetadata.getSize())
                .body(resource);
    }


    @GetMapping("/mod/files")
    public ResponseEntity<List<FileResponse>> listAllFiles(@RequestParam Optional<String> sortBy,
                                                           @RequestParam Optional<String> filterByDate,
                                                           @RequestParam Optional<Long> filterById,
                                                           @RequestParam Optional<Long> filterBySize,
                                                           Principal principal) {
        return ResponseEntity.ok(fileService.listAllFiles(sortBy.orElse("uploadTime"), filterByDate, filterById, filterBySize));
    }

}
