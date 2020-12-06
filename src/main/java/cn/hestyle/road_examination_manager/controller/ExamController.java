package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static cn.hestyle.road_examination_manager.controller.BaseController.FAILURE;
import static cn.hestyle.road_examination_manager.controller.BaseController.SUCCESS;

@RestController
@RequestMapping("/exam")
public class ExamController {
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
}
