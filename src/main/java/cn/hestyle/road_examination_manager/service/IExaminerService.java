package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Examiner;

import java.util.List;

public interface IExaminerService {

    Examiner login(String id, String password);

    Boolean addExaminer(Examiner examiner);

    void delExaminer(String id);

    Examiner findById(String id);

    List<Examiner> findByPage(Integer pageIndex, Integer pageSize);

    Integer getExaminerCount();

    Boolean modifyBaseInfo(Examiner examiner);

    Boolean deleteExaminerByIdList(List<String> idList);

    Boolean modifyPassword(String id, String newPassword, String reNewPassword);
}
