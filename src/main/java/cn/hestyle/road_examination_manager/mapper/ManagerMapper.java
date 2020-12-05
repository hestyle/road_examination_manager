package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Manager;
import org.apache.ibatis.annotations.Mapper;

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
}
