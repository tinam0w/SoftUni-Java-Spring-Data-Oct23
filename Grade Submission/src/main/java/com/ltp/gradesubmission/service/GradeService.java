package com.ltp.gradesubmission.service;

import com.ltp.gradesubmission.utils.Constants;
import com.ltp.gradesubmission.model.Grade;
import com.ltp.gradesubmission.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    GradeRepository gradeRepository;

    @Autowired
    public GradeService(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    public Grade getGrade(int index){
        return gradeRepository.getGrade(index);
    }

    public void addGrade(Grade grade){
        gradeRepository.addGrade(grade);
    }

    public void updateGrade(Grade grade, int index){
        gradeRepository.updateGrade(grade, index);
    }

    public List<Grade> getGrades(){
        return gradeRepository.getGrades();
    }

    public Integer getGradeIndex(String id) {
        for (Grade studentGrade : getGrades()) {
            if (studentGrade.getId().equals(id)) return getGrades().indexOf(studentGrade);
        }
        return Constants.NOT_FOUND;
    }

    public Grade getGradeById(String  id){
        int index = getGradeIndex(id);
        return index == Constants.NOT_FOUND ?
                        new Grade() : getGrade(index);
    }

    public void submitGrade(Grade grade){
        int index = getGradeIndex(grade.getId());
        if (index == Constants.NOT_FOUND) {
            addGrade(grade);
        } else {
            updateGrade(grade, index);
        }
    }
}
