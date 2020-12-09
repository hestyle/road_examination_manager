package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.ExamItem;
import cn.hestyle.road_examination_manager.mapper.ExamItemMapper;
import cn.hestyle.road_examination_manager.mapper.ExamOperationMapper;
import cn.hestyle.road_examination_manager.service.IExamItemService;
import cn.hestyle.road_examination_manager.service.exception.FindException;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ExamItem业务实现类
 * @author hestyle
 */
@Service
public class ExamItemServiceImpl implements IExamItemService {
    @Resource
    private ExamItemMapper examItemMapper;
    @Resource
    private ExamOperationMapper examOperationMapper;


    @Override
    public Boolean add(ExamItem examItem) throws InsertException {
        // 检查各个属性合法性
        if (examItem.getName() == null || examItem.getName().length() == 0) {
            throw new InsertException("保存失败，考试项name字段必填");
        }
        if (examItem.getName().length() < 2 || examItem.getName().length() > 50) {
            throw new InsertException("保存失败，考试项name字段长度必须控制在2~50个字符！");
        }
        if (null != examItemMapper.findByName(examItem.getName())) {
            throw new InsertException("保存失败，考试项name = " + examItem.getName() + " 已被注册！");
        }
        if (examItem.getScore() == null || examItem.getScore() < 1 || examItem.getScore() > 100) {
            throw new InsertException("保存失败，考试项分值必须为 1 ~ 100");
        }
        // 检查operation_ids
        if (examItem.getOperationIds() == null || examItem.getOperationIds().length() == 0) {
            throw new InsertException("保存失败，考试项所包含的操作项必须设置！");
        }
        String[] operationIds = examItem.getOperationIds().split(",");
        for (String idString : operationIds) {
            if (idString == null || idString.length() == 0) {
                throw new InsertException("保存失败，考试项operation_ids设置格式错误！");
            }
            Integer id = null;
            try {
                // 各个id必须是正确的数字，且是有效的operation_id
                id = Integer.parseInt(idString);
            } catch (Exception e) {
                e.printStackTrace();
                throw new InsertException("保存失败，考试项operation_ids设置格式错误！");
            }
            if (null == examOperationMapper.findById(id)) {
                throw new InsertException("保存失败，考试项operation_ids中id = " + id + "未注册！");
            }
        }
        if (examItem.getIsDel() == null || (examItem.getIsDel() != 0 && examItem.getIsDel() != 1)) {
            throw new InsertException("保存失败，考试项状态必须设置，且有且只有0未删除、1已删除两种状态！");
        }
        try {
            return 1 == examItemMapper.addNew(examItem);
        } catch (Exception e) {
            e.printStackTrace();
            throw new InsertException("保存失败，数据库发生未知错误！");
        }
    }

    @Override
    public ExamItem findById(Integer id) throws FindException {
        ExamItem examItem = null;
        try {
            examItem = examItemMapper.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FindException("查找失败，数据库发生未知异常！");
        }
        if (examItem == null) {
            throw new FindException("查找失败，id = " + id + "的考试项未注册！");
        }
        return examItem;
    }

}
