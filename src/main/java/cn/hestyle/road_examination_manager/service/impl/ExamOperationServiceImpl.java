package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.mapper.ExamOperationMapper;
import cn.hestyle.road_examination_manager.service.IExamOperationService;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * examOperation service层实现类
 * @author hestyle
 */
@Service
public class ExamOperationServiceImpl implements IExamOperationService {
    @Resource
    private ExamOperationMapper examOperationMapper;

    @Override
    public Boolean add(ExamOperation examOperation) throws InsertException {
        // 判断name字段是否为空
        if (examOperation.getName() == null || examOperation.getName().length() == 0) {
            throw new InsertException("保存失败，考试操作项name字段不能空！");
        } else if (examOperation.getName().length() < 10 || examOperation.getName().length() > 50) {
            throw new InsertException("保存失败，考试操作项name字段长度非法，请控制在10~50个字符！");
        }
        // 判断name是否被占用
        if (examOperationMapper.findByName(examOperation.getName()) != null) {
            throw new InsertException("保存失败，考试操作项name字段已被占用，请修改再次提交！");
        }
        try {
            return 1 == examOperationMapper.addNew(examOperation);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InsertException("保存失败，数据库发生未知异常！");
        }
    }

    @Override
    public ExamOperation findById(Integer id) throws FindException {
        ExamOperation examOperation = null;
        try {
            examOperation = examOperationMapper.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examOperation == null) {
            throw new FindException("查找失败，id=" + id + "未注册操作项！");
        }
        return examOperation;
    }
}
