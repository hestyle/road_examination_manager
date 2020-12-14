package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.entity.ExamTemplate;
import cn.hestyle.road_examination_manager.entity.ExamTemplateItem;
import cn.hestyle.road_examination_manager.mapper.ExamItemMapper;
import cn.hestyle.road_examination_manager.mapper.ExamTemplateItemMapper;
import cn.hestyle.road_examination_manager.mapper.ExamTemplateMapper;
import cn.hestyle.road_examination_manager.service.IExamTemplateService;
import cn.hestyle.road_examination_manager.service.exception.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class ExamTemplateServiceImpl implements IExamTemplateService {
    @Resource
    ExamTemplateMapper examTemplateMapper;
    @Resource
    ExamItemMapper examItemMapper;
    @Resource
    ExamTemplateItemMapper examTemplateItemMapper;

    @Override
    public List<ExamTemplate> findByPage(Integer pageIndex, Integer pageSize) {
        // 检查页码是否合法
        if (pageIndex < 1) {
            throw new PageFindErrorException("页码 " + pageIndex + " 非法，必须大于0！");
        }
        // 检查页大小是否合法
        if (pageSize < 1) {
            throw new PageFindErrorException("页大小 " + pageSize + " 非法，必须大于0！");
        }
        // 调用持久层mapper
        try {
            return examTemplateMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getExamTemplateCount() {
        // 调用持久层mapper
        try {
            return examTemplateMapper.getExamTemplateCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean delById(String id) {
        ExamTemplate data = findById(id);
        if(data == null){
            throw new DeleteException("删除失败，考试信息不存在");
        }

        try {
            return 1 == examTemplateMapper.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("删除考试信息时访问数据库失败！");
        }
    }

    private ExamTemplate findById(String id) {
        ExamTemplate examTemplate=null;
        try {
            examTemplate = examTemplateMapper.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("根据考试模板id查询考试模板数据时访问数据库失败！");
        }
        return examTemplate;
    }

    @Override
    public List<ExamItem> findAllExamItem() {
        try {
            return examTemplateMapper.findAllAvailableExamItem();
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("查询所有考试项时访问数据库失败！");
        }
    }

    @Override
    public Boolean add(String name, Integer score, String type, List<ExamItem> examItemList) {
        ExamTemplate examTemplate = new ExamTemplate();
        examTemplate.setIsDel(0);
        examTemplate.setName(name);
        examTemplate.setType(type.toUpperCase());

        Integer realScore = 0;
        for(ExamItem examItem : examItemList){
            realScore += examItem.getScore();
        }
        if(realScore != score){
            throw new ServiceException("考试项总分数与考试模板总分数不符！");
        }
        examTemplate.setScore(score);

        examTemplate.setId(generateId());

        try {
            if(1 == examTemplateMapper.addNew(examTemplate)){
                List<ExamTemplateItem> examTemplateItemList = new LinkedList<>();
                for (ExamItem examItem : examItemList){
                    examTemplateItemList.add(new ExamTemplateItem(examTemplate.getId(), examItem.getId()));
                }
                if(examItemList.size() == examTemplateItemMapper.addNewByList(examTemplateItemList)){
                    return true;
                }else {
                    throw new ServiceException("保存考试模板失败！");
                }
            }else {
                throw new ServiceException("保存考试模板失败！");
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("保存考试模板时访问数据库失败！");
        }
    }

    private String generateId() throws AccessDefinedException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String yyyymmdd = sdf.format(new Date()).substring(0, 8);
        Integer num = 0;
        try {
            num = examTemplateMapper.getTodayNum(yyyymmdd);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("生成考试模板ID时访问数据库失败！");
        }
        num = num + 1;
        String strNum = String.format("%04d", num);
        return yyyymmdd + strNum;
    }

    @Override
    public ExamTemplate findByExamTemplateId(String examTemplateId) {
        ExamTemplate examTemplate = findById(examTemplateId);
        if(examTemplate == null){
            throw new FindException("符合要求的考试模板不存在");
        }

        List<ExamItem> examItemList = null;
        try {
            examItemList = examTemplateMapper.findExamItemListByExamTemplateId(examTemplateId);
        }catch (Exception e){
            e.printStackTrace();
            throw new AccessDefinedException("根据考生模板id查询考试项信息时访问数据库失败！");
        }
        if(examItemList == null){
            throw new ServiceException("考试模板不含任何考试项！");
        }
        examTemplate.setExamItemList(examItemList);

        return examTemplate;
    }
}
