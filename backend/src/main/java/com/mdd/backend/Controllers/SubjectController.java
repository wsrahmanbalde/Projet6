package com.mdd.backend.Controllers;

import com.mdd.backend.Models.Dto.SubjectDTO.SubjectRequest;
import com.mdd.backend.Models.Dto.SubjectDTO.SubjectResponse;
import com.mdd.backend.Models.Subject;
import com.mdd.backend.Services.Subject.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @PostMapping
    public ResponseEntity<SubjectResponse> create(@RequestBody SubjectRequest subject) {
        return ResponseEntity.ok(subjectService.createSubject(subject));
    }

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAll() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getSubjectById(id));
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<SubjectResponse> getByName(@PathVariable String name) {
        return ResponseEntity.ok(subjectService.getSubjectByName(name));
    }
}
