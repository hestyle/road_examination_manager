package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;

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
}
