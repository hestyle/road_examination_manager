package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.entity.ExamTemplate;

import java.util.List;

public interface IExamTemplateService {
    List<ExamTemplate> findByPage(Integer pageIndex, Integer pageSize);

    Integer getExamTemplateCount();

    Boolean delById(String id);

    List<ExamItem> findAllExamItem();

    Boolean add(String name, Integer score, String type, List<ExamItem> examItemList);

    ExamTemplate findByExamTemplateId(String examTemplateId);
}
