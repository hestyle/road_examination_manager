package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.entity.ExamTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ExamTemplateMapper {
    /**
     * 根据考试模板id获取考试模板信息
     * @param id
     * @return
     */
    public ExamTemplate findById(String id);

    /**
     * 根据考试模板id查询该考试模板的所有考试内容
     * @param id
     * @return
     */
    public List<ExamItem> findExamItemListByExamTemplateId(String id);

    public Integer getExamTemplateCount();

    public List<ExamTemplate> findByPage(Integer beginIndex, Integer pageSize);

    Integer deleteById(String id);

    List<ExamItem> findAllAvailableExamItem();

    Integer getTodayNum(String yyyymmdd);

    Integer addNew(ExamTemplate examTemplate);
}
