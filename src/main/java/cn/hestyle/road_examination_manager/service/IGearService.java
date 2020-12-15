package cn.hestyle.road_examination_manager.service;

import cn.hestyle.road_examination_manager.entity.Gear;

import java.util.List;

public interface IGearService {

    List<Gear> findByPage(Integer pageIndex, Integer pageSize);

    Integer getGearCount();
}
