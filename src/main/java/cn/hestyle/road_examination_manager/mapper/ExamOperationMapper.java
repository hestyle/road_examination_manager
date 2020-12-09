package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户持久层
 * @author hestyle
 */
@Mapper
public interface ExamOperationMapper {
    /**
     * 通过id查询操作项
     * @param id        操作项id
     * @return          exam operation
     */
    ExamOperation findById(Integer id);

    /**
     * 通过name查询操作项
     * @param name      操作项name
     * @return          examOperation list
     */
    ExamOperation findByName(String name);

    /**
     * 增加新examOperation
     * @param examOperation     新examOperation
     * @return                  是否插入成功
     */
    Integer addNew(ExamOperation examOperation);

    /**
     * 分页查询所有examOperation
     * @param beginIndex    id降序排列
     * @param pageSize      一页大小
     * @return              examOperation
     */
    List<ExamOperation> findByPage(Integer beginIndex, Integer pageSize);

    /**
     * 获取manager的数量
     * @return      manager的数量
     */
    Integer getExamOperationCount();

    /**
     * 更新examOperation信息
     * @param examOperation     待更新的examOperation
     * @return                  是否更新成功
     */
    Integer update(ExamOperation examOperation);
}
