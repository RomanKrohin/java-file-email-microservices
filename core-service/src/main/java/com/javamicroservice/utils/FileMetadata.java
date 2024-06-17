package com.javamicroservice.utils;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "BYTEA", name = "data", nullable = false)
    private byte[] data;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "size", nullable = false)
    private long size;
    @Column(name = "upload_time", nullable = false)
    private LocalDateTime uploadTime;
    @Column(name = "user_email", nullable = false)
    private String userEmail;




    public FileMetadata() {

    }

    public FileMetadata(byte[] data, String fileName, long size, LocalDateTime uploadTime, String userEmail) {
        this.data = data;
        this.fileName = fileName;
        this.size = size;
        this.uploadTime = uploadTime;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
