package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.entity.Manager;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.IExaminerService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
        @PostMapping("/candidate_list.do")
        public ResponseResult<List<Candidate>> handleList(HttpSession session) {
            // 执行业务端的业务
            List<Candidate> list = iCandidateService.candidate_list();
            // 将用户名发到session中，保存到服务端
            session.setAttribute("candidates", list);
            return new ResponseResult<>(SUCCESS, "考生查询成功！", list);
        }

        @PostMapping("/candidate_add.do")
        public String handleAdd(@RequestParam("id")String id, @RequestParam("photo_path")String photoPath,
                                @RequestParam("phone_number")String phoneNumber,
                                @RequestParam("name")String name,
                                @RequestParam("age")Integer age,
                                @RequestParam("gender")String gender,
                                @RequestParam("driver_school")String driverSchool,
                                RedirectAttributes attributes) {
            //考生信息
            Candidate candidate = new Candidate();
            candidate.setId(id);
            candidate.setAge(age);
            candidate.setDriverSchool(driverSchool);
            candidate.setGender(gender);
            candidate.setName(name);
            candidate.setPhotoPath(photoPath);
            candidate.setPhoneNumber(phoneNumber);
            candidate.setIsDel(0);
            //添加到数据库
            iCandidateService.addCandidate(candidate);
            //返回消息
            attributes.addFlashAttribute("message","考生添加成功！");
            //返回考生列表界面
            return "redirect:/examiner";
        }
}
