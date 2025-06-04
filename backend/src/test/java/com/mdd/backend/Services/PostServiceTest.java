package com.mdd.backend.Services;

import com.mdd.backend.Exceptions.PostNotFoundException;
import com.mdd.backend.Exceptions.SubjectNotFoundException;
import com.mdd.backend.Models.*;
import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Repositories.PostRepository;
import com.mdd.backend.Repositories.SubjectRepository;
import com.mdd.backend.Services.Post.PostServiceImpl;
import com.mdd.backend.Services.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PostServiceTest {

    private PostRepository postRepository;
    private SubjectRepository subjectRepository;
    private UserService userService;
    private PostServiceImpl postService;

    @BeforeEach
    public void setUp() {
        postRepository = mock(PostRepository.class);
        subjectRepository = mock(SubjectRepository.class);
        userService = mock(UserService.class);
        postService = new PostServiceImpl(postRepository, subjectRepository, userService);
    }

    @Test
    public void testCreatePost_Success() {
        PostRequest request = new PostRequest();
        request.setTitle("New Post");
        request.setContent("Content");
        request.setSubjectId(1L);

        Subject subject = new Subject();
        User user = new User();

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(userService.getCurrentAuthenticatedUser()).thenReturn(user);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PostResponse response = postService.createPost(request);

        assertEquals("New Post", response.getTitle());
        assertEquals("Content", response.getContent());
    }

    @Test
    public void testCreatePost_SubjectNotFound() {
        PostRequest request = new PostRequest();
        request.setSubjectId(2L);
        when(subjectRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(SubjectNotFoundException.class, () -> postService.createPost(request));
    }

    @Test
    public void testGetPostById_Success() {
        Post post = new Post();
        post.setId(1L);
        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        PostResponse response = postService.getPostById(1L);
        assertEquals(1L, response.getId());
    }

    @Test
    public void testGetPostById_NotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(PostNotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    public void testGetPostsBySubject_Success() {
        when(subjectRepository.existsById(1L)).thenReturn(true);
        when(postRepository.findBySubjectId(1L)).thenReturn(List.of(new Post(), new Post()));
        List<PostResponse> responses = postService.getPostsBySubject(1L);
        assertEquals(2, responses.size());
    }

    @Test
    public void testGetPostsBySubject_SubjectNotFound() {
        when(subjectRepository.existsById(999L)).thenReturn(false);
        assertThrows(SubjectNotFoundException.class, () -> postService.getPostsBySubject(999L));
    }

    @Test
    public void testConvertEntityToDto_NullPost() {
        assertThrows(IllegalArgumentException.class, () -> postService.convertEntityToDto(null));
    }
}

