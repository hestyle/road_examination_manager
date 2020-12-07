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
}
