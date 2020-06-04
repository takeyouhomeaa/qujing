package edu.fzu.qujing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.fzu.qujing.annotation.SystemControllerLog;
import edu.fzu.qujing.bean.DetailTask;
import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.service.DetailTaskService;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ozg
 */
@Api(tags = "任务相关操作")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    DetailTaskService detailTaskService;



    @ApiOperation(value = "发布任务接口", notes = "ttl字段是分钟")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "任务名", name = "name", dataType = "string", required = true),
            @ApiImplicitParam(value = "任务细节", name = "content", dataType = "string", required = true),
            @ApiImplicitParam(value = "任务类型", name = "ttid", dataType = "string", required = true),
            @ApiImplicitParam(value = "截止时间", name = "ttl", dataType = "string", required = true),
            @ApiImplicitParam(value = "是否加急", name = "expedited", dataType = "string", required = true),
            @ApiImplicitParam(value = "积分", name = "points", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Post success"),
            @ApiResponse(code = 403,message = "points are not enough")
    })
    @SystemControllerLog("发布任务")
    @PostMapping("/post")
    public ResponseEntity<String> postTask(@ApiIgnore HttpServletRequest request,
                                           @ApiIgnore @RequestBody Map<String, String> map) {
        map.put("studentId",JwtUtil.getSubject(request));
        Task task = taskService.postTask(map);
        if(task == null){
            return ResponseEntity.status(403).body("points are not enough");
        }
        return ResponseEntity.ok("Post success");
    }




    @ApiOperation(value = "雇主取消任务接口", notes = "URL传递id")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "取消理由", name = "content", dataType = "string", required = true),
            @ApiImplicitParam(value = "取消类型", name = "type", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cancel success"),
    })
    @SystemControllerLog("雇主取消任务")
    @DeleteMapping("/sender/cancel/{id}")
    public ResponseEntity<String> senderCancelTask(@ApiIgnore @PathVariable("id") Integer id,
                                                   @ApiIgnore @RequestBody Map<String, String> map) {
        System.out.println("方法被执行");
        String content = map.get("content");
        String type = map.get("type");
        taskService.cancelTaskToEmployer(id, content, type);
        return ResponseEntity.ok("Cancel success");
    }




    @ApiOperation(value = "接受者取消任务接口", notes = "URL传递id")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "取消理由", name = "content", dataType = "string", required = true),
            @ApiImplicitParam(value = "取消类型", name = "type", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Cancel success"),
    })
    @SystemControllerLog("接受者取消任务")
    @DeleteMapping("/receiver/cancel/{id}")
    public ResponseEntity<String> receiverCancelTask(@ApiIgnore @PathVariable("id") Integer id,
                                                     @ApiIgnore @RequestBody Map<String, String> map,
                                                     @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        String content = map.get("content");
        String type = map.get("type");
        taskService.cancelTaskToEmployee(id, content, type);
        return ResponseEntity.ok("Cancel success");
    }




    @ApiOperation(value = "接受者完成任务接口", notes = "URL传递id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Accomplish success"),
    })
    @SystemControllerLog("接受者完成任务")
    @PutMapping("/receiver/accomplish/{id}")
    public ResponseEntity<String> receiverAccomplishTask(@ApiIgnore @PathVariable Integer id,
                                                         @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        taskService.completeTaskToEmployee(id,subject);
        return ResponseEntity.ok("Accomplish success");
    }




    @ApiOperation(value = "雇主确认任务完成接口", notes = "URL传递id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Confirm success"),
    })
    @SystemControllerLog("雇主确认任务完成")
    @PutMapping("/sender/confirm/{id}")
    public ResponseEntity<String> senderAccomplishTask(@ApiIgnore @PathVariable("id") Integer id,
                                                       @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        taskService.confirmTaskToEmployer(id, subject);
        return ResponseEntity.ok("Confirm success");
    }




    @ApiOperation(value = "获取未被接受的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")
    @SystemControllerLog("获取未被接受的任务列表")
    @GetMapping("/listUnacceptedTask/{pos}")
    public List<Task> listUnacceptedTask(@ApiIgnore @PathVariable("pos") Integer pos,
                                         @ApiIgnore HttpServletRequest request) {
        return taskService.listUnacceptedTask(pos,JwtUtil.getSubject(request));
    }

    @ApiOperation(value = "获取未接受任务的数量",notes = "每页是10条记录")
    @SystemControllerLog("获取未接受任务的数量")
    @GetMapping("/getCount/unacceptedTask")
    public Map<String,Integer> getCountToUnacceptedTask(@ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        Map<String,Integer> map = new HashMap<>(2);
        Integer count =  taskService.getCountToUnacceptedTask(subject);
        map.put("count",count);
        return map;
    }



    @SystemControllerLog("获取未被接受的任务列表")
    @ApiOperation(value = "获取未被接受的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")
    @GetMapping("/listUnacceptedTaskByType/{pos}/{type}")
    public List<DetailTask> listUnacceptedTaskByType(@ApiIgnore @PathVariable("pos") Integer pos,
                                                     @ApiIgnore @PathVariable("type") Integer type,
                                                     @ApiIgnore HttpServletRequest request) {
        return detailTaskService.listUnacceptedTaskByType(pos,type,JwtUtil.getSubject(request));
    }


    @SystemControllerLog("获取未接受任务的数量(按照类型)")
    @ApiOperation(value = "获取未接受任务的数量(按照类型)",notes = "每页是10条记录")
    @GetMapping("/getCount/UnacceptedTaskByType/{type}")
    public Map<String,Integer> getCountToUnacceptedTaskByType(@ApiIgnore @PathVariable Integer type) {
        Map<String,Integer> map = new HashMap<>(2);
        Integer count = taskService.getCount(new QueryWrapper<Task>()
                                            .eq("state", 1)
                                            .eq("ttid", type));
        map.put("count",count);
        return map;
    }


    @SystemControllerLog("获取当前用户接受的任务列表")
    @ApiOperation(value = "获取当前用户接受的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")
    @GetMapping("/listAccept/{pos}")
    public List<DetailTask> listAcceptToFinished(@ApiIgnore @PathVariable("pos") Integer pos,
                                 @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        return detailTaskService.listAccept(subject, pos);
    }

    @SystemControllerLog("获取用户接受任务的数量")
    @ApiOperation(value = "获取用户接受任务的数量",notes = "每页是10条记录")
    @GetMapping("/getCount/accept")
    public Map<String,Integer> getCountToAccept(@ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        Map<String,Integer> map = new HashMap<>(2);
        Integer count = taskService.getCount(new QueryWrapper<Task>()
                .eq("receiverid", subject));
        map.put("count",count);
        return map;
    }

    @SystemControllerLog("获取当前用户发布的任务列表")
    @ApiOperation(value = "获取当前用户发布的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")
    @GetMapping("/listPublish/{pos}")
    public List<DetailTask> listPublish(@PathVariable("pos") Integer pos,
                                  @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        return detailTaskService.listPublish(subject, pos);
    }

    @SystemControllerLog("获取未接受任务的数量(按照类型)")
    @ApiOperation(value = "获取未接受任务的数量(按照类型)",notes = "每页是10条记录")
    @GetMapping("/getCount/publish")
    public Map<String,Integer> getCountToPublish(@ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        Map<String,Integer> map = new HashMap<>();
        Integer count = taskService.getCount(new QueryWrapper<Task>()
                .eq("senderid", subject));
        map.put("count",count);
        return map;
    }


    @SystemControllerLog("获得详细的任务信息")
    @ApiOperation(value = "获得详细的任务信息", notes = "URL传递id,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")
    @GetMapping("/getDetailTask/{id}")
    public DetailTask getDetailTask(@ApiIgnore @PathVariable("id") Integer id) {
        return detailTaskService.getDetailTask(id);
    }




    @ApiOperation(value = "接受任务接口", notes = "URL传递id,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Accept success"),
            @ApiResponse(code = 403, message = "Acceptable tasks are full"),
            @ApiResponse(code = 400, message = "Task accepted by others"),
    })
    @SystemControllerLog("接受任务")
    @PutMapping("/acceptTask/{id}")
    public ResponseEntity<String> acceptTask(@ApiIgnore @PathVariable("id") Integer id,
                                             @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        Task task = taskService.acceptTask(id, subject);
        if(task != null) {
            if(task.getId() == null) {
                return ResponseEntity.status(403).body("Acceptable tasks are full");
            }
            return ResponseEntity.ok("Accept success");
        }
        return ResponseEntity.badRequest().body("Task accepted by others");
    }


}
