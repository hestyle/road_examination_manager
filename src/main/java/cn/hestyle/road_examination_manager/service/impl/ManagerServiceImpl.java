package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.mapper.ManagerMapper;
import cn.hestyle.road_examination_manager.service.IManagerService;
import cn.hestyle.road_examination_manager.service.exception.ManagerNotFoundException;
import cn.hestyle.road_examination_manager.service.exception.PasswordNotMatchException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * manager service层实现类
 * @author hestyle
 */
@Service
public class ManagerServiceImpl implements IManagerService {
    @Resource
    private ManagerMapper managerMapper;

    @Override
    public Manager login(String username, String password) throws ManagerNotFoundException, PasswordNotMatchException {
        Manager manager = managerMapper.findByUsername(username);
        // 判断用户名是否存在账号
        if (null == manager) {
            throw new ManagerNotFoundException("登录失败!您尝试登录的用户名" + username + "不存在!");
        }
        // 判断用户账号是否被注销
        if (1 == manager.getIsDel()) {
            throw new ManagerNotFoundException("登录失败!您尝试登录的用户名" + username + "已经被注销!");
        }
        // 判断密码是否正确
        if (password != null && password.equals(manager.getPassword())) {
            // 将password置空
            manager.setPassword(null);
            return manager;
        } else {
            // 比对失败，抛出异常，密码不匹配
            throw new PasswordNotMatchException("登录失败!您尝试登录的用户密码错误!");
        }
    }
}
