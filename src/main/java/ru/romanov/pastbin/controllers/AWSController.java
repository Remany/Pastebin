package ru.romanov.pastbin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.romanov.pastbin.services.S3Service;

@RestController
@RequestMapping("/pastebin/test")
public class AWSController {
    private final S3Service s3Service;

    @Autowired
    public AWSController(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> uploadFile(@RequestParam(value = "file")MultipartFile multipartFile) {
        s3Service.uploadText(multipartFile);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
