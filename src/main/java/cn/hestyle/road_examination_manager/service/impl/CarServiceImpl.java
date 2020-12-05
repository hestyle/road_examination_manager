package cn.hestyle.road_examination_manager.service.impl;

import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.mapper.CarMapper;
import cn.hestyle.road_examination_manager.service.ICarService;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CarService implements ICarService {
    @Resource
    CarMapper carMapper;

    @Override
    public Car addNew(Car car) {
        Integer rows = carMapper.addNew(car);
        if(rows != 1){
            throw new InsertException("新增车辆信息失败");
        }
        return car;
    }
}
