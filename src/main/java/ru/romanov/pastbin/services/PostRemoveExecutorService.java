package ru.romanov.pastbin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PostRemoveExecutorService {
    private final PostService postService;

    @Autowired
    public PostRemoveExecutorService(PostService postService) {
        this.postService = postService;
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void deleteExpiredPosts() {
        postService.deleteExpiredPosts();
    }
}
