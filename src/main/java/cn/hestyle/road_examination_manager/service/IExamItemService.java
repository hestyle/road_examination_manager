package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.service.exception.*;

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
     * 通过idsString批量查询
     * @param idsString     格式"id1,id2,id3,...,idn"字符串
     * @return              ExamItem list
     */
    List<ExamItem> findByIdsString(String idsString) throws FindException;

    /**
     * 通过idList批量查询
     * @param idList        格式{id1,id2,id3,...,idn}
     * @return              ExamItem list
     */
    List<ExamItem> findByIdList(List<Integer> idList) throws FindException;

    /**
     * 获取examItem的数量
     * @return          examItem的数量
     */
    Integer getExamItemCount();

    /**
     * 修改examItem
     * @param examItem      examItem
     * @return              是否修改成功
     */
    Boolean modify(ExamItem examItem) throws UpdateException;

    /**
     * 修改examItem
     * @param id            examItem id
     * @return              是否修改成功
     */
    Boolean modifyVoicePath(Integer id, String voicePath) throws UpdateException;

    /**
     * 通过id list删除
     * @param idList    examItem id list
     * @return          是否删除成功
     */
    Boolean deleteByIdList(List<Integer> idList) throws DeleteException;
}
