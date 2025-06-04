package com.mdd.backend.Services.Subject;

import com.mdd.backend.Models.Dto.PostDTO.PostRequest;
import com.mdd.backend.Models.Dto.PostDTO.PostResponse;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Post;
import com.mdd.backend.Models.Subject;

import java.util.List;

public interface SubjectService {
    SubjectResponse createSubject(SubjectRequest request);
    List<SubjectResponse> getAllSubjects();
    SubjectResponse getSubjectById(Long id);
    SubjectResponse getSubjectByName(String name);
    Subject getSubjectEntityById(Long id);

    SubjectResponse convertEntityToDto (Subject subject);
    Subject convertDtoToEntity(SubjectRequest subjectRequest);
}
