package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.exception.*;

import java.util.List;

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

    /**
     * 增加新manager
     * @param manager   新manger账号
     * @return          是否保存成功
     * @throws ManagerAddFailedException  插入发生时数据库异常
     */
    Boolean add(Manager manager) throws ManagerAddFailedException;

    /**
     * 分页查询manager
     * @param pageIndex     页码（从1开始）
     * @param pageSize      一页大小
     * @return              manager list
     */
    List<Manager> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException;

    /**
     * 获取manager的数量
     * @return      manager的数量
     */
    Integer getManagerCount();

    /**
     * 修改manager账号密码
     * @param username      待修改账号的username
     * @param newPassword   新密码
     * @param reNewPassword re新密码
     * @return              是否修改成功
     */
    Boolean modifyPassword(String username, String newPassword, String reNewPassword) throws UpdateException;
}
