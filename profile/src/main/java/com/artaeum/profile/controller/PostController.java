package com.artaeum.profile.controller;

import com.artaeum.profile.controller.error.NotFoundException;
import com.artaeum.profile.controller.utils.PaginationUtil;
import com.artaeum.profile.domain.Post;
import com.artaeum.profile.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody @Valid Post post, Principal principal) {
        post.setUserId(principal.getName());
        this.postService.create(post);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void update(@RequestBody @Valid Post post, Principal principal) {
        this.postService.get(post.getId())
                .filter(p -> p.getUserId().equals(principal.getName()))
                .ifPresent(this.postService::update);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> get(@PathVariable Long id) {
        return this.postService.get(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElseThrow(NotFoundException::new);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(Pageable pageable, @RequestParam(required = false) String userId) {
        Page<Post> page;
        HttpHeaders headers;
        if (userId == null) {
            page = this.postService.getAll(pageable);
            headers = PaginationUtil.generatePaginationHttpHeaders(page, "/posts");
        } else {
            page = this.postService.getAllByUserId(pageable, userId);
            headers = PaginationUtil.generatePaginationHttpHeaders(page, "/posts?userId=" + userId);
        }
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Post>> search(Pageable pageable, @RequestParam String query) {
        Page<Post> page = this.postService.search(pageable, query);
        HttpHeaders headers = PaginationUtil
                .generatePaginationHttpHeaders(page, "/posts/search?query=" + query);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id, Principal principal) {
        this.postService.get(id)
                .filter(p -> p.getUserId().equals(principal.getName()))
                .ifPresent(this.postService::delete);
    }
}
