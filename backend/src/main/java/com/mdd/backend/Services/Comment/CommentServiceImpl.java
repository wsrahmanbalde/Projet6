package com.mdd.backend.Services.Comment;

import com.mdd.backend.Exceptions.ResourceNotFoundException;
import com.mdd.backend.Exceptions.UnauthorizedException;
import com.mdd.backend.Models.Comment;
import com.mdd.backend.Models.Dto.CommentDTO.CommentRequest;
import com.mdd.backend.Models.Dto.CommentDTO.CommentResponse;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Models.Post;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.CommentRepository;
import com.mdd.backend.Repositories.PostRepository;
import com.mdd.backend.Services.User.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              PostRepository postRepository,
                              UserService userService,
                              ModelMapper modelMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @Override
    public CommentResponse createComment(CommentRequest commentRequest) {
        User currentUser = userService.getCurrentAuthenticatedUser();

        if (currentUser == null) {
            throw new UnauthorizedException("Aucun utilisateur connecté");
        }

        Post post = postRepository.findById(commentRequest.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException("Post avec l'ID " + commentRequest.getPostId() + " non trouvé"));

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .post(post)
                .author(currentUser)
                .createdAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);

        return convertEntityToDto(savedComment);
    }

    @Override
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Commentaire avec l'ID " + id + " non trouvé");
        }
        commentRepository.deleteById(id);
    }

    @Override
    public CommentResponse convertEntityToDto(Comment comment) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        CommentResponse response = modelMapper.map(comment, CommentResponse.class);
        response.setAuthorUsername(comment.getAuthor().getUsername());
        response.setPostId(comment.getPost().getId());
        return response;
    }

    @Override
    public Comment convertDtoToEntity(CommentRequest commentRequest) {
        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        // Le post et l’auteur sont gérés ailleurs
        return comment;
    }
}
