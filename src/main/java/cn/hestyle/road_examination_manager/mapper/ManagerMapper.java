package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户持久层
 * @author hestyle
 */
@Mapper
public interface ManagerMapper {
    /**
     * 通过用户名查询manager
     * @param username  用户名
     * @return          manager
     */
    Manager findByUsername(String username);

    /**
     * 增加新manager
     * @param manager   新manger账号
     * @return          是否插入成功
     */
    Integer addNew(Manager manager);

    /**
     * 分页查询所有manager
     * @param beginIndex    username降序排列
     * @param pageSize      一页大小
     * @return              manager list
     */
    List<Manager> findByPage(Integer beginIndex, Integer pageSize);

    /**
     * 获取manager的数量
     * @return      manager的数量
     */
    Integer getManagerCount();

    /**
     * 更新manager账号信息
     * @param manager   待更新的manager账号
     * @return          是否更新成功
     */
    Integer update(Manager manager);
}
