package ru.hpclab.hl.module1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hpclab.hl.module1.model.Grade;
import ru.hpclab.hl.module1.model.Student;
import ru.hpclab.hl.module1.service.GradeService;
import ru.hpclab.hl.module1.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/grades")
public class GradeController {
    private final GradeService gradeService;

    @Autowired
    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    public List<Grade> getUsers() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/{id}")
    public Grade getGradeById(@PathVariable String id) {
        return gradeService.getGradeById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteGrade(@PathVariable String id) {
        gradeService.deleteGrade(id);
    }

    @PostMapping(value = "/grade")
    public Grade saveGrade(@RequestBody Grade grade) {
        return gradeService.saveGrade(grade);
    }

    @PutMapping(value = "/{id}")
    public Grade updateGrade(@PathVariable(required = false) String id, @RequestBody Grade grade) {
        return gradeService.updateGrade(id, grade);
    }

    @GetMapping("/average-grade")
    public double getAverageGradeBySubjectAndYear(
            @RequestParam UUID studentId,
            @RequestParam UUID subjectId,
            @RequestParam int year) {

        return gradeService.calculateAverageGradeBySubjectAndYear(studentId, subjectId, year);
    }
}
