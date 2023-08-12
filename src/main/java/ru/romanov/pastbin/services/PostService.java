package ru.romanov.pastbin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.models.Post;
import ru.romanov.pastbin.repositories.PostRepository;

import java.security.Principal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PersonService personService;
    private final S3Service s3Service;

    @Autowired
    public PostService(PostRepository postRepository, PersonService personService, S3Service s3Service) {
        this.postRepository = postRepository;
        this.personService = personService;
        this.s3Service = s3Service;
    }

    public String createUrl() {
        RestTemplate restTemplate = new RestTemplate();
        String requestUrl = "http://app-2:8088/generator/url/get";
        return restTemplate.getForObject(requestUrl, String.class);
    }

    public Post getPostByUrl(String url) {
        return postRepository.findByUrl(url)
                .orElseThrow(() -> new NoSuchElementException("Post not found"));
    }

    protected void setLifecycle(Post post) {
        post.setCreatedAt(new Date());
        if (post.getLifecycle() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(post.getCreatedAt());
            calendar.add(Calendar.DAY_OF_YEAR, post.getLifecycle());
            post.setExpiresAt(calendar.getTime());
        }
    }

    @Transactional
    public void save(Post post, Principal principal) {
        Optional<Person> foundPerson = personService.getPersonByUsername(principal.getName());
        setLifecycle(post);
        post.setPerson(foundPerson.get());
        postRepository.save(post);
    }

    @Transactional
    public void deleteExpiredPosts() {
        Date currentTime = new Date();
        List<Post> expiredPosts = postRepository.findByExpiresAtBefore(currentTime);
        expiredPosts.forEach(post -> {
            s3Service.deleteObjectFromS3(post.getObjectKey());
        });
        postRepository.deleteAll(expiredPosts);
    }
}
