package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.AddManagerDataErrorException;
import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.IManagerService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * manager controller
 * @author hestyle
 */
@RestController
@RequestMapping("/manager")
public class ManagerController extends BaseController{
    @Autowired
    private IManagerService managerService;

    @PostMapping("/login.do")
    public ResponseResult<Manager> handleLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        // 执行业务端的业务
        Manager manager = managerService.login(username, password);
        // 将用户名发到session中，保存到服务端
        session.setAttribute("username", manager.getUsername());
        return new ResponseResult<>(SUCCESS, "登录成功！", manager);
    }

    @PostMapping("/add.do")
    public ResponseResult<Void> handleAdd(@RequestParam("newManageJsonData") String newManageJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newManagerData转成json，取出新manager账号的各个属性
        ObjectMapper objectMapper = new ObjectMapper();
        Manager newManager = null;
        try {
            newManager = objectMapper.readValue(newManageJsonData, Manager.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, new AddManagerDataErrorException("新管理员账号格式不正确！"));
        }
        if (managerService.add(newManager)) {
            return new ResponseResult<>(SUCCESS, newManager.getUsername() + "账号已保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, newManager.getUsername() + "账号保存失败，原因未知！");
        }
    }

    @PostMapping("/findByPage.do")
    public ResponseResult<List<Manager>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        List<Manager> managerList = managerService.findByPage(pageIndex, pageSize);
        Integer pageCount = (managerService.getManagerCount() + pageSize - 1) / pageSize;
        return new ResponseResult<List<Manager>>(SUCCESS, pageCount, managerList, "查询成功！");
    }

    @PostMapping("/modifySelfPassword.do")
    public ResponseResult<Void> handleModifySelfPassword(@RequestParam("newPassword") String newPassword, @RequestParam("reNewPassword") String reNewPassword, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        if (managerService.modifyPassword((String) session.getAttribute("username"), newPassword, reNewPassword)) {
            return new ResponseResult<>(SUCCESS, "密码修改成功！");
        } else {
            return new ResponseResult<>(FAILURE, "密码修改失败！");
        }
    }

    @PostMapping("/modifySelfBaseInfo.do")
    public ResponseResult<Void> handleModifySelfBaseInfo(@RequestParam("newBaseInfoJsonData") String newBaseInfoJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newBaseInfoJsonData转成json，取出新baseInfo的各个属性
        ObjectMapper objectMapper = new ObjectMapper();
        Manager newManager = null;
        try {
            newManager = objectMapper.readValue(newBaseInfoJsonData, Manager.class);
            // 从session中取出username
            newManager.setUsername((String) session.getAttribute("username"));
            if (managerService.modifyBaseInfo(newManager)) {
                return new ResponseResult<>(SUCCESS, "基本信息修改保存成功！");
            } else {
                return new ResponseResult<>(FAILURE, "修改保存失败，原因未知！");
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "修改失败，信息格式不正确！");
        }
    }

    @PostMapping("/findManagerByUsername/{username}")
    public ResponseResult<Manager> handleFindManagerByUsername(@PathVariable("username") String username, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 查找manager账号
        Manager manager = managerService.findManagerByUsername(username);
        return new ResponseResult<Manager>(SUCCESS, "查找成功！", manager);
    }

    @PostMapping("/resetOtherPassword.do")
    public ResponseResult<Void> handleResetOtherPassword(@RequestParam("newPasswordJsonData") String newPasswordJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newManagerData转成json，取出username、newPassword、reNewPassword三个属性
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> newPasswordDataMap = null;
        try {
            newPasswordDataMap = objectMapper.readValue(newPasswordJsonData, new TypeReference<Map<String, Object>>() {});
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "数据格式不正确！");
        }
        if (managerService.modifyPassword((String) newPasswordDataMap.get("username"), (String) newPasswordDataMap.get("newPassword"), (String) newPasswordDataMap.get("reNewPassword"))) {
            return new ResponseResult<>(SUCCESS, "密码重置成功！");
        }
        return new ResponseResult<>(FAILURE, "数据格式不正确！");
    }
}
