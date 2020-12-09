package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.service.IExamItemService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * examItem controller
 * @author hestyle
 */
@RestController
@RequestMapping("/examItem")
public class ExamItemController extends BaseController {
    @Autowired
    private IExamItemService examItemService;

    @PostMapping("/add.do")
    public ResponseResult<ExamItem> handleAdd(@RequestParam("newExamItemJsonData") String newExamItemJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newExamItemJsonData转成json，然后转成ExamItem对象
        ObjectMapper objectMapper = new ObjectMapper();
        ExamItem examItem = null;
        try {
            examItem = objectMapper.readValue(newExamItemJsonData, ExamItem.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "新ExamItem数据格式不正确！");
        }
        if (examItemService.add(examItem)) {
            return new ResponseResult<ExamItem>(SUCCESS, "保存成功！", examItem);
        } else {
            return new ResponseResult<>(FAILURE, examItem.getName() + "操作项保存失败，原因未知！");
        }
    }

    @PostMapping("/findById.do")
    public ResponseResult<ExamItem> handleFindById(@RequestParam(value = "id", defaultValue = "0") Integer id, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<ExamItem>(SUCCESS, "查询成功！", examItemService.findById(id));
    }
}
