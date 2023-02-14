package com.synrgy.security.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface KostService {
    String uploadFile(MultipartFile file, String folderName) throws IOException;
}
