package com.mdd.backend.Controllers;

import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Services.Post.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok(postService.createPost(postRequest));
    }

    @GetMapping("/desc")
    public ResponseEntity<List<PostResponse>> getAllDesc() {
        return ResponseEntity.ok(postService.getAllPostsSortedByDateDesc());
    }

    @GetMapping("/asc")
    public ResponseEntity<List<PostResponse>> getAllAsc() {
        return ResponseEntity.ok(postService.getAllPostsSortedByDateAsc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<List<PostResponse>> getBySubject(@PathVariable Long subjectId) {
        return ResponseEntity.ok(postService.getPostsBySubject(subjectId));
    }
}
