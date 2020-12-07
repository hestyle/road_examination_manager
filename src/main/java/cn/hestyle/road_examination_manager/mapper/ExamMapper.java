package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Exam;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamMapper {
    /**
     * 根据考生准考证号查询考试信息
     * @param admissionNo 考生准考证号
     * @return Exam
     */
    public Exam findByAdmissionNo(String admissionNo);

    /**
     * 分页查询 获取未被删除的考试信息 考试开始时间降序排序
     * @param beginIndex
     * @param pageSize
     * @return
     */
    List<Exam> findByPage(Integer beginIndex, Integer pageSize);

    /**
     * 获取考试信息数量
     * 只查询未删除的
     * @return
     */
    Integer getExamCount();

    Integer addNew(Exam exam);

    Integer deleteByAdmissionNo(String admissionNo);
}
