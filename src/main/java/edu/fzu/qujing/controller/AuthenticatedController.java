package edu.fzu.qujing.controller;

import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.AuthenticatedService;
import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.AuthorityUtil;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Api(tags = "用户的权限控制")
@RestController
@RequestMapping("/authenticated")
public class AuthenticatedController {

    @Autowired
    private AuthenticatedService authenticatedService;

    @Autowired
    private UserService userService;
    /**
     * @param map
     * @return
     * @throws AuthenticationException
     */
    @ApiOperation(value = "用户登录接口", notes = "Json方式登录,登录需要从header中获取JwtToken")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名:学号或邮箱",name = "username",dataType = "string",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "string",required = true)})
    @ApiResponses({
            @ApiResponse(code = 200,message = "success"),
            @ApiResponse(code = 401,message = "Username or password incorrect"),
            @ApiResponse(code = 403,message = "Account is frozen to 封禁时间",
                    examples = @Example({@ExampleProperty(value = "Account is frozen to 2020-07-02 17:24:25")})),
            @ApiResponse(code = 404,message = "Account does not exist")
    })
    @ResponseHeader(name="Authorization",description="获取JwtToken")
    @PostMapping("/loginByStudentId")
    public ResponseEntity<String> loginByStudentId(@ApiIgnore @RequestBody Map<String,String> map,
                                        @ApiIgnore HttpServletResponse response)  {
        ResponseEntity<String> status =
                authenticatedService.loginByStudentId(map.get("username"),map.get("password"),response);
        return status;
    }



    @ApiOperation(value = "用户注册接口", notes = "Json方式注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
            @ApiImplicitParam(value = "学号",name = "studentId",dataType = "string",required = true),
            @ApiImplicitParam(value = "昵称",name = "username",dataType = "string",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "string",required = true)})
    @ApiResponses({
            @ApiResponse(code = 200,message = "success")
    })
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authenticatedService.register(user);
        return ResponseEntity.ok("success");
    }


    @ApiOperation(value = "验证码发送接口", notes = "Json方式注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
            @ApiImplicitParam(value = "学号",name = "studentId",dataType = "string",required = true),
            @ApiImplicitParam(value = "昵称",name = "username",dataType = "string",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "string",required = true)})
    @PostMapping("/sendCaptcha")
    public ResponseEntity<String> sendCaptcha(@ApiIgnore @RequestBody Map<String,String> map) {
        String phone = map.get("phone");
        return authenticatedService.sendCaptcha(phone);
    }




    @ApiOperation(value = "用户激活接口", notes = "通过检验check来判断是否激活成功")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
            @ApiImplicitParam(value = "验证码",name = "check",dataType = "string",required = true)})
    @ApiResponses({
            @ApiResponse(code = 200,message = "Email has been sent"),
            @ApiResponse(code = 403,message = "Verification code mismatch"),
    })
    @PutMapping("/active")
    public ResponseEntity<String> active(@ApiIgnore @RequestBody Map<String,String> map){
        boolean flag = authenticatedService.activeUser(map.get("phone"),map.get("check"));
        if(flag){
            return ResponseEntity.ok("Activation successful");
        }else {
            return ResponseEntity.status(403).body("Verification code mismatch");
        }
    }


    @ApiOperation(value = "用户退出登录接口", notes = "请在退出登录事件发生时将heaer中的jwtToken 清除")
    @GetMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logout");
    }



    @ApiOperation(value = "忘记密码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true),
            @ApiImplicitParam(value = "新密码", name = "newPwd", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Modified success"),
            @ApiResponse(code = 403, message = "The old password does not match")
    })
    @PutMapping("/forgetPwd")
    public ResponseEntity<String> forgetPassword(@ApiIgnore @RequestBody Map<String,String> map) {
        User user = userService.updatePassword(map.get("phone"),map.get("newPwd"));
        if(user != null){
            return ResponseEntity.ok("Modified success");
        }

        return ResponseEntity.status(403).body("Modified success");
    }




    @ApiOperation(value = "验证验证码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true),
            @ApiImplicitParam(value = "验证码", name = "check", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 404, message = "false")
    })
    @GetMapping("/verifyCaptcha")
    public ResponseEntity<String> verifyCaptcha(@ApiIgnore @RequestBody Map<String,String> map) {
        if(authenticatedService.verifyCaptcha(map.get("check"), map.get("phone"))){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.status(404).body("false");
    }
}
