package edu.fzu.qujing.controller;

import edu.fzu.qujing.bean.Type;
import edu.fzu.qujing.service.TypeService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@Api(tags = "类型相关操作")
@RequestMapping("/type")
public class TypeController {

    @Autowired
    TypeService typeService;

    @ApiOperation(value = "获取任务状态接口")

    @GetMapping("/listTaskState")
    public List<Type> listTaskState() {
       return typeService.listTaskState();
    }




    @ApiOperation(value = "获取任务类型接口")

    @GetMapping("/listTaskType")
    public List<Type> listTaskType() {
        return typeService.listTaskType();
    }




    @ApiOperation(value = "通过id获取任务类型接口",notes = "通过URL传递id")

    @GetMapping("/getTaskType/{id}")
    public Type getTaskType(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getTaskType(id);
    }




    @ApiOperation(value = "通过id获取任务状态接口",notes = "通过URL传递id")

    @GetMapping("/getTaskState/{id}")
    public Type getTaskState(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getTakeState(id);
    }


    @ApiOperation(value = "获取消费类型接口")

    @GetMapping("/listExpensesType")
    public List<Type> listExpensesType() {
        return typeService.listExpensesType();
    }


    @ApiOperation(value = "通过id获取消费类型接口",notes = "通过URL传递id")

    @GetMapping("/getExpensesType/{id}")
    public Type getExpensesType(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getExpensesType(id);
    }
}
