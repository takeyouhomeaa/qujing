package edu.fzu.qujing.controller;

import edu.fzu.qujing.annotation.SystemControllerLog;
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
    @SystemControllerLog
    @GetMapping("/listTaskState")
    public List<Type> listTaskState() {
       return typeService.listTaskState();
    }




    @ApiOperation(value = "获取任务类型接口")
    @SystemControllerLog
    @GetMapping("/listTaskType")
    public List<Type> listTaskType() {
        return typeService.listTaskType();
    }




    @ApiOperation(value = "通过id获取任务类型接口",notes = "通过URL传递id")
    @SystemControllerLog
    @GetMapping("/getTaskType/{id}")
    public Type getTaskType(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getTaskType(id);
    }




    @ApiOperation(value = "通过id获取任务状态接口",notes = "通过URL传递id")
    @SystemControllerLog
    @GetMapping("/getTaskState/{id}")
    public Type getTaskState(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getTakeState(id);
    }




    @ApiOperation(value = "获取消费类型接口")
    @SystemControllerLog
    @GetMapping("/listExpensesType")
    public List<Type> listExpensesType() {
        return typeService.listExpensesType();
    }




    @ApiOperation(value = "通过类型id获取欺诈反馈类型",notes = "通过URL传递id")
    @SystemControllerLog
    @GetMapping("/getFeedBackTpe/{id}")
    public Type getFeedBackTpe(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getFeedBackTpe(id);
    }




    @ApiOperation(value = "通过类型id获得举报类型",notes = "通过URL传递id")
    @SystemControllerLog("通过类型id获得举报类型")
    @GetMapping("/getSuitType/{id}")
    public Type getSuitType(@ApiIgnore @PathVariable("id") Integer id) {
        return typeService.getSuitType(id);
    }




    @ApiOperation(value = "获得欺诈反馈类型列表")
    @SystemControllerLog("获得欺诈反馈类型列表")
    @GetMapping("/listFeedBackTpe")
    public List<Type> listFeedBackTpe() {
        return typeService.listFeedBackTpe();
    }



    @ApiOperation(value = "获得举报类型列表")
    @SystemControllerLog
    @GetMapping("/listSuitType")
    public List<Type> listSuitType() {
        return typeService.listSuitType();
    }


}
