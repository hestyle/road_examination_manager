package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.mapper.ManagerMapper;
import cn.hestyle.road_examination_manager.service.IManagerService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public Boolean add(Manager manager) throws ManagerAddFailedException{
        // 检查manager.username是否为空
        if (null == manager.getUsername()) {
            throw new ManagerAddFailedException("账号保存失败，未设置username！");
        }
        // 检查username是否已被注册（username是主键）
        if (null != managerMapper.findByUsername(manager.getUsername())) {
            throw new ManagerAddFailedException("账号保存失败，用户名" + manager.getUsername() + " 已经被注册！");
        }
        if (manager.getName() == null || manager.getName().length() < 6 || manager.getName().length() > 20) {
            throw new InsertException("增加失败，name字段长度非法，请控制在6-20位！");
        }
        if (manager.getAge() != null && (manager.getAge() < 1 || manager.getAge() > 120)) {
            throw new InsertException("增加失败，age必须大于零，且小于120！");
        }
        if (manager.getGender() != null && !"男".equals(manager.getGender()) && !"女".equals(manager.getGender())) {
            throw new InsertException("增加失败，gender只能为【男】或者【女】！");
        }
        if (manager.getPhoneNumber() != null && (manager.getPhoneNumber().length() < 8 || manager.getName().length() > 11)) {
            throw new InsertException("增加失败，phoneNumber字段长度非法，请控制在8-11位！");
        }
        if (manager.getIsDel() != null && manager.getIsDel() != 0 && manager.getIsDel() != 1) {
            throw new InsertException("增加失败，isDel字段必须为0或1！");
        }
        // 受影响的行数==1，说明插入成功
        try {
            return 1 == managerMapper.addNew(manager);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagerAddFailedException("账号保存失败，数据库发生未知异常！");
        }
    }

    @Override
    public List<Manager> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
        // 检查页码是否合法
        if (pageIndex < 1) {
            throw new PageFindErrorException("页码 " + pageIndex + " 非法，必须大于0！");
        }
        // 检查页大小是否合法
        if (pageSize < 1) {
            throw new PageFindErrorException("页大小 " + pageSize + " 非法，必须大于0！");
        }
        // 调用持久层mapper
        try {
            return managerMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getManagerCount() {
        // 调用持久层mapper
        try {
            return managerMapper.getManagerCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modifyPassword(String username, String newPassword, String reNewPassword)  throws UpdateException {
        // 通过username查询manager
        Manager manager = managerMapper.findByUsername(username);
        if (null == manager) {
            throw new UpdateException("密码修改失败，" + username + " 用户未注册！");
        }
        // 判断两次密码是否一致
        if (null == newPassword || !newPassword.equals(reNewPassword)) {
            throw new UpdateException("密码修改失败！两次密码不一致，请重新输入！");
        }
        // 判断密码长度是否合法
        if (newPassword.length() < 6 || newPassword.length() > 20) {
            throw new UpdateException("密码长度非法，请控制在6-20位！");
        }
        manager.setPassword(newPassword);
        try {
            return 1 == managerMapper.update(manager);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("密码修改失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modifyBaseInfo(Manager manager) throws UpdateException {
        // 通过username查询manager
        Manager managerData = managerMapper.findByUsername(manager.getUsername());
        if (null == managerData) {
            throw new UpdateException("修改失败，" + manager.getUsername() + " 用户未注册！");
        }
        // 提取出需要更新的字段，并且判断字段是否合法
        if (manager.getName() != null) {
            if (manager.getName().length() < 6 || manager.getName().length() > 20) {
                throw new UpdateException("修改失败，name字段长度非法，请控制在6-20位！");
            }
            managerData.setName(manager.getName());
        }
        if (manager.getAge() != null) {
            if (manager.getAge() < 1) {
                throw new UpdateException("修改失败，age必须大于零！");
            }
            managerData.setAge(manager.getAge());
        }
        if (manager.getGender() != null) {
            if (!"男".equals(manager.getGender()) && !"女".equals(manager.getGender())) {
                throw new UpdateException("修改失败，gender只能为【男】或者【女】！");
            }
            managerData.setGender(manager.getGender());
        }
        if (manager.getPhoneNumber() != null) {
            if (manager.getPhoneNumber().length() < 8 || manager.getName().length() > 11) {
                throw new UpdateException("修改失败，phoneNumber字段长度非法，请控制在8-11位！");
            }
            managerData.setPhoneNumber(manager.getPhoneNumber());
        }
        if (manager.getIsDel() != null) {
            if (manager.getIsDel() != 0 && manager.getIsDel() != 1) {
                throw new UpdateException("修改失败，isDel字段必须为0或1！");
            }
            managerData.setPhoneNumber(manager.getPhoneNumber());
        }
        try {
            return 1 == managerMapper.update(managerData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("信息修改失败，数据库发生未知异常！");
        }
    }

    @Override
    public Manager findManagerByUsername(String username) throws ManagerNotFoundException {
        Manager manager = managerMapper.findByUsername(username);
        // 判断用户名是否存在账号
        if (null == manager) {
            throw new ManagerNotFoundException("查找! 用户名 " + username + " 未注册!");
        }
        // 清除password
        manager.setPassword(null);
        return manager;
    }

    @Override
    public Boolean deleteManagerByUsername(String username) throws DeleteException {
        Manager manager = managerMapper.findByUsername(username);
        // 判断用户名是否存在账号
        if (null == manager) {
            throw new ManagerNotFoundException("删除失败! 用户名 " + username + " 未注册!");
        }
        // 判断账号是否已经删除
        if (0 != manager.getIsDel()) {
            throw new DeleteException("该账号已删除，无需再次删除！");
        }
        try {
            // 删除并不是真的删除，只是将is_des置1
            manager.setIsDel(1);
            return 1 == managerMapper.update(manager);
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException("删除失败，数据库发生未知异常");
        }
    }

    @Override
    public Boolean deleteManagersByUsernameList(List<String> usernameList) throws DeleteException {
        // 首先验证所有username是否都注册
        List<Manager> managerList = new ArrayList<>();
        for (String username : usernameList) {
            Manager manager = managerMapper.findByUsername(username);
            if (manager == null) {
                throw new DeleteException("批量删除失败，" + username + " 未注册！");
            } else {
                managerList.add(manager);
            }
        }
        // 批量删除
        try {
            for (Manager manager : managerList) {
                manager.setIsDel(1);
                managerMapper.update(manager);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            throw new DeleteException("批量删除失败，数据库发生未知异常！");
        }
    }
}
