package com.mdd.backend.Services.Subject;

import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Repositories.SubjectRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    ModelMapper modelMapper;

    public SubjectServiceImpl(SubjectRepository subjectRepository , ModelMapper modelMapper) {
        this.subjectRepository = subjectRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public SubjectResponse createSubject(SubjectRequest request) {
        Subject subject = convertDtoToEntity(request);
        Subject savedSubject = subjectRepository.save(subject);
        return convertEntityToDto(savedSubject);
    }

    @Override
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll()
                .stream()
                .map(this::convertEntityToDto)
                .toList();
    }

    @Override
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec l'id : " + id));
        return convertEntityToDto(subject);
    }

    @Override
    public SubjectResponse getSubjectByName(String name) {
        Subject subject = subjectRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Sujet introuvable avec le nom : " + name));
        return convertEntityToDto(subject);
    }

    @Override
    public SubjectResponse convertEntityToDto(Subject subject) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper.map(subject, SubjectResponse.class);
    }

    @Override
    public Subject getSubjectEntityById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sujet non trouvé avec l'ID : " + id));
    }

    @Override
    public Subject convertDtoToEntity(SubjectRequest subjectRequest) {
        return modelMapper.map(subjectRequest, Subject.class);
    }
}