package ru.romanov.pastbin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.romanov.pastbin.repositories.PostRepository;
import ru.romanov.pastbin.util.PostRemoveExecutor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class PostRemoveExecutorService {
    private final PostRepository postRepository;
    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Autowired
    public PostRemoveExecutorService(PostRepository postRepository) {
        this.postRepository = postRepository;
        PostRemoveExecutor postRemoveExecutor = new PostRemoveExecutor(this.postRepository);
        scheduler.scheduleAtFixedRate(postRemoveExecutor, 0, 24, TimeUnit.HOURS);
    }
}
