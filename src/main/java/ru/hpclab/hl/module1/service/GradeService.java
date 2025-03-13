package ru.hpclab.hl.module1.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import ru.hpclab.hl.module1.DTO.GradeDTO;
import ru.hpclab.hl.module1.Entity.GradeEntity;
import ru.hpclab.hl.module1.controller.exeption.UserException;
import ru.hpclab.hl.module1.model.Grade;
import ru.hpclab.hl.module1.repository.GradeRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class GradeService {

    private final GradeRepository gradeRepository;
    @Autowired
    private ModelMapper modelMapper;



    private GradeEntity convertToEntity(GradeDTO gradeDTO) {
        return modelMapper.map(gradeDTO, GradeEntity.class);
    }

    public GradeDTO convertToDTO(GradeEntity gradeEntity) {
        GradeDTO gradeDTO = new GradeDTO();
        gradeDTO.setStudentId(gradeEntity.getStudentEntity().getStudentId());
        gradeDTO.setSubjectId(gradeEntity.getSubjectEntity().getSubjectId());
        gradeDTO.setGradeValue(gradeEntity.getGradeValue());
        gradeDTO.setGradingDate(gradeEntity.getGradingDate());
        return gradeDTO;
    }
    
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public List<GradeDTO> getAllGrades() {
        return gradeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }


    public GradeDTO getGradeById(String id) {
        GradeEntity entity =  gradeRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserException("Grade with ID " + id + " not found"));
        return convertToDTO(entity);
    }

    public GradeDTO saveGrade(GradeDTO grade) {
        GradeEntity entity = convertToEntity(grade);
        return convertToDTO( gradeRepository.save(entity));
    }

    public void deleteGrade(String id) {
        gradeRepository.deleteById(UUID.fromString(id));
    }

    public GradeDTO updateGrade(String id, GradeDTO grade) {
        GradeEntity entity = convertToEntity(grade);
        entity.setGradeId(UUID.fromString(id));
        return  convertToDTO( gradeRepository.save(entity));
    }

    public double calculateAverageGradeForClass(UUID subjectId, int year) {
        Date startDate = getStartOfYear(year);
        Date endDate = getEndOfYear(year);

        List<GradeEntity> grades = gradeRepository.findBySubjectAndGradingDateBetween(subjectId, startDate, endDate);

        if (grades.isEmpty()) {
            return 0.0;
        }

        double sum = grades.stream().mapToInt(GradeEntity::getGradeValue).sum();
        return sum / grades.size();
    }



    private Date getStartOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        return calendar.getTime();
    }

    private Date getEndOfYear(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return calendar.getTime();
    }
}
