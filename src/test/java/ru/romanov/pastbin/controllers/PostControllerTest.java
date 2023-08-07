package ru.romanov.pastbin.controllers;

import static java.util.Optional.empty;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.romanov.pastbin.dto.PostDTO;
import ru.romanov.pastbin.dto.ReturnedPostDTO;
import ru.romanov.pastbin.models.Person;
import ru.romanov.pastbin.models.Post;
import ru.romanov.pastbin.repositories.PostRepository;
import ru.romanov.pastbin.services.PostService;
import ru.romanov.pastbin.services.S3Service;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @Mock
    private S3Service s3Service;
    @Mock
    private PostService postService;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PostRepository postRepository;
    @InjectMocks
    private PostController postController;
    @Test
    void shouldCreatedPost() {
        PostDTO postDTO = new PostDTO();
        postDTO.setText("Test post content");

        Principal principal = mock(Principal.class);
        String expectedObjectKey = "some_object_key";
        String expectedUrl = "http://localhost:8080/pastebin/posts/get/some_generated_url";

        Post expectedPost = new Post();
        when(postService.createUrl()).thenReturn("some_generated_url");
        when(modelMapper.map(postDTO, Post.class)).thenReturn(expectedPost);
        when(s3Service.uploadText(expectedPost.getText(), principal)).thenReturn(expectedObjectKey);

        ResponseEntity<String> responseEntity = postController.createPost(postDTO, principal);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUrl, responseEntity.getBody());

        verify(postService, times(1)).save(expectedPost, principal);
    }

    @Test
    void shouldGettingPostWhenPostPresent() {
        String domain = "http://localhost:8080/pastebin/posts/get/";
        String url = "someurl";

        Post expectedPost = new Post();
        expectedPost.setObjectKey("someKey");
        expectedPost.setText("some text");

        when(postService.getPostByUrl(domain + url)).thenReturn(expectedPost);
        when(s3Service.getTextFromS3(expectedPost.getObjectKey())).thenReturn(expectedPost.getText());

        ResponseEntity<ReturnedPostDTO> responseEntity = postController.getPost(url);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPost.getText(), responseEntity.getBody().getText());
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        String domain = "http://localhost:8080/pastebin/posts/get/";
        String url = "someurl";
        when(postService.getPostByUrl(domain + url)).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> {
            postController.getPost(url);
        });
    }
}
