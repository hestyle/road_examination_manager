package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.service.exception.AccessDefinedException;
import cn.hestyle.road_examination_manager.service.exception.DeleteException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import cn.hestyle.road_examination_manager.service.exception.UpdateException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface IExamService {
    /**
     * 根据考生准考证号查询考试信息
     * @param admissionNo
     * @return
     */
    Exam findByAdmissionNo(String admissionNo) throws AccessDefinedException;

    List<Exam> findByPage(Integer pageIndex, Integer pageSize, String admissionNo) throws PageFindErrorException;

//    List<Exam> findByPage(Integer pageIndex, Integer pageSize, String admissionNo) throws PageFindErrorException;

    Boolean add(Exam newExam) throws AccessDefinedException;

    Boolean deleteByAdmissionNo(String admissionNo)  throws AccessDefinedException, DeleteException;

    Boolean modifyExamInfo(Exam newExam) throws UpdateException, AccessDefinedException;

    Map<String, Object> findDetailInfoByAdmissionNo(String admissionNo);

    /**
     * 根据准考证号获取道路考试模板信息、道路考试模板对应的考试项目
     * @param admissionNo
     * @return
     */
    Map<String, Object> findExamItemsInfoByAdmissionNo(String admissionNo);

    /**
     * 根据准考证号获取灯光考试模板信息、道路考试模板对应的考试项目
     * @param admissionNo
     * @return
     */
    Map<String, Object> findExamLightItemsInfoByAdmissionNo(String admissionNo);

    /**
     * 获取符合准考证的数据数量，只查询未删除的
     * @param admissionNo
     * @return
     */
    Integer getExamCount(String admissionNo);

    Exam generateExamInfo(String candidateId, String examTemplateId, String lightExamTemplateId, String examTime);

    List<Exam> findByExaminerId(String examinerId);
}
