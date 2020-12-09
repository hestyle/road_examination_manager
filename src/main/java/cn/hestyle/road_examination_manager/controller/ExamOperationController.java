package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.service.IExamOperationService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
        // 将newExamOperationJsonData转成json，然后转成ExamOperation对象
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
    public ResponseResult<ExamOperation> handleFindByName(@RequestParam(value = "name", defaultValue = "") String name, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<ExamOperation>(SUCCESS, "查询成功！", examOperationService.findByName(name));
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

    @PostMapping("/findByIdsString.do")
    public ResponseResult<List<ExamOperation>> handleFindByIdsString(@RequestParam(value = "idsString", defaultValue = "") String idsString, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<List<ExamOperation>>(SUCCESS, "查询成功！", examOperationService.findByIdsString(idsString));
    }

    @PostMapping("/findByIdList.do")
    public ResponseResult<List<ExamOperation>> handleFindByIdList(@RequestParam(value = "idListJsonString", defaultValue = "") String idListJsonString, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        // 将idListJsonString转成json，然后转成List<Integer>
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> idList = null;
        try {
            idList = objectMapper.readValue(idListJsonString, new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "idList数据格式不正确！");
        }
        return new ResponseResult<List<ExamOperation>>(SUCCESS, "查询成功！", examOperationService.findByIdList(idList));
    }

    @PostMapping("/modify.do")
    public ResponseResult<Void> handleModify(@RequestParam("newExamOperationJsonData") String newExamOperationJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newExamOperationJsonData转成json，然后转成ExamOperation对象
        ObjectMapper objectMapper = new ObjectMapper();
        ExamOperation examOperation = null;
        try {
            examOperation = objectMapper.readValue(newExamOperationJsonData, ExamOperation.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "ExamOperation数据格式不正确！");
        }
        if (examOperationService.modify(examOperation)) {
            return new ResponseResult<Void>(SUCCESS, "保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, examOperation.getName() + "操作项修改保存失败，原因未知！");
        }
    }
}
