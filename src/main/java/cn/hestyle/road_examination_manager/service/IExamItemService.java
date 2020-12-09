package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;

import java.util.List;

/**
 * examItem service层接口
 */
public interface IExamItemService {
    /**
     * 增加新examItem
     * @param examItem      新examItem
     * @return              是否保存成功
     */
    Boolean add(ExamItem examItem) throws InsertException;

    /**
     * 通过id查找ExamItem
     * @param id    examItem id
     * @return      examItem
     */
    ExamItem findById(Integer id) throws FindException;

    /**
     * 通过name查找ExamItem
     * @param name  examItem name
     * @return      examItem
     */
    ExamItem findByName(String name) throws FindException;

    /**
     * 分页查询所有examItem
     * @param pageIndex     页码
     * @param pageSize      一页大小
     * @return              examItem list
     */
    List<ExamItem> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException;

    /**
     * 获取examItem的数量
     * @return          examItem的数量
     */
    Integer getExamItemCount();
}
