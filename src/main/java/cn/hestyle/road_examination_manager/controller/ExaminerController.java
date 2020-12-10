package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.service.IExaminerService;
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
import java.util.Map;

@RestController
@RequestMapping("/examiner")
public class ExaminerController extends BaseController{
    @Autowired
    IExaminerService iExaminerService;

    @PostMapping("/login.do")
    public ResponseResult<Examiner> handleLogin(@RequestParam("id") String id, @RequestParam("password") String password, HttpSession session) {
        // 执行业务端的业务
        Examiner examiner = iExaminerService.login(id, password);
        // 将用户名发到session中，保存到服务端
        session.setAttribute("id", examiner.getId());
        return new ResponseResult<>(SUCCESS, "登录成功！", examiner);
    }

    @PostMapping("/examiner_add.do")
    public ResponseResult<Void> handleAdd(@RequestParam("newExaminerJsonData")String newExaminerJsonData,
                            HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
        }
        //考生信息
        ObjectMapper objectMapper = new ObjectMapper();
        Examiner examiner = null;
        try {
            examiner = objectMapper.readValue(newExaminerJsonData, Examiner.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (iExaminerService.addExaminer(examiner)) {
            return new ResponseResult<>(SUCCESS, examiner.getId() + "账号已保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, examiner.getId() + "账号保存失败，原因未知！");
        }
    }

    @PostMapping("/findByPage.do")
    public ResponseResult<List<Examiner>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
        }
        List<Examiner> examinerList = iExaminerService.findByPage(pageIndex, pageSize);
        Integer count = iExaminerService.getExaminerCount();
        return new ResponseResult<List<Examiner>>(SUCCESS, count, examinerList, "查询成功！");
    }

    @PostMapping("/modifyExaminerBaseInfo.do")
    public ResponseResult<Void> handleModifyExaminerBaseInfo(@RequestParam("newBaseInfoJsonData") String newBaseInfoJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
        }
        // 将newBaseInfoJsonData转成json，取出新baseInfo的各个属性
        ObjectMapper objectMapper = new ObjectMapper();
        Examiner newExaminer = null;
        try {
            newExaminer = objectMapper.readValue(newBaseInfoJsonData, Examiner.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "修改失败，信息格式不正确！");
        }
        if (iExaminerService.modifyBaseInfo(newExaminer)) {
            return new ResponseResult<>(SUCCESS, "基本信息修改保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, "修改保存失败，原因未知！");
        }
    }

    @PostMapping("/deleteExaminersById.do")
    public ResponseResult<Void> handleDeleteExaminersByUsername(@RequestParam("idListJsonData") String idListJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
        }
        // 将usernameListJsonData转成String list
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> idList = null;
        try {
            idList = objectMapper.readValue(idListJsonData, new TypeReference<List<String>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "批量删除失败，信息格式不正确！");
        }
        if (iExaminerService.deleteExaminerByIdList(idList)) {
            return new ResponseResult<>(SUCCESS, "修改保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, "批量删除失败，原因未知！");
        }
    }

    @PostMapping("/resetExaminerPassword.do")
    public ResponseResult<Void> handleResetExaminerPassword(@RequestParam("newPasswordJsonData") String newPasswordJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
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
        if (iExaminerService.modifyPassword((String) newPasswordDataMap.get("id"), (String) newPasswordDataMap.get("newPassword"), (String) newPasswordDataMap.get("reNewPassword"))) {
            return new ResponseResult<>(SUCCESS, "密码重置成功！");
        }
        return new ResponseResult<>(FAILURE, "数据格式不正确！");
    }

}
