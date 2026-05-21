package com.Cloud.Connection;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

  //--------------------------------------------------------------
    


    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam String fileName) {

        byte[] fileData = fileService.downloadFile(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + fileName + "\"")
                .body(fileData);
    }
}