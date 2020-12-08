package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.service.IExamOperationService;
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

/**
 * examOperation controller
 * @author hestyle
 */
@RestController
@RequestMapping("/examOperation")
public class ExamOperationController extends BaseController {
    @Autowired
    private IExamOperationService examOperationService;

    @PostMapping("/add.do")
    public ResponseResult<ExamOperation> handleAdd(@RequestParam("newExamOperationJsonData") String newExamOperationJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newManagerData转成json，取出新manager账号的各个属性
        ObjectMapper objectMapper = new ObjectMapper();
        ExamOperation examOperation = null;
        try {
            examOperation = objectMapper.readValue(newExamOperationJsonData, ExamOperation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "新ExamOperation数据格式不正确！");
        }
        if (examOperationService.add(examOperation)) {
            return new ResponseResult<ExamOperation>(SUCCESS, "保存成功！", examOperation);
        } else {
            return new ResponseResult<>(FAILURE, examOperation.getName() + "操作项保存失败，原因未知！");
        }
    }

    @PostMapping("/findById.do")
    public ResponseResult<ExamOperation> handleFindById(@RequestParam(value = "id", defaultValue = "0") Integer id, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<ExamOperation>(SUCCESS, "查询成功！", examOperationService.findById(id));
    }

    @PostMapping("/findByName.do")
    public ResponseResult<List<ExamOperation>> handleFindByName(@RequestParam(value = "name", defaultValue = "") String name, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<List<ExamOperation>>(SUCCESS, "查询成功！", examOperationService.findByName(name));
    }

    @PostMapping("/findByPage.do")
    public ResponseResult<List<ExamOperation>> handleFindByPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        List<ExamOperation> examOperationList = examOperationService.findByPage(pageIndex, pageSize);
        Integer count = examOperationService.getExamOperationCount();
        return new ResponseResult<List<ExamOperation>>(SUCCESS, count, examOperationList, "查询成功！");
    }
}
