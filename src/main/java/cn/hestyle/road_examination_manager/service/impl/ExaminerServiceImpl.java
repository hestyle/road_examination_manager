package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Examiner;
import cn.hestyle.road_examination_manager.mapper.ExaminerMapper;
import cn.hestyle.road_examination_manager.service.IExaminerService;
import cn.hestyle.road_examination_manager.service.exception.ExaminerNotFoundException;
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
}
