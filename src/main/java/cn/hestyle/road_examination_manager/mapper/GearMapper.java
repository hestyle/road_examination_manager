package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Gear;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface GearMapper {

    List<Gear> findByPage(Integer beginIndex, Integer pageSize);

    Integer getGearCount();

    Gear findById(Integer id);

    Integer update(Gear gear);
}
