package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.ExamTemplateItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamTemplateItemMapper {
    public Integer addNewByList(List<ExamTemplateItem> examTemplateItemList);
}
