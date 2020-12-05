package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.IManagerService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

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
}
