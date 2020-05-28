package edu.fzu.qujing.controller;

import edu.fzu.qujing.bean.Task;
import edu.fzu.qujing.service.TaskService;
import edu.fzu.qujing.util.AuthorityUtil;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "任务相关操作")
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;


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
    })


    @PostMapping("/post")
    public ResponseEntity<String> postTask(@ApiIgnore HttpServletRequest request,
                                           @ApiIgnore @RequestBody Map<String, String> map) {
        map.put("studentId",JwtUtil.getSubject(request));
        taskService.postTask(map);
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

    @DeleteMapping("/sender/cancel/{id}")
    public ResponseEntity<String> senderCancleTask(@ApiIgnore @PathVariable("id") Integer id,
                                                   @ApiIgnore @RequestBody Map<String, String> map,
                                                   @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        String content = map.get("content");
        String type = map.get("type");
        taskService.cancelTaskToEmployer(id,subject , content, type);
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

    @DeleteMapping("/receiver/cancel/{id}")
    public ResponseEntity<String> receiverCancleTask(@ApiIgnore @PathVariable("id") Integer id,
                                                     @ApiIgnore @RequestBody Map<String, String> map,
                                                     @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        String content = map.get("content");
        String type = map.get("type");
        taskService.cancelTaskToEmployee(id, subject, content, type);
        return ResponseEntity.ok("Cancel success");
    }




    @ApiOperation(value = "接受者完成任务接口", notes = "URL传递id")

    @ApiResponses({
            @ApiResponse(code = 200, message = "Accomplish success"),
    })

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

    @PutMapping("/sender/confirm/{id}")
    public ResponseEntity<String> senderAccomplishTask(@ApiIgnore @PathVariable Integer id,
                                                       @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        taskService.confirmTaskToEmployer(id, subject);
        return ResponseEntity.ok("Confirm success");
    }

    @ApiOperation(value = "获取未被接受的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @GetMapping("/listUnacceptedTask/{pos}")
    public List<Task> listUnacceptedTask(@ApiIgnore @PathVariable("pos") Integer pos) {
        return taskService.listUnacceptedTask(pos);
    }

    @ApiOperation(value = "获取当前用户接受的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @GetMapping("/listAccept/{pos}")
    public List<Task> listAccept(@ApiIgnore @PathVariable("pos") Integer pos,
                                 @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        return taskService.listAccept(subject, pos);
    }

    @ApiOperation(value = "获取当前用户发布的任务列表", notes = "URL传递pos,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @GetMapping("/listPublish/{pos}")
    public List<Task> listPublish(@PathVariable("pos") Integer pos,
                                  @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        return taskService.listPublish(subject, pos);
    }

    @ApiOperation(value = "获当前用户接受的任务列表", notes = "URL传递id,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @GetMapping("/getDetailTask/{id}")
    public Task getDetailTask(@ApiIgnore @PathVariable("id") Integer id) {
        return taskService.getDetailTask(id);
    }

    @ApiOperation(value = "接受任务接口", notes = "URL传递id,任务类型请使用type查询出全部的类型和状态，然后使用数组去赋值")

    @ApiResponses({
            @ApiResponse(code = 200, message = "Accept success"),
            @ApiResponse(code = 400, message = "Task accepted by others"),
    })

    @PutMapping("/acceptTask/{id}")
    public ResponseEntity<String> acceptTask(@ApiIgnore @PathVariable("id") Integer id,
                                             @ApiIgnore HttpServletRequest request) {
        String subject = JwtUtil.getSubject(request);
        Task task = taskService.acceptTask(id, subject);
        if(task != null) {
            return ResponseEntity.ok("Accept success");
        }
        return ResponseEntity.badRequest().body("Task accepted by others");
    }


}
