package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Exam;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ExamMapper {
    /**
     * 根据考生准考证号查询考试信息
     * @param admissionNo 考生准考证号
     * @return Exam
     */
    public Exam findByAdmissionNo(String admissionNo);
}
