package cn.hestyle.road_examination_manager.controller;


import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.ExamTemplate;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.IExamTemplateService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/examTemplate")
public class ExamTemplateController extends BaseController {
    @Autowired
    private IExamTemplateService examTemplateService;

    @PostMapping("/findByPage.do")
    public ResponseResult<List<ExamTemplate>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex,
                                                               @RequestParam("pageSize") Integer pageSize,
                                                               HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        List<ExamTemplate> examTemplateList = examTemplateService.findByPage(pageIndex, pageSize);
        Integer count = examTemplateService.getExamTemplateCount();
        return new ResponseResult<List<ExamTemplate>>(SUCCESS, count, examTemplateList, "查询成功！");
    }

}
