package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.AddManagerDataErrorException;
import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.controller.exception.RequestException;
import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

import static cn.hestyle.road_examination_manager.controller.BaseController.FAILURE;
import static cn.hestyle.road_examination_manager.controller.BaseController.SUCCESS;

@RestController
@RequestMapping("/exam")
public class ExamController extends BaseController {
    @Autowired
    IExamService examService;
    @Autowired
    ICandidateService candidateService;

    @PostMapping("/findExamInfoByAdmissionNo.do")
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
                                                       @RequestParam(name = "admissionNo", required = false) String admissionNo,
                                                       HttpSession session) {
        // 判断是否已经登录过
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        List<Exam> examList = null;
        Integer count = 0;
        examList = examService.findByPage(pageIndex, pageSize, admissionNo);
        count = examService.getExamCount(admissionNo);

//        Integer pageCount = (examService.getExamCount() + pageSize - 1) / pageSize;
        return new ResponseResult<List<Exam>>(SUCCESS, count, examList, "查询成功！");
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


    @GetMapping("/deleteByAdmissionNo.do")
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
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
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

    @PostMapping("/getDetailInfoByAdmissionNo/{admissionNo}")
    public ResponseResult<Map<String, Object>> handleGetDetailInfoByAdmissionNo(@PathVariable("admissionNo") String admissionNo,
                                                                   HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }

        Map<String, Object> data= examService.findDetailInfoByAdmissionNo(admissionNo);

        return new ResponseResult<Map<String, Object>>(SUCCESS, "查询成功", data);
    }

    @PostMapping("/getExamItemsByAdmissionNo/{admissionNo}")
    public ResponseResult<Map<String, Object>> handleGetExamItemsInfoByAdmissionNo(@PathVariable("admissionNo") String admissionNo,
                                                                                HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }

        Map<String, Object> data= examService.findExamItemsInfoByAdmissionNo(admissionNo);

        return new ResponseResult<Map<String, Object>>(SUCCESS, "查询成功", data);
    }

    @PostMapping("/getExamLightItemsInfoByAdmissionNo/{admissionNo}")
    public ResponseResult<Map<String, Object>> handleGetExamLightTemplateInfoByAdmissionNo(@PathVariable("admissionNo") String admissionNo,
                                                                                      HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }

        Map<String, Object> data= examService.findExamLightItemsInfoByAdmissionNo(admissionNo);

        return new ResponseResult<Map<String, Object>>(SUCCESS, "查询成功", data);
    }

    @PostMapping("/getCandidateInfoByCandidateId/{candidateId}")
    public ResponseResult<Candidate> handleGetCandidateInfoByCandidateId(@PathVariable("candidateId") String id,
                                                                         HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }

        Candidate candidate = candidateService.findById(id);
        if(candidate == null){
            return new ResponseResult<>(FAILURE, "考生信息不存在！");
        }

        return new ResponseResult<Candidate>(SUCCESS, "查询成功！", candidate);
    }

    /*
    需要参数 考生id 道路考生模板id 灯光考生模板id
     */
    @PostMapping("/generateExamInfo.do")
    public ResponseResult<Exam> handleGenerateExamInfo(@RequestParam(name = "jsonData") String jsonData,
                                                       HttpSession session){

        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {});
        }catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "数据格式不正确！");
        }

        String candidateId = (String) map.get("candidateId");
        String examTemplateId = (String) map.get("examTemplateId");
        String lightExamTemplateId = (String) map.get("lightExamTemplateId");
        String examTime = (String) map.get("examTime");

        if(candidateId==null || examTemplateId==null || lightExamTemplateId==null || examTime==null
                || candidateId=="" || examTemplateId=="" || lightExamTemplateId=="" || examTime==""){
            return new ResponseResult<>(FAILURE, "请输入正确的数据！");
        }


        Exam exam = examService.generateExamInfo(candidateId, examTemplateId, lightExamTemplateId, examTime);
        return new ResponseResult<Exam>(SUCCESS, "考试信息生成成功！", exam);
    }

    @PostMapping("/findExamByExaminerId.do")
    public ResponseResult<List<Exam>> handleFindExamByExaminerId(@RequestParam("examinerId") String examinerId,
                                                                 HttpSession session){
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }

        if(examinerId == null){
            throw new RequestException("请输入考官信息！");
        }
        return new ResponseResult<>(SUCCESS, "查询成功！", examService.findByExaminerId(examinerId));
    }
}
