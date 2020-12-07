package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.service.IExaminerService;
import cn.hestyle.road_examination_manager.service.exception.ExaminerNotFoundException;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/examiner")
public class ExaminerController extends BaseController{
    @Autowired
    IExaminerService iExaminerService;
    @PostMapping("/examiner_list.do")
    public ResponseResult<List<Examiner>> handleList(HttpSession session) {
        // 执行业务端的业务
        List<Examiner> list = iExaminerService.examiner_list();
        // 将用户名发到session中，保存到服务端
        session.setAttribute("examiners", list);
        return new ResponseResult<>(SUCCESS, "考官查询成功！", list);
    }

    @PostMapping("/examiner_add.do")
    public String handleAdd(@RequestParam("newExaminerJsonData")String newExaminerJsonData,
                            RedirectAttributes attributes) {
        //考生信息
        ObjectMapper objectMapper = new ObjectMapper();
        Examiner examiner = null;
        try {
            examiner = objectMapper.readValue(newExaminerJsonData, Examiner.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //添加到数据库
        examiner.setIsDel(0);
        iExaminerService.addExaminer(examiner);
        //返回消息
        attributes.addFlashAttribute("message","考官添加成功！");
        //返回考官列表界面
        return "redirect:/examiner";
    }

    @PostMapping("/examiner_del.do")
    public String handleDel(@RequestParam("id")String id, RedirectAttributes attributes){

        if(iExaminerService.findById(id)==null){
            throw new ExaminerNotFoundException("没有该考官信息，删除失败！");
        }
        if(iExaminerService.findById(id).getIsDel()==1){
            throw new ExaminerNotFoundException("该考官已经删除，不能重复删除！");
        }
        iExaminerService.delExaminer(id);
        return "redirect:/examiner";

    }

    @PostMapping("/findByPage.do")
    public ResponseResult<List<Examiner>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        List<Examiner> examinerList = iExaminerService.findByPage(pageIndex, pageSize);
        Integer pageCount = (iExaminerService.getExaminerCount() + pageSize - 1) / pageSize;
        return new ResponseResult<List<Examiner>>(SUCCESS, pageCount, examinerList, "查询成功！");
    }

}
