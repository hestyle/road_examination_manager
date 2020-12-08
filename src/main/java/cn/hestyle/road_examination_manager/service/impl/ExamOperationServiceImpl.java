package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamOperation;
import cn.hestyle.road_examination_manager.mapper.ExamOperationMapper;
import cn.hestyle.road_examination_manager.service.IExamOperationService;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<ExamOperation> findByName(String name) throws FindException {
        // 判断name长度
        if (name == null || name.length() == 0) {
            throw new FindException("查找失败，name不能为空！");
        }
        List<ExamOperation> examOperationList = null;
        try {
            examOperationList = examOperationMapper.findByName(name);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examOperationList == null || examOperationList.size() == 0) {
            throw new FindException("查找失败，未查找到操作项！");
        }
        return examOperationList;
    }

    @Override
    public List<ExamOperation> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return examOperationMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getExamOperationCount() {
        try {
            return examOperationMapper.getExamOperationCount();
        } catch (Exception e) {
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }
}
