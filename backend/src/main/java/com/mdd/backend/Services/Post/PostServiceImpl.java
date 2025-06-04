package com.mdd.backend.Services.Post;

import com.mdd.backend.Exceptions.PostNotFoundException;
import com.mdd.backend.Exceptions.SubjectNotFoundException;
import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Models.Post;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Models.User;
import com.mdd.backend.Repositories.PostRepository;
import com.mdd.backend.Repositories.SubjectRepository;
import com.mdd.backend.Services.User.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final SubjectRepository subjectRepository;
    private final UserService userService;
    ModelMapper modelMapper;

    public PostServiceImpl(PostRepository postRepository, SubjectRepository subjectRepository, UserService userService) {
        this.postRepository = postRepository;
        this.subjectRepository = subjectRepository;
        this.userService = userService;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public PostResponse createPost(PostRequest request) {
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new SubjectNotFoundException("Sujet avec ID " + request.getSubjectId() + " introuvable."));

        User author = userService.getCurrentAuthenticatedUser();

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSubject(subject);
        post.setAuthor(author);
        post.setCreatedAt(LocalDateTime.now());

        postRepository.save(post);
        return convertEntityToDto(post);
    }

    @Override
    public List<PostResponse> getAllPostsSortedByDateDesc() {

        return postRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public List<PostResponse> getAllPostsSortedByDateAsc() {

        return postRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post avec ID " + id + " introuvable."));
        return convertEntityToDto(post);
    }

    @Override
    public List<PostResponse> getPostsBySubject(Long subjectId) {

        if (!subjectRepository.existsById(subjectId)) {
            throw new SubjectNotFoundException("Sujet avec ID " + subjectId + " introuvable.");
        }

        return postRepository.findBySubjectId(subjectId)
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public PostResponse convertEntityToDto(Post post) {
        if (post == null) {
            throw new IllegalArgumentException("L'entité Post ne peut pas être nulle.");
        }
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(post,PostResponse.class);
    }

    @Override
    public Post convertDtoToEntity(PostRequest postRequest) {
        return modelMapper.map(postRequest,Post.class);
    }

}
