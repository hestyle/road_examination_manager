package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.mapper.ExaminerMapper;
import cn.hestyle.road_examination_manager.service.IExaminerService;
import cn.hestyle.road_examination_manager.service.exception.ExaminerNotFoundException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExaminerServiceImpl implements IExaminerService{
    @Resource
    ExaminerMapper examinerMapper;

    @Override
    public List<Examiner> examiner_list(){
        List<Examiner> list = examinerMapper.findAll();
        return list;
    }

    @Override
    public void addExaminer(Examiner examiner){
        examinerMapper.addExaminer(examiner);
    }

    @Override
    public void delExaminer(String id){
        if(findById(id)==null){
            throw new ExaminerNotFoundException("删除考官失败！未找到考官信息");
        }
        examinerMapper.delExaminer(id);
    }

    @Override
    public Examiner findById(String id) {
        return examinerMapper.findById(id);
    }

    @Override
    public List<Examiner> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return examinerMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getExaminerCount() {
        // 调用持久层mapper
        try {
            return examinerMapper.getExaminerCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }
}
