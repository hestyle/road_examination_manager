package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.mapper.ExamMapper;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.service.exception.AccessDefinedException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ExamService implements IExamService {
    @Resource
    ExamMapper examMapper;

    @Override
    public Exam findByAdmissionNo(String admissionNo) throws AccessDefinedException{
        Exam exam=null;
        try {
            exam = examMapper.findByAdmissionNo(admissionNo);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("根据准考证查询考试信息时访问数据库失败！");
        }
        return exam;
    }
}
