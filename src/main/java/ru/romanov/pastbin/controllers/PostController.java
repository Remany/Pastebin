package ru.romanov.pastbin.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.pastbin.dto.PostDTO;
import ru.romanov.pastbin.dto.ReturnedPostDTO;
import ru.romanov.pastbin.models.Post;
import ru.romanov.pastbin.services.PostService;
import ru.romanov.pastbin.services.S3Service;

import java.security.Principal;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/pastebin/posts")
public class PostController {
    private final S3Service s3Service;
    private final PostService postService;
    private final ModelMapper modelMapper;
    @Autowired
    public PostController(S3Service s3Service, PostService postService, ModelMapper modelMapper) {
        this.s3Service = s3Service;
        this.postService = postService;
        this.modelMapper = modelMapper;
    }

    private Post convertToPost(PostDTO postDTO) {
        return this.modelMapper.map(postDTO, Post.class);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO, Principal principal) {
        String domain = "http://localhost:8080/pastebin/posts/get/";
        Post post = convertToPost(postDTO);
        post.setUrl(domain + postService.createUrl());
        post.setObjectKey(s3Service.uploadText(post.getText(), principal));
        postService.save(post, principal);
        return ResponseEntity.ok(post.getUrl());
    }

    @GetMapping("/get/{url}")
    public ResponseEntity<ReturnedPostDTO> getPost(@PathVariable("url") String url) {
        String domain = "http://localhost:8080/pastebin/posts/get/";
        Post foundPost = postService.getPostByUrl(domain + url)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
        String text = s3Service.getTextFromS3(foundPost.getObjectKey());
        String title = foundPost.getTitle();
        return ResponseEntity.ok(new ReturnedPostDTO(title, text));
    }
}
