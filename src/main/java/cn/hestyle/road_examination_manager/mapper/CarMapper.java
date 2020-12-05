package cn.hestyle.road_examination_manager.mapper;

import cn.hestyle.road_examination_manager.entity.Car;
import org.apache.ibatis.annotations.Mapper;

/**
 * 车辆 持久层
 * @author wjl
 */
@Mapper
public interface CarMapper {
    /**
     * 通过车辆id查询mcar
     * @param id  车辆id
     * @return          Car
     */
    Car findById(Integer id);

//    Car findByIP(String ipAddress);
//
//    Car findByMAC(String macAddress);

    /**
     *新增车辆信息
     * @param car 车辆数据
     * @return 受影响的行数
     */
    Integer addNew(Car car);

    /**
     *
     * @param id 车辆id
     * @return 受影响的行数
     */
    Integer deleteById(Integer id);

    /**
     *
     * @param car 车辆信息
     * @return 受影响的行数
     */
    Integer updateInfo(Car car);


}
