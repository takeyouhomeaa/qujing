package edu.fzu.qujing.controller;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.AuthenticatedService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = "用户的权限控制")
@RestController
@RequestMapping("/authenticated")
public class AuthenticatedController {

    @Autowired
    private AuthenticatedService authenticatedService;

    /**
     * @param map
     * @return
     * @throws AuthenticationException
     */
    @ApiOperation(value = "用户登录接口", notes = "Json方式登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名:学号或邮箱",name = "username",dataType = "string",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "string",required = true)})

    @ApiResponses({
            @ApiResponse(code = 200,message = "success"),
            @ApiResponse(code = 201,message = "没有这个返回值"),
            @ApiResponse(code = 401,message = "Username or password incorrect"),
            @ApiResponse(code = 403,message = "Account is frozen to 封禁时间",
                    examples = @Example({@ExampleProperty(value = "Account is frozen to 2020-07-02 17:24:25")})),
            @ApiResponse(code = 404,message = "Account does not exist")
    })


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String,String> map,
                                        HttpServletResponse response) throws AuthenticationException {
        ResponseEntity<String> status = authenticatedService.login(map.get("username"),map.get("password"),response);
        return status;
    }


    @ApiOperation(value = "用户注册接口", notes = "Json方式注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "邮箱",name = "email",dataType = "string",required = true),
            @ApiImplicitParam(value = "学号",name = "studentId",dataType = "string",required = true),
            @ApiImplicitParam(value = "昵称",name = "username",dataType = "string",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "string",required = true)})

    @ApiResponses({
            @ApiResponse(code = 200,message = "Email has been sent"),
            @ApiResponse(code = 201,message = "没有这个返回值"),
            @ApiResponse(code = 401,message = "没有这个返回值"),
            @ApiResponse(code = 403,message = "没有这个返回值"),
            @ApiResponse(code = 401,message = "没有这个返回值")
    })

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authenticatedService.register(user);
        return ResponseEntity.ok("Email has been sent");
    }

    @ApiOperation(value = "用户激活接口", notes = "通过检验check来判断是否激活成功")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "任务id",name = "id",dataType = "int",required = true),
            @ApiImplicitParam(value = "验证码",name = "check",dataType = "string",required = true)})

    @ApiResponses({
            @ApiResponse(code = 200,message = "Email has been sent"),
            @ApiResponse(code = 201,message = "没有这个返回值"),
            @ApiResponse(code = 401,message = "没有这个返回值"),
            @ApiResponse(code = 403,message = "Verification code mismatch"),
            @ApiResponse(code = 401,message = "没有这个返回值")
    })
    @PostMapping("/active")
    public ResponseEntity<String> active(@RequestParam("id") Integer id,@RequestParam("check") String check){
        boolean flag = authenticatedService.activeUser(id, check);
        if(flag){
            return ResponseEntity.ok("Activation successful");
        }else {
            return ResponseEntity.status(403).body("Verification code mismatch");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout");
    }
}
