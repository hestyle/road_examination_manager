package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.service.exception.*;

import java.util.List;

/**
 * examOperation service层接口
 */
public interface IExamOperationService {
    /**
     * 增加新examOperation
     * @param examOperation     新examOperation
     * @return                  是否保存成功
     */
    Boolean add(ExamOperation examOperation) throws InsertException;

    /**
     * 通过id查找examOperation
     * @param id                id
     * @return                  examOperation
     */
    ExamOperation findById(Integer id) throws FindException;

    /**
     * 通过name查找examOperation
     * @param name              name
     * @return                  examOperation
     */
    ExamOperation findByName(String name) throws FindException;

    /**
     * 分页查询所有examOperation
     * @param pageIndex     页码
     * @param pageSize      一页大小
     * @return              examOperation list
     */
    List<ExamOperation> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException;

    /**
     * 通过idsString批量查询
     * @param idsString     格式"id1,id2,id3,...,idn"字符串
     * @return              ExamOperation list
     */
    List<ExamOperation> findByIdsString(String idsString) throws FindException;

    /**
     * 通过idList批量查询
     * @param idList        格式{id1,id2,id3,...,idn}
     * @return              ExamOperation list
     */
    List<ExamOperation> findByIdList(List<Integer> idList) throws FindException;

    /**
     * 查询examOperation的数量
     * @return              examOperation的数量
     */
    Integer getExamOperationCount();

    /**
     * 修改examOperation
     * @param examOperation     examOperation
     * @return                  保存成功与否
     */
    Boolean modify(ExamOperation examOperation) throws UpdateException;

    /**
     * 通过id删除
     * @param id        examOperation id
     * @return          是否删除成功
     */
    Boolean deleteById(Integer id) throws DeleteException;

    /**
     * 通过id list删除
     * @param idList    examOperation id list
     * @return          是否删除成功
     */
    Boolean deleteByIdList(List<Integer> idList) throws DeleteException;
}
