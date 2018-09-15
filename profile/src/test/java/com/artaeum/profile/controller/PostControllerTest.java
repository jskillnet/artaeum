package com.artaeum.profile.controller;

import com.artaeum.profile.client.UaaClient;
import com.artaeum.profile.domain.Post;
import com.artaeum.profile.dto.PostDTO;
import com.artaeum.profile.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PostControllerTest {

    private static final String USER_LOGIN = "login";

    private static final Long USER_ID = 123L;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PostController postController;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @MockBean
    private UaaClient uaaClient;

    private MockMvc mockMvc;

    @Before
    public void init() {
        when(this.uaaClient.getUserIdByLogin(USER_LOGIN)).thenReturn(USER_ID);
        when(this.uaaClient.getUserLoginById(USER_ID)).thenReturn(USER_LOGIN);
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(this.postController)
                .setCustomArgumentResolvers(this.pageableArgumentResolver)
                .build();
    }

    @Test
    @Transactional
    public void whenCreatePost() throws Exception {
        PostDTO postDTO = new PostDTO();
        postDTO.setUserLogin(USER_LOGIN);
        postDTO.setText("text");
        this.mockMvc.perform(post("/posts")
                .principal(new UsernamePasswordAuthenticationToken(USER_LOGIN, "password", Collections.emptyList()))
                .content(this.objectMapper.writeValueAsBytes(postDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        Post result = this.postRepository.getOne(1L);
        assertEquals(postDTO.getText(), result.getText());
        assertEquals(USER_ID, result.getUserId());
    }

    @Test
    @Transactional
    public void whenUpdatePost() throws Exception {
        Post post = this.createPost();
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setUserLogin(USER_LOGIN);
        postDTO.setText("new text");
        this.mockMvc.perform(put("/posts")
                .principal(new UsernamePasswordAuthenticationToken(USER_LOGIN, "password", Collections.emptyList()))
                .content(this.objectMapper.writeValueAsBytes(postDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Post result = this.postRepository.getOne(1L);
        assertEquals(postDTO.getText(), result.getText());
        assertEquals(USER_ID, result.getUserId());
    }

    @Test
    public void whenGetNotExistPost() throws Exception {
        this.mockMvc.perform(get("/posts/123"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetPost() throws Exception {
        Post post = this.createPost();
        this.mockMvc.perform(get("/posts/{id}", post.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.text").value(post.getText()))
                .andExpect(jsonPath("$.userLogin").value(USER_LOGIN))
                .andExpect(jsonPath("$.createdDate").value(post.getCreatedDate().toEpochSecond()));
    }

    @Test
    public void getAllPosts() throws Exception {
        Post post = this.createPost();
        this.mockMvc.perform(get("/posts?sort=id,desc")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(post.getId()))
                .andExpect(jsonPath("$.[0].text").value(post.getText()))
                .andExpect(jsonPath("$.[0].userLogin").value(USER_LOGIN))
                .andExpect(jsonPath("$.[0].createdDate").value(post.getCreatedDate().toEpochSecond()));
    }

    @Test
    public void whenDeletePost() throws Exception {
        Post post = this.createPost();
        this.mockMvc.perform(delete("/posts/" + post.getId())
                .principal(new UsernamePasswordAuthenticationToken(USER_LOGIN, "password", Collections.emptyList())))
                .andExpect(status().isOk());
        Optional<Post> result = this.postRepository.findById(post.getId());
        assertFalse(result.isPresent());
    }

    private Post createPost() {
        Post post = new Post();
        post.setText("test text");
        post.setUserId(USER_ID);
        post.setCreatedDate(ZonedDateTime.now());
        return this.postRepository.save(post);
    }
}