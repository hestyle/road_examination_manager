package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;

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
}
