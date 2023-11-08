package com.ltp.gradesubmission.service;

import com.ltp.gradesubmission.Constants;
import com.ltp.gradesubmission.Grade;
import com.ltp.gradesubmission.repository.GradeRepository;

import java.util.List;

public class GradeService {

    GradeRepository gradeRepository = new GradeRepository();

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
