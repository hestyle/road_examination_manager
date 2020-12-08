package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;

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
     * @return                  examOperation list
     */
    List<ExamOperation> findByName(String name) throws FindException;

    /**
     * 分页查询所有examOperation
     * @param pageIndex     页码
     * @param pageSize      一页大小
     * @return              examOperation list
     */
    List<ExamOperation> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException;

    /**
     * 查询examOperation的数量
     * @return              examOperation的数量
     */
    Integer getExamOperationCount();
}
