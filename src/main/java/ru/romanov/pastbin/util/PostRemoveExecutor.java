package ru.romanov.pastbin.util;

import ru.romanov.pastbin.repositories.PostRepository;

import java.time.LocalDateTime;

public class PostRemoveExecutor implements Runnable {
    private PostRepository postRepository;

    public PostRemoveExecutor(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public void run() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        postRepository.deleteExpiredPost(currentDateTime);
    }
}
