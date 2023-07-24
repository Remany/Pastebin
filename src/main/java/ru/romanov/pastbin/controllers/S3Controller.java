package ru.romanov.pastbin.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.pastbin.services.PostService;
import ru.romanov.pastbin.services.S3Service;

import java.security.Principal;

@RestController
@RequestMapping("/pastebin/s3")
public class S3Controller {
    private final S3Service s3Service;
    private final PostService postService;

    @Autowired
    public S3Controller(S3Service s3Service, PostService postService) {
        this.s3Service = s3Service;
        this.postService = postService;
    }

    @PostMapping("/save")
    public ResponseEntity<HttpStatus> uploadFile(Principal principal) {
        s3Service.uploadText("Hello world", principal);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
