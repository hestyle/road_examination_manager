package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.AddManagerDataErrorException;
import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;

import static cn.hestyle.road_examination_manager.controller.BaseController.FAILURE;
import static cn.hestyle.road_examination_manager.controller.BaseController.SUCCESS;

@RestController
@RequestMapping("/exam")
public class ExamController extends BaseController {
    @Autowired
    IExamService examService;

    @GetMapping("/findExamInfoByAdmissionNo.do")
    public ResponseResult<Exam> handleFindExamInfoByAdmissionNo(@RequestParam(name = "admissionNo") String admissionNo,
                                                          HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        Exam exam = examService.findByAdmissionNo(admissionNo);
        if(exam == null){
            return new ResponseResult<>(FAILURE, "查询失败，未找到该准考证号对应的考试信息！");
        }
        return new ResponseResult<>(SUCCESS, "查询成功！", exam);
    }

    @PostMapping("/findByPage.do")
    public ResponseResult<List<Exam>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex,
                                                       @RequestParam("pageSize") Integer pageSize,
                                                       HttpSession session) {
        // 判断是否已经登录过
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        List<Exam> examList = examService.findByPage(pageIndex, pageSize);
        Integer pageCount = (examService.getExamCount() + pageSize - 1) / pageSize;
        return new ResponseResult<List<Exam>>(SUCCESS, pageCount, examList, "查询成功！");
    }

    @PostMapping("add.do")
    public ResponseResult<Exam> handleAdd(@RequestParam(name = "newExamJsonData") String newExamJsonData,
                                          HttpSession session){
        // 判断是否已经登录过
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        // 将newManagerData转成json，取出新manager账号的各个属性
        ObjectMapper objectMapper = new ObjectMapper();
        Exam newExam = null;
        try {
            newExam = objectMapper.readValue(newExamJsonData, Exam.class);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, new AddManagerDataErrorException("新考试信息格式不正确！"));
        }
        if(examService.add(newExam)){
            return new ResponseResult<Exam>(SUCCESS, "新增考试信息成功！", newExam);
        }
        return new ResponseResult<>(FAILURE, "新增考试信息失败！");

    }


    @PostMapping("/deleteByAdmissionNo.do")
    public ResponseResult<Void> handleDeleteByAdmissionNo(@RequestParam("admissionNo") String admissionNo,
                                                          HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        if(examService.deleteByAdmissionNo(admissionNo)){
            return new ResponseResult<>(SUCCESS, "删除成功！");
        }
        else return new ResponseResult<>(FAILURE, "删除失败，原因未知！");
    }

    @PostMapping("/modifyExamInfo.do")
    public ResponseResult<Void> handlemMdifyExamInfo(@RequestParam("newExamInfoJsonData") String newExamInfoJsonData,
                                                     HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Exam newExam = null;
        try {
            newExam = objectMapper.readValue(newExamInfoJsonData, Exam.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "修改失败，信息格式不正确！");
        }

        if(examService.modifyExamInfo(newExam)){
            return new ResponseResult<>(SUCCESS, "考试信息修改保存成功！");
        }
        return new ResponseResult<>(FAILURE, "修改保存失败，原因未知！");
    }
}
