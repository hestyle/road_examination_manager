package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Gear;
import cn.hestyle.road_examination_manager.mapper.GearMapper;
import cn.hestyle.road_examination_manager.service.IGearService;
import cn.hestyle.road_examination_manager.service.exception.PageFindErrorException;
import cn.hestyle.road_examination_manager.service.exception.UpdateException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class GearServiceImpl implements IGearService {

    @Resource
    GearMapper gearMapper;

    @Override
    public List<Gear> findByPage(Integer pageIndex, Integer pageSize) throws PageFindErrorException {
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
            return gearMapper.findByPage((pageIndex - 1) * pageSize, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Integer getGearCount() {
        // 调用持久层mapper
        try {
            return gearMapper.getGearCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new PageFindErrorException("分页查询失败，数据库发生未知异常！");
        }
    }

    @Override
    public Boolean modifyBaseInfo(Gear gear) throws UpdateException {
        // 通过id查询Candidate
        Gear gearData = gearMapper.findById(gear.getId());
        if (null == gearData) {
            throw new UpdateException("修改失败，" + gear.getId() + " 挡位不存在！");
        }
        if (gear.getIsDel() != null) {
            if (gear.getIsDel() != 0 && gear.getIsDel() != 1) {
                throw new UpdateException("修改失败，isDel字段必须为0或1！");
            }
            gearData.setIsDel(0);
        }
        if (gear.getMaxSpeed() != null && gear.getMinSpeed() != null) {
            if (gear.getMinSpeed() < -15 || gear.getMaxSpeed() < -15) {
                throw new UpdateException("修改失败，速度超出范围！");
            }
            gearData.setMaxSpeed(gear.getMaxSpeed());
            gearData.setMinSpeed(gear.getMinSpeed());
        }
        if(gear.getName() != null){
            if(gear.getName().length() > 50 || gear.getName().length() < 0){
                throw new UpdateException("修改失败，名称长度不合法");
            }
            gearData.setName(gear.getName());
        }
        if(gear.getDescription().length() < 0 || gear.getDescription().length() > 255){
            throw new UpdateException("修改失败，描述长度超出范围");
        }
        gearData.setDescription(gear.getDescription());

        try {
            return 1 == gearMapper.update(gearData);
        } catch (Exception e) {
            e.printStackTrace();
            throw new UpdateException("信息修改失败，数据库发生未知异常！");
        }
    }

}
