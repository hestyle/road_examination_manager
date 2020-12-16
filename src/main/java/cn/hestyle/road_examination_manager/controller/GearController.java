package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Gear;
import cn.hestyle.road_examination_manager.service.IGearService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @PostMapping("/modifyGearInfo.do")
    public ResponseResult<Void> handleModifyCandidateBaseInfo(@RequestParam("newGearJsonData") String newBaseInfoJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newBaseInfoJsonData转成json，取出新baseInfo的各个属性
        ObjectMapper objectMapper = new ObjectMapper();
        Gear newGear = null;
        try {
            newGear = objectMapper.readValue(newBaseInfoJsonData, Gear.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "修改失败，信息格式不正确！");
        }
        if (iGearService.modifyBaseInfo(newGear)) {
            return new ResponseResult<>(SUCCESS, "基本信息修改保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, "修改保存失败，原因未知！");
        }
    }

}
