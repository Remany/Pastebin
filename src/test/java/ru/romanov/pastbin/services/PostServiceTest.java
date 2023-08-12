package ru.romanov.pastbin.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.models.Post;
import ru.romanov.pastbin.repositories.PostRepository;

import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private PersonService personService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private S3Service s3Service;
    @InjectMocks
    private PostService postService;
    @Captor
    private ArgumentCaptor<Object> objectKeyCaptor;

    @Test
    void shouldGettingPostWhenPostPresent() {
        String url = "/someurl";
        Post expectedPost = new Post();
        expectedPost.setUrl(url);
        expectedPost.setText("some text");

        when(postRepository.findByUrl(url)).thenReturn(Optional.of(expectedPost));
        Post resultPost = postService.getPostByUrl(url);

        assertEquals(resultPost, expectedPost);
    }

    @Test
    void shouldGettingPostWhenPostNotFound() {
        String url = "/someurl";

        when(postRepository.findByUrl(url)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> {
            postService.getPostByUrl(url);
        });
    }

    @Test
    void shouldSavedPost() throws NoSuchMethodException {
        Post post = new Post();
        post.setText("some text");

        Person person = new Person();
        person.setUsername("someName");
        when(personService.getPersonByUsername(person.getUsername())).thenReturn(Optional.of(person));

        Principal principal = new Principal() {
            @Override
            public String getName() {
                return "someName";
            }
        };

        postService.save(post, principal);

        verify(postRepository, times(1)).save(post);
        assertEquals(person, post.getPerson());
    }

    @Test
    void shouldDeletedExpiredPosts() {
        Date currentTime = new Date();
        List<Post> expiredPosts = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Post expiredPost = new Post();
            expiredPost.setExpiresAt(new Date(currentTime.getTime() - (i + 1) * 1000));
            expiredPost.setObjectKey("post" + i);
            expiredPosts.add(expiredPost);
        }

        when(postRepository.findByExpiresAtBefore(Mockito.any())).thenReturn(expiredPosts);

        postService.deleteExpiredPosts();

        for (Post expiredPost : expiredPosts) {
            verify(s3Service, times(1)).deleteObjectFromS3(expiredPost.getObjectKey());
        }

        verify(postRepository, times(1)).deleteAll(expiredPosts);
    }
}