package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.service.exception.AccessDefinedException;
import org.springframework.stereotype.Service;

public interface IExamService {
    /**
     * 根据考生准考证号查询考试信息
     * @param admissionNo
     * @return
     */
    Exam findByAdmissionNo(String admissionNo) throws AccessDefinedException;
}
