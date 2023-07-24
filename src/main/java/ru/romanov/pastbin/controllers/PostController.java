package ru.romanov.pastbin.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.pastbin.dto.PostDTO;
import ru.romanov.pastbin.models.Post;
import ru.romanov.pastbin.services.PostService;
import ru.romanov.pastbin.services.S3Service;

import java.security.Principal;
import java.util.Optional;

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

    private static void accept(Post examplePost) {
        new Post();
    }

    private Post convertToPost(PostDTO postDTO) {
        return this.modelMapper.map(postDTO, Post.class);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPost(@RequestBody PostDTO postDTO, Principal principal) {
        Post post = convertToPost(postDTO);
        post.setUrl(postService.createUrl());
        postService.save(post, principal);
        s3Service.uploadText(post.getText(), principal);
        return ResponseEntity.ok(post.getUrl());
    }

    @GetMapping("/get/{url}")
    public Post getPost(@PathVariable("url") String url) {
        Optional<Post> foundPost = postService.getPostByUrl(url);
        return foundPost.get();
    }
}
