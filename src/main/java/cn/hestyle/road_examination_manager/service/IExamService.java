package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.service.exception.AccessDefinedException;
import cn.hestyle.road_examination_manager.service.exception.DeleteException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import cn.hestyle.road_examination_manager.service.exception.UpdateException;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IExamService {
    /**
     * 根据考生准考证号查询考试信息
     * @param admissionNo
     * @return
     */
    Exam findByAdmissionNo(String admissionNo) throws AccessDefinedException;

    Integer getExamCount() throws PageFindErrorException;

    List<Exam> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException;

    Boolean add(Exam newExam) throws AccessDefinedException;

    Boolean deleteByAdmissionNo(String admissionNo)  throws AccessDefinedException, DeleteException;

    Boolean modifyExamInfo(Exam newExam) throws UpdateException, AccessDefinedException;
}
