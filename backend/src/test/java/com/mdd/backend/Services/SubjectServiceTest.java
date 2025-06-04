package com.mdd.backend.Services;

import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Repositories.SubjectRepository;
import com.mdd.backend.Services.Subject.SubjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SubjectServiceTest {

    private SubjectRepository subjectRepository;
    private SubjectServiceImpl subjectService;

    @BeforeEach
    void setUp() {
        subjectRepository = mock(SubjectRepository.class);
        ModelMapper modelMapper = new ModelMapper();
        subjectService = new SubjectServiceImpl(subjectRepository, modelMapper);
    }

    @Test
    void testCreateSubject() {
        SubjectRequest request = new SubjectRequest();
        request.setName("Java");

        Subject saved = new Subject();
        saved.setId(1L);
        saved.setName("Java");

        when(subjectRepository.save(any(Subject.class))).thenReturn(saved);

        SubjectResponse response = subjectService.createSubject(request);

        assertNotNull(response);
        assertEquals("Java", response.getName());
    }

    @Test
    void testGetAllSubjects() {
        Subject s = new Subject();
        s.setName("Spring");

        when(subjectRepository.findAll()).thenReturn(List.of(s));

        List<SubjectResponse> list = subjectService.getAllSubjects();
        assertEquals(1, list.size());
        assertEquals("Spring", list.get(0).getName());
    }

    @Test
    void testGetSubjectById_Found() {
        Subject s = new Subject();
        s.setId(1L);
        s.setName("Angular");

        when(subjectRepository.findById(1L)).thenReturn(Optional.of(s));

        SubjectResponse response = subjectService.getSubjectById(1L);

        assertEquals("Angular", response.getName());
    }

    @Test
    void testGetSubjectById_NotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> subjectService.getSubjectById(1L));
    }

    @Test
    void testGetSubjectByName_Found() {
        Subject s = new Subject();
        s.setName("Node");

        when(subjectRepository.findByName("Node")).thenReturn(Optional.of(s));

        SubjectResponse response = subjectService.getSubjectByName("Node");

        assertEquals("Node", response.getName());
    }
}
