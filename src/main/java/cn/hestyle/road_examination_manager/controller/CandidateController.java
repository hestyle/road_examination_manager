package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.exception.CandidateNotFoundException;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
@RequestMapping("/candidate")
public class CandidateController extends BaseController{
        @Autowired
        ICandidateService iCandidateService;


        @PostMapping("/candidate_add.do")
        public String handleAdd(@RequestParam("newCandidateJsonData")String newCandidateJsonData,
                                RedirectAttributes attributes, HttpSession session) {
            // 判断是否已经登录过
            if (null == session.getAttribute("username")) {
                throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
            }
            //判断库中是否存在相同身份信息的考生
            ObjectMapper objectMapper = new ObjectMapper();
            Candidate candidate = null;
            try {
                candidate = objectMapper.readValue(newCandidateJsonData, Candidate.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            //添加到数据库
            iCandidateService.addCandidate(candidate);
            //返回消息
            attributes.addFlashAttribute("message","考生添加成功！");
            //返回考生列表界面
            return "redirect:/candidate/table.html";
        }

        @PostMapping("/candidate_del.do")
        public String handleDel(@RequestParam("id")String id, HttpSession session){
            // 判断是否已经登录过
            if (null == session.getAttribute("username")) {
                throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
            }
            if(iCandidateService.findById(id)==null){
                throw new CandidateNotFoundException("考生信息不存在，请重新确认");
            }
            if(iCandidateService.findById(id).getIsDel()==1){
                throw new CandidateNotFoundException("该考生已删除，不能重复删除！");
            }
            iCandidateService.delCandidate(id);
            return "redirect:/examiner";
        }

        @PostMapping("/modifyOtherBaseInfo.do")
        public ResponseResult<Void> handleModifyOtherBaseInfo(@RequestParam("newBaseInfoJsonData") String newBaseInfoJsonData, HttpSession session) {
            // 判断是否已经登录过
            if (null == session.getAttribute("username")) {
                throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
            }
            // 将newBaseInfoJsonData转成json，取出新baseInfo的各个属性
            ObjectMapper objectMapper = new ObjectMapper();
            Candidate newCandidate = null;
            try {
                newCandidate = objectMapper.readValue(newBaseInfoJsonData, Candidate.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return new ResponseResult<>(FAILURE, "修改失败，信息格式不正确！");
            }
            if (iCandidateService.modifyBaseInfo(newCandidate)) {
                return new ResponseResult<>(SUCCESS, "基本信息修改保存成功！");
            } else {
                return new ResponseResult<>(FAILURE, "修改保存失败，原因未知！");
            }
        }

        @PostMapping("/findByPage.do")
        public ResponseResult<List<Candidate>> handleFindByPage(@RequestParam("pageIndex") Integer pageIndex, @RequestParam("pageSize") Integer pageSize, HttpSession session) {
            // 判断是否已经登录过
            if (null == session.getAttribute("username")) {
                throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
            }
            List<Candidate> candidateList = iCandidateService.findByPage(pageIndex, pageSize);
            Integer pageCount = (iCandidateService.getCandidateCount() + pageSize - 1) / pageSize;
            return new ResponseResult<List<Candidate>>(SUCCESS, pageCount, candidateList, "查询成功！");
        }

        @PostMapping("/deleteCandidatesById.do")
        public ResponseResult<Void> handleDeleteCandidatesByUsername(@RequestParam("idListJsonData") String idListJsonData, HttpSession session) {
            // 判断是否已经登录过
            if (null == session.getAttribute("username")) {
                throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
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
            if (iCandidateService.deleteCandidateByIdList(idList)) {
                return new ResponseResult<>(SUCCESS, "修改保存成功！");
            } else {
                return new ResponseResult<>(FAILURE, "批量删除失败，原因未知！");
            }
        }

}
