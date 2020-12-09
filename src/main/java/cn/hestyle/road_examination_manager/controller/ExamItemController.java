package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.service.IExamItemService;
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
import java.util.Random;

/**
 * examItem controller
 * @author hestyle
 */
@RestController
@RequestMapping("/examItem")
public class ExamItemController extends BaseController {
    /**允许上传文件的名称*/
    public static final String UPLOAD_DIR_NAME = "upload/audio";

    /**上传文件的大小*/
    public static final long FILE_MAX_SIZE = 5 * 1024 * 1024;

    /**允许上传的文件类型*/
    public static final List<String> FILE_CONTENT_TYPES = new ArrayList<>();

    static {
        FILE_CONTENT_TYPES.add("audio/mpeg");
    }

    @Autowired
    private IExamItemService examItemService;

    @PostMapping("/add.do")
    public ResponseResult<ExamItem> handleAdd(@RequestParam("newExamItemJsonData") String newExamItemJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newExamItemJsonData转成json，然后转成ExamItem对象
        ObjectMapper objectMapper = new ObjectMapper();
        ExamItem examItem = null;
        try {
            examItem = objectMapper.readValue(newExamItemJsonData, ExamItem.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "新ExamItem数据格式不正确！");
        }
        if (examItemService.add(examItem)) {
            return new ResponseResult<ExamItem>(SUCCESS, "保存成功！", examItem);
        } else {
            return new ResponseResult<>(FAILURE, examItem.getName() + "操作项保存失败，原因未知！");
        }
    }

    @PostMapping("/findById.do")
    public ResponseResult<ExamItem> handleFindById(@RequestParam(value = "id", defaultValue = "0") Integer id, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<ExamItem>(SUCCESS, "查询成功！", examItemService.findById(id));
    }

    @PostMapping("/findByName.do")
    public ResponseResult<ExamItem> handleFindByName(@RequestParam(value = "name", defaultValue = "") String name, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<ExamItem>(SUCCESS, "查询成功！", examItemService.findByName(name));
    }

    @PostMapping("/findByPage.do")
    public ResponseResult<List<ExamItem>> handleFindByPage(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        List<ExamItem> examItemList = examItemService.findByPage(pageIndex, pageSize);
        Integer count = examItemService.getExamItemCount();
        return new ResponseResult<List<ExamItem>>(SUCCESS, count, examItemList, "查询成功！");
    }

    @PostMapping("/findByIdsString.do")
    public ResponseResult<List<ExamItem>> handleFindByIdsString(@RequestParam(value = "idsString", defaultValue = "") String idsString, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        return new ResponseResult<List<ExamItem>>(SUCCESS, "查询成功！", examItemService.findByIdsString(idsString));
    }

    @PostMapping("/findByIdList.do")
    public ResponseResult<List<ExamItem>> handleFindByIdList(@RequestParam(value = "idListJsonString", defaultValue = "") String idListJsonString, HttpSession session) {
        // 判断是否已经登录过(有两种可能管理员、考官)
        if (null == session.getAttribute("username") && null == session.getAttribute("id")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员或考官登录！");
        }
        // 将idListJsonString转成json，然后转成List<Integer>
        ObjectMapper objectMapper = new ObjectMapper();
        List<Integer> idList = null;
        try {
            idList = objectMapper.readValue(idListJsonString, new TypeReference<List<Integer>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "idList数据格式不正确！");
        }
        return new ResponseResult<List<ExamItem>>(SUCCESS, "查询成功！", examItemService.findByIdList(idList));
    }

    @PostMapping("/modify.do")
    public ResponseResult<Void> handleModify(@RequestParam("newExamItemJsonData") String newExamItemJsonData, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 将newExamItemJsonData转成json，然后转成ExamItem对象
        ObjectMapper objectMapper = new ObjectMapper();
        ExamItem examItem = null;
        try {
            examItem = objectMapper.readValue(newExamItemJsonData, ExamItem.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseResult<>(FAILURE, "ExamOperation数据格式不正确！");
        }
        if (examItemService.modify(examItem)) {
            return new ResponseResult<Void>(SUCCESS, "保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, examItem.getName() + "考试项修改保存失败，原因未知！");
        }
    }

    @PostMapping("/uploadVoice.do")
    public ResponseResult<String> handleUpload(@RequestParam("file") MultipartFile file, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        // 检查文件类型、大小
        if (file.isEmpty()) {
            return new ResponseResult<>(FAILURE, "上传失败，没有选择上传的文件，或选中的文件为空！");
        }
        if (file.getSize() > FILE_MAX_SIZE) {
            return new ResponseResult<>(FAILURE, "上传失败，上传的文件超过5MB！");
        }
        if (!FILE_CONTENT_TYPES.contains(file.getContentType())) {
            return new ResponseResult<>(FAILURE, "上传失败，文件类型非法，只允许上传mp3文件！");
        }
        // 检查保存上传文件的文件夹存在(一个是在resource一个是在target下)
        String pathNameTemp = null;
        try {
            pathNameTemp = ResourceUtils.getURL("classpath:").getPath() + "static/upload/audio";
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
        String voicePath = "/road_examination_manager/" + UPLOAD_DIR_NAME + "/" + saveFileName;
        return new ResponseResult<String>(SUCCESS, "上传成功", voicePath);
    }

    @PostMapping("/modifyVoicePath.do")
    public ResponseResult<Void> handleModifyVoicePath(@RequestParam("id") Integer id, @RequestParam("voicePath") String voicePath, HttpSession session) {
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }
        if (examItemService.modifyVoicePath(id, voicePath)) {
            return new ResponseResult<Void>(SUCCESS, "保存成功！");
        } else {
            return new ResponseResult<>(FAILURE, "修改保存失败，原因未知！");
        }
    }
}
