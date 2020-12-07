package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Examiner;

import java.util.List;

public interface IExaminerService {
    List<Examiner> examiner_list();

    void addExaminer(Examiner examiner);

    void delExaminer(String id);

    Examiner findById(String id);
}
