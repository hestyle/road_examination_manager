package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamTemplate;
import cn.hestyle.road_examination_manager.mapper.ExamTemplateMapper;
import cn.hestyle.road_examination_manager.service.IExamTemplateService;
import cn.hestyle.road_examination_manager.service.exception.AccessDefinedException;
import cn.hestyle.road_examination_manager.service.exception.DeleteException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExamTemplateServiceImpl implements IExamTemplateService {
    @Resource
    ExamTemplateMapper examTemplateMapper;

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
}
