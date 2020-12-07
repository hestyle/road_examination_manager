package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Exam;
import cn.hestyle.road_examination_manager.mapper.ExamMapper;
import cn.hestyle.road_examination_manager.service.IExamService;
import cn.hestyle.road_examination_manager.service.exception.AccessDefinedException;
import cn.hestyle.road_examination_manager.service.exception.DeleteException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import cn.hestyle.road_examination_manager.service.exception.UpdateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExamService implements IExamService {
    @Resource
    ExamMapper examMapper;

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
    public Integer getExamCount() throws PageFindErrorException {
        try{
            return examMapper.getExamCount();
        }catch (Exception e){
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public List<Exam> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return examMapper.findByPage((pageIndex - 1)*pageSize, pageSize);
        }catch (Exception e){
            e.printStackTrace();
            throw new PageFindErrorException("分页查询考试信息失败，数据库发生未知异常！");
        }
    }

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
}
