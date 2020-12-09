package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户持久层
 * @author hestyle
 */
@Mapper
public interface ExamItemMapper {
    /**
     * 通过id查询考试项
     * @param id        考试项id
     * @return          ExamItem
     */
    ExamItem findById(Integer id);

    /**
     * 通过name查询考试项
     * @param name      考试项name
     * @return          examItem
     */
    ExamItem findByName(String name);

    /**
     * 增加新examItem
     * @param examItem  新examItem
     * @return          是否插入成功
     */
    Integer addNew(ExamItem examItem);

    /**
     * 分页查询所有examItem
     * @param beginIndex    id降序排列
     * @param pageSize      一页大小
     * @return              examItem list
     */
    List<ExamItem> findByPage(Integer beginIndex, Integer pageSize);

    /**
     * 获取examItem的数量
     * @return      manager的数量
     */
    Integer getExamItemCount();

    /**
     * 更新examItem信息
     * @param examItem     待更新的examItem
     * @return             是否更新成功
     */
    Integer update(ExamItem examItem);
}
