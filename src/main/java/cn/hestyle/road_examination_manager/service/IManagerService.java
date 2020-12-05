package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.exception.ManagerNotFoundException;
import cn.hestyle.road_examination_manager.service.exception.PasswordNotMatchException;

/**
 * manager service层接口
 */
public interface IManagerService {

    /**
     * 通过username、password登录
     * @param username  username
     * @param password  password
     * @return          manager账号
     */
    Manager login(String username, String password) throws ManagerNotFoundException, PasswordNotMatchException;
}
