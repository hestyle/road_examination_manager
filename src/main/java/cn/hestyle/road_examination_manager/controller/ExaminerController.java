package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.controller.exception.RequestException;
import cn.hestyle.road_examination_manager.entity.Candidate;
import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.service.ICandidateService;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.service.IExaminerService;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/examiner")
public class ExaminerController extends BaseController{
    /**允许上传文件的名称*/
    public static final String UPLOAD_DIR_NAME = "upload/image";

    /**上传文件的大小*/
    public static final long FILE_MAX_SIZE = 5 * 1024 * 1024;

    /**允许上传的文件类型*/
    public static final List<String> FILE_CONTENT_TYPES = new ArrayList<>();

    static {
        FILE_CONTENT_TYPES.add("image/jpeg");
    }

    @Autowired
    IExaminerService iExaminerService;
    @Autowired
    IExamService iExamService;
    @Autowired
    ICandidateService iCandidateService;


    @PostMapping("/login.do")
    public ResponseResult<Examiner> handleLogin(@RequestParam("id") String id, @RequestParam("password") String password, HttpSession session) {
        // 执行业务端的业务
        Examiner examiner = iExaminerService.login(id, password);
        // 将用户名发到session中，保存到服务端
        session.setAttribute("id", examiner.getId());
        session.setMaxInactiveInterval(60 * 60 * 2);
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

    @PostMapping("findExamByExaminerId.do")
    public ResponseResult<List<Exam>> handleFindExam(@RequestParam("examinerId")String id,HttpSession session){
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }

        if(id == null){
            throw new RequestException("请输入考官信息！");
        }
        return new ResponseResult<>(SUCCESS, "查询成功！", iExamService.findByExaminerId(id));
    }

    @PostMapping("/uploadImage.do")
    public ResponseResult<String> handleUpload(@RequestParam("file") MultipartFile file, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username") && null==session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        // 检查文件类型、大小
        if (file.isEmpty()) {
            return new ResponseResult<>(FAILURE, "上传失败，没有选择上传的文件，或选中的文件为空！");
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            return new ResponseResult<>(FAILURE, "上传失败，上传的文件超过5MB！");
        }
        if (!FILE_CONTENT_TYPES.contains(file.getContentType())) {
            return new ResponseResult<>(FAILURE, "上传失败，文件类型非法，只允许上传jpg文件！");
        }
        // 检查保存上传文件的文件夹存在(一个是在resource一个是在target下)
        String pathNameTemp = null;
        try {
            pathNameTemp = ResourceUtils.getURL("classpath:").getPath() + "static/upload/image";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String pathNameTruth = pathNameTemp.replace("target", "src").replace("classes", "main/resources");
        File parentTemp = new File(pathNameTemp);
        File parentTruth = new File(pathNameTruth);
        if (!parentTemp.exists()) {
            parentTemp.mkdirs();
        }
        if (!parentTruth.exists()) {
            parentTruth.mkdirs();
        }
        // 重新命令、保存上传文件
        String suffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'));
        String saveFileName = System.currentTimeMillis() + "" + (new Random().nextInt(90000000) + 10000000) + suffix;
        File destTemp = new File(parentTemp, saveFileName);
        File destTruth = new File(parentTruth, saveFileName);
        try {
            // 写到target，再copy至resource
            file.transferTo(destTemp);
            FileCopyUtils.copy(destTemp, destTruth);
            System.err.println("文件上传完成！");
        } catch (Exception e) {
            e.printStackTrace();
            return new  ResponseResult<>(FAILURE, "上传失败，发生未知异常！");
        }
        String imagePath = "/road_examination_manager/" + UPLOAD_DIR_NAME + "/" + saveFileName;
        return new ResponseResult<String>(SUCCESS, "上传成功", imagePath);
    }

    @PostMapping("/findCandidateById.do")
    public ResponseResult<Candidate> handleFindCandidateById(@RequestParam("candidateId")String candidateId, HttpSession session){
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        if(candidateId == null){
            throw new RequestException("请输入考生信息");
        }
        return new ResponseResult<>(SUCCESS, "查询成功！", iCandidateService.findById(candidateId));
    }
}
