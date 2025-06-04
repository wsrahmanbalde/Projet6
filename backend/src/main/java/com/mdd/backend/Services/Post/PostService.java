package com.mdd.backend.Services.Post;

import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Models.Post;

import java.util.List;

public interface PostService {
    PostResponse createPost(PostRequest request);
    List<PostResponse> getAllPostsSortedByDateDesc();
    List<PostResponse> getAllPostsSortedByDateAsc();
    PostResponse getPostById(Long id);
    List<PostResponse> getPostsBySubject(Long subjectId);

    PostResponse convertEntityToDto ( Post post);
    Post convertDtoToEntity(PostRequest postRequest);
}
