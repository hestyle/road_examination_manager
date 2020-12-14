package cn.hestyle.road_examination_manager.controller;


import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.entity.ExamTemplate;
import cn.hestyle.road_examination_manager.service.IExamItemService;
import cn.hestyle.road_examination_manager.service.IExamTemplateService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/examTemplate")
public class ExamTemplateController extends BaseController {
    @Autowired
    private IExamTemplateService examTemplateService;
    @Autowired
    IExamItemService examItemService;

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

    @PostMapping("/del.do")
    public ResponseResult<Void> handleDelById(@RequestParam("id")String id, HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        if(examTemplateService.delById(id)){
            return new ResponseResult<>(SUCCESS, "删除成功！");
        }else {
            return new ResponseResult<>(FAILURE, "删除失败！");
        }
    }

    @PostMapping("/findAllExamItem.do")
    public ResponseResult<List<ExamItem>> handleFindAllExamItem(HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        List<ExamItem> examItemList = examTemplateService.findAllExamItem();
        return new ResponseResult<>(SUCCESS, "查询成功！", examItemList);
    }
    @PostMapping("/add.do")
    public ResponseResult<Void> handleAdd(@RequestParam(name = "jsonData") String jsonData,
                                          HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "修改失败，信息格式不正确！");
        }

        String name = (String) map.get("name");
        Integer score = Integer.parseInt((String) map.get("score"));
        String type = (String) map.get("type");
        String examItemIds = (String) map.get("add_examItemIds");
        List<ExamItem> examItemList = examItemService.findByIdsString(examItemIds);
        if(name==null || score==null || type==null || examItemIds==null
                || name=="" || type=="" || examItemIds=="" || examItemList.size() == 0 || score<0 || score>100){
            return new ResponseResult<>(FAILURE, "请输入正确的数据！");
        }

        if(examTemplateService.add(name, score, type, examItemList)){
            return new ResponseResult<>(SUCCESS, "添加成功！");
        }else {
            return new ResponseResult<>(FAILURE, "添加失败，请稍后再试");
        }
    }


    @PostMapping("/findByExamTemplateId.do")
    public ResponseResult<ExamTemplate> handleFindByExamTemplateId(@RequestParam ("examTemplateId") String examTemplateId,
                                                   HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！管理员或考官未登录！请先进行登录");
        }

        return new ResponseResult<ExamTemplate>(SUCCESS, "查询成功", examTemplateService.findByExamTemplateId(examTemplateId));
    }



}
