package ru.romanov.pastbin.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.models.Post;
import ru.romanov.pastbin.repositories.PostRepository;

import java.security.Principal;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final PersonService personService;

    @Autowired
    public PostService(PostRepository postRepository, PersonService personService) {
        this.postRepository = postRepository;
        this.personService = personService;
    }

    public String createUrl() {
//        RestTemplate restTemplate = new RestTemplate();
//        String requestUrl = "http://localhost:8088/get/url";
//        String resultUrl = restTemplate.getForObject(requestUrl, String.class);
        String url = "testurl";
        return url; // TODO edit later
    }

    @Transactional
    public void save(Post post, Principal principal) {
        Optional<Person> foundPerson = personService.getPersonByUsername(principal.getName());
        if (foundPerson.isPresent()) {
            post.setCreatedAt(new Date());
            post.setPerson(foundPerson.get());
            postRepository.save(post);
        }
    }

    public Optional<Post> getPostByUrl(String url) {
        return postRepository.findByUrl(url);
    }
}
