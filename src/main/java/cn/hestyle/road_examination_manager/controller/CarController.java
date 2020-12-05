package cn.hestyle.road_examination_manager.controller;

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
        carService.addNew(car).toString();

        return new ResponseResult<>(SUCCESS);
    }

    @GetMapping("/del.do")
    public ResponseResult<Void> handleDelById(@RequestParam("id")Integer id, HttpSession session){
        carService.delById(id);
        return new ResponseResult<>(SUCCESS);
    }
}
