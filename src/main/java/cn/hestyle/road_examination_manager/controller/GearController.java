package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Gear;
import cn.hestyle.road_examination_manager.service.IGearService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/gear")
public class GearController extends BaseController{

    @Autowired
    IGearService iGearService;

    @PostMapping("/findByPage.do")
    public ResponseResult<List<Gear>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        List<Gear> gearList = iGearService.findByPage(pageIndex, pageSize);
        Integer count = iGearService.getGearCount();
        return new ResponseResult<>(SUCCESS, count, gearList, "查询成功！");
    }
}
