package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Examiner;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExaminerMapper{
    List<Examiner> findAll();

    void addExaminer(Examiner examiner);

    void delExaminer(String id);

    Examiner findById(String id);

    /**
     * 分页查询所有manager
     * @param beginIndex    username降序排列
     * @param pageSize      一页大小
     * @return              manager list
     */
    List<Examiner> findByPage(Integer beginIndex, Integer pageSize);

    Integer getExaminerCount();

    Integer update(Examiner examiner);
}
