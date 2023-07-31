package ru.romanov.pastbin.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.romanov.pastbin.dto.PostDTO;
import ru.romanov.pastbin.security.JWTUtil;
import ru.romanov.pastbin.services.PostService;
import ru.romanov.pastbin.services.S3Service;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private S3Service s3Service;
    @MockBean
    private JWTUtil jwtUtil;


    @Test
    public void testCreatePost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setText("some text"); postDTO.setTitle("some title");

        Principal principal = () -> "testuser";

        when(postService.createUrl()).thenReturn("someurl");
        when(s3Service.uploadText(any(), any())).thenReturn("someObjectKey");

        mockMvc.perform(post("/pastebin/posts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(postDTO))
                        .principal(principal))
                        .andExpect(status().isOk())
                        .andExpect(content().string("http://localhost:8080/pastebin/posts/get/1234"));
    }
    @Test
    void getPost() {
    }
}