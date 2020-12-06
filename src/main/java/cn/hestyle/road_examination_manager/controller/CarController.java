package cn.hestyle.road_examination_manager.controller;

import cn.hestyle.road_examination_manager.controller.exception.ManagerNotLoginException;
import cn.hestyle.road_examination_manager.entity.Car;
import cn.hestyle.road_examination_manager.service.ICarService;
import cn.hestyle.road_examination_manager.service.exception.InsertException;
import cn.hestyle.road_examination_manager.util.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static cn.hestyle.road_examination_manager.controller.BaseController.SUCCESS;

@RestController
@RequestMapping("/car")
public class CarController {
    @Autowired
    private ICarService carService;

    @PostMapping("/add.do")
    public ResponseResult<Car> handleAdd(Car car, HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        carService.addNew(car);
        /**
         * 失败不会返回成功代码，而是抛出异常
         */
        return new ResponseResult<>(SUCCESS, "车辆信息添加成功！");
    }

    @GetMapping("/del.do")
    public ResponseResult<Void> handleDelById(@RequestParam("id")Integer id, HttpSession session){
        // 判断是否已经登录过
        if (null == session.getAttribute("username")) {
            throw new ManagerNotLoginException("操作失败！请先进行管理员登录！");
        }

        carService.delById(id);
        return new ResponseResult<>(SUCCESS, "车辆信息删除成功！");
    }
}
