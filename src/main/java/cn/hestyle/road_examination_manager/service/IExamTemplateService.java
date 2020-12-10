package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamTemplate;

import java.util.List;

public interface IExamTemplateService {
    List<ExamTemplate> findByPage(Integer pageIndex, Integer pageSize);

    Integer getExamTemplateCount();

    Boolean delById(String id);
}
