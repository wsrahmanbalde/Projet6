package com.mdd.backend.Services;

import com.mdd.backend.Exceptions.ResourceNotFoundException;
import com.mdd.backend.Exceptions.UnauthorizedException;
import com.mdd.backend.Models.Comment;
import com.mdd.backend.Models.Dto.CommentDTO.CommentRequest;
import com.mdd.backend.Models.Dto.CommentDTO.CommentResponse;
import com.mdd.backend.Models.Post;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.CommentRepository;
import com.mdd.backend.Repositories.PostRepository;
import com.mdd.backend.Services.Comment.CommentServiceImpl;
import com.mdd.backend.Services.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceTest {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserService userService;
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        postRepository = mock(PostRepository.class);
        userService = mock(UserService.class);
        ModelMapper modelMapper = new ModelMapper();
        commentService = new CommentServiceImpl(commentRepository, postRepository, userService, modelMapper);
    }

    @Test
    void testCreateComment_success() {
        CommentRequest request = new CommentRequest();
        request.setContent("Contenu du commentaire");
        request.setPostId(1L);

        User user = new User();
        user.setId(2L);
        user.setUsername("john");

        Post post = new Post();
        post.setId(1L);

        Comment savedComment = Comment.builder()
                .id(3L)
                .content("Contenu du commentaire")
                .author(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        when(userService.getCurrentAuthenticatedUser()).thenReturn(user);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);

        CommentResponse response = commentService.createComment(request);

        assertNotNull(response);
        assertEquals("john", response.getAuthorUsername());
        assertEquals(1L, response.getPostId());
        assertEquals("Contenu du commentaire", response.getContent());

        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void testCreateComment_unauthorized() {
        when(userService.getCurrentAuthenticatedUser()).thenReturn(null);
        CommentRequest request = new CommentRequest();
        request.setPostId(1L);

        assertThrows(UnauthorizedException.class, () -> commentService.createComment(request));
    }

    @Test
    void testCreateComment_postNotFound() {
        CommentRequest request = new CommentRequest();
        request.setContent("Hello");
        request.setPostId(42L);

        User user = new User();
        when(userService.getCurrentAuthenticatedUser()).thenReturn(user);
        when(postRepository.findById(42L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> commentService.createComment(request));
    }

    @Test
    void testGetCommentsByPostId_returnsList() {
        Post post = new Post();
        post.setId(1L);

        User user = new User();
        user.setId(2L);
        user.setUsername("alice");

        Comment comment = Comment.builder()
                .id(10L)
                .content("Test comment")
                .author(user)
                .post(post)
                .createdAt(LocalDateTime.now())
                .build();

        when(commentRepository.findByPostId(1L)).thenReturn(List.of(comment));

        List<CommentResponse> responses = commentService.getCommentsByPostId(1L);

        assertEquals(1, responses.size());
        assertEquals("Test comment", responses.get(0).getContent());
        assertEquals("alice", responses.get(0).getAuthorUsername());
        assertEquals(1L, responses.get(0).getPostId());
    }

    @Test
    void testDeleteComment_success() {
        when(commentRepository.existsById(1L)).thenReturn(true);

        commentService.deleteComment(1L);

        verify(commentRepository).deleteById(1L);
    }

    @Test
    void testDeleteComment_notFound() {
        when(commentRepository.existsById(99L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> commentService.deleteComment(99L));
    }
}