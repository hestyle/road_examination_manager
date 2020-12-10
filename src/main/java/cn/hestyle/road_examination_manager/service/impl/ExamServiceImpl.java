package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.entity.ExamTemplate;
import cn.hestyle.road_examination_manager.mapper.ExamItemMapper;
import cn.hestyle.road_examination_manager.mapper.ExamMapper;
import cn.hestyle.road_examination_manager.mapper.ExamTemplateMapper;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExamServiceImpl implements IExamService {
    @Resource
    ExamMapper examMapper;
    @Resource
    ExamItemMapper examItemMapper;
    @Resource
    ExamTemplateMapper examTemplateMapper;

    @Override
    public Exam findByAdmissionNo(String admissionNo) throws AccessDefinedException{
        Exam exam=null;
        try {
            exam = examMapper.findByAdmissionNo(admissionNo);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("根据准考证查询考试信息时访问数据库失败！");
        }
        return exam;
    }

    @Override
    public Integer getExamCount(String admissionNo) {
        try{
            return examMapper.getExamCount(admissionNo);
        }catch (Exception e){
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public List<Exam> findByPage(Integer pageIndex, Integer pageSize, String admissionNo) throws PageFindErrorException {
        // 检查页码是否合法
        if (pageIndex < 1) {
            throw new PageFindErrorException("页码 " + pageIndex + " 非法，必须大于0！");
        }
        // 检查页大小是否合法
        if (pageSize < 1) {
            throw new PageFindErrorException("页大小 " + pageSize + " 非法，必须大于0！");
        }
        // 调用持久层mapper
        try{
            return examMapper.findByPage((pageIndex - 1)*pageSize, pageSize, admissionNo);
        }catch (Exception e){
            e.printStackTrace();
            throw new PageFindErrorException("分页查询考试信息失败，数据库发生未知异常！");
        }
    }

//    @Override
//    public List<Exam> findByPage(Integer pageIndex, Integer pageSize, String admissionNo) throws PageFindErrorException {
//        return null;
//    }

    @Override
    public Boolean add(Exam newExam) throws AccessDefinedException{
        try {
            return 1 == examMapper.addNew(newExam);
        } catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("保存考试信息时访问数据库失败！");
        }
    }

    @Override
    public Boolean deleteByAdmissionNo(String admissionNo) throws AccessDefinedException, DeleteException{
        Exam data = findByAdmissionNo(admissionNo);
        if(data == null){
            throw new DeleteException("删除失败，考试信息不存在");
        }

        try {
            return 1 == examMapper.deleteByAdmissionNo(admissionNo);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("删除考试信息时访问数据库失败！");
        }
    }

    @Override
    public Boolean modifyExamInfo(Exam newExam) throws UpdateException, AccessDefinedException{
        Exam data = findByAdmissionNo(newExam.getAdmissionNo());
        if(data == null){
            throw new UpdateException("考试信息不存在，修改失败！");
        }
        if(data.getIsDel() == 1){
            throw new UpdateException("考试信息已经被删除，无法修改！");
        }

        try {
            return 1 == examMapper.update(newExam);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("修改考试信息时访问数据库失败！");
        }
    }

    @Override
    public Map<String, Object> findDetailInfoByAdmissionNo(String admissionNo) {
        Map<String, Object> res = new HashMap<String, Object>();
        Exam exam = findByAdmissionNo(admissionNo);
        if(exam == null){
            throw new FindException("符合要求的考试信息不存在！");
        }
        res.put("exam", exam);

        ExamTemplate examTemplate = null;
        try {
            examTemplate = examTemplateMapper.findById(exam.getExamTemplateId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板信息时访问数据库失败！");
        }
        if(examTemplate == null){
            throw new FindException("符合要求的道路考试模板不存在！");
        }
        res.put("examTemplate", examTemplate);

        ExamTemplate lightExamTemplate = null;
        try {
            lightExamTemplate = examTemplateMapper.findById(exam.getLightExamTemplateId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板信息时访问数据库失败！");
        }
        if(lightExamTemplate == null){
            throw new FindException("符合要求的道路考试模板不存在！");
        }
        res.put("lightExamTemplate", lightExamTemplate);

        List<ExamItem> examItemList = null;
        try {
            examItemList = examTemplateMapper.findExamItemListByExamTemplateId(examTemplate.getId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板的考试项目时访问数据库失败！");
        }
        res.put("examItemList", examItemList);

        List<ExamItem> lightExamItemList = null;
        try {
            lightExamItemList = examTemplateMapper.findExamItemListByExamTemplateId(lightExamTemplate.getId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板的考试项目时访问数据库失败！");
        }
        res.put("lightExamItemList", lightExamItemList);

        return res;
    }

    @Override
    public Map<String, Object> findExamItemsInfoByAdmissionNo(String admissionNo) {
        Map<String, Object> res = new HashMap<String, Object>();
        Exam exam = findByAdmissionNo(admissionNo);
        if(exam == null){
            throw new FindException("符合要求的考试信息不存在！");
        }

        /**
         * 获取道路考试模板
         */
        ExamTemplate examTemplate = null;
        try {
            examTemplate = examTemplateMapper.findById(exam.getExamTemplateId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板信息时访问数据库失败！");
        }
        if(examTemplate == null){
            throw new FindException("符合要求的道路考试模板不存在！");
        }
        res.put("examTemplate", examTemplate);

        /*
        获取考试模板对应考试内容
         */
        List<ExamItem> examItemList = null;
        try {
            examItemList = examTemplateMapper.findExamItemListByExamTemplateId(examTemplate.getId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板的考试项目时访问数据库失败！");
        }
        res.put("examItemList", examItemList);

        return res;
    }

    @Override
    public Map<String, Object> findExamLightItemsInfoByAdmissionNo(String admissionNo) {
        Map<String, Object> res = new HashMap<String, Object>();
        Exam exam = findByAdmissionNo(admissionNo);
        if(exam == null){
            throw new FindException("符合要求的考试信息不存在！");
        }

        /**
         * 获取夜考考试模板
         */
        ExamTemplate lightExamTemplate = null;
        try {
            lightExamTemplate = examTemplateMapper.findById(exam.getLightExamTemplateId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板信息时访问数据库失败！");
        }
        if(lightExamTemplate == null){
            throw new FindException("符合要求的道路考试模板不存在！");
        }
        res.put("lightExamTemplate", lightExamTemplate);

        /*
        获取考试模板对应考试内容
         */
        List<ExamItem> lightExamItemList = null;
        try {
            lightExamItemList = examTemplateMapper.findExamItemListByExamTemplateId(lightExamTemplate.getId());
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("获取考试模板的考试项目时访问数据库失败！");
        }
        res.put("lightExamItemList", lightExamItemList);

        return res;
    }
}
