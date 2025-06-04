package com.mdd.backend.Services.Comment;

import com.mdd.backend.Models.Comment;
import com.mdd.backend.Models.Dto.CommentDTO.CommentRequest;
import com.mdd.backend.Models.Dto.CommentDTO.CommentResponse;
import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Models.Post;

import java.util.List;

public interface CommentService {
    CommentResponse createComment(CommentRequest comment);
    List<CommentResponse> getCommentsByPostId(Long postId);
    void deleteComment(Long id);

    CommentResponse convertEntityToDto (Comment comment);
    Comment convertDtoToEntity(CommentRequest commentRequest);
}
