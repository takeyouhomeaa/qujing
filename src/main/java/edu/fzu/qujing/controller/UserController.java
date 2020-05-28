package edu.fzu.qujing.controller;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Api(tags = "用户的账户相关操作")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "更改密码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "原密码", name = "oldPwd", dataType = "string", required = true),
            @ApiImplicitParam(value = "新密码", name = "newPwd", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Modified success"),
            @ApiResponse(code = 403, message = "The old password does not match")
    })
    @PutMapping("/updatePwd")
    public ResponseEntity<String> updatePassword(@ApiIgnore @RequestBody Map<String,String> map,
                                         @ApiIgnore HttpServletRequest request){

        User user = userService.updatePassword(JwtUtil.getSubject(request) ,map.get("oldPwd"),map.get("newPwd"));
        if(user != null){
            return ResponseEntity.ok("Modified success");
        }

        return ResponseEntity.status(403).body("Modified success");
    }



    @ApiOperation(value = "检查手机号是否存在接口")
    @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true)
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 404, message = "false")
    })
    @GetMapping("/checkPhone")
    public ResponseEntity<String> checkPhone(@ApiIgnore @RequestBody Map<String,String> map) {
        if(userService.checkPhone(map.get("phone"))){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.status(404).body("false");
    }



    @ApiOperation(value = "检查手机号是否存在接口")
    @ApiImplicitParam(value = "学号", name = "studentId", dataType = "string", required = true)
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 404, message = "false")
    })
    @GetMapping("/checkStudentId")
    public ResponseEntity<String> checkStudentId(@ApiIgnore @RequestBody Map<String,String> map) {
        if(userService.checkStudentId(map.get("studentId"))){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.status(404).body("false");
    }


}
