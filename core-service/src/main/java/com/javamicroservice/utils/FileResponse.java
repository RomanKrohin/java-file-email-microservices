package com.javamicroservice.utils;

public class FileResponse {
    private Long id;
    private String fileName;
    private long size;
    private String uploadTime;
    private String userEmail;

    public FileResponse(Long id, String fileName, long size, String uploadTime, String userEmail) {
        this.id = id;
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

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
