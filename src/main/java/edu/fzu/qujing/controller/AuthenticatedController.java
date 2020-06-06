package edu.fzu.qujing.controller;

import edu.fzu.qujing.annotation.SystemControllerLog;
import edu.fzu.qujing.bean.User;
import edu.fzu.qujing.service.AuthenticatedService;
import edu.fzu.qujing.service.UserService;
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
            @ApiResponse(code = 400,message = "Username or password incorrect"),
            @ApiResponse(code = 403,message = "Account is frozen to 封禁时间",
                    examples = @Example({@ExampleProperty(value = "Account is frozen to 2020-07-02 17:24:25")})),
            @ApiResponse(code = 404,message = "Account does not exist")
    })
    @SystemControllerLog("用户使用学号登录")
    @ResponseHeader(name="Authorization",description="获取JwtToken")
    @PostMapping("/loginByStudentId")
    public ResponseEntity<String> loginByStudentId(@ApiIgnore @RequestBody Map<String,String> map,
                                        @ApiIgnore HttpServletResponse response)  {
        ResponseEntity<String> status =
                authenticatedService.loginByStudentId(map.get("username"),map.get("password"),response);
        System.out.println(map.get("username"));
        return status;
    }


    @ApiOperation(value = "用户登录接口", notes = "Json方式登录,登录需要从header中获取JwtToken")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户名:学号或邮箱",name = "username",dataType = "string",required = true),
            @ApiImplicitParam(value = "密码",name = "password",dataType = "string",required = true)})
    @ApiResponses({
            @ApiResponse(code = 200,message = "success"),
            @ApiResponse(code = 400,message = "Username or password incorrect"),
            @ApiResponse(code = 403,message = "Account is frozen to 封禁时间",
                    examples = @Example({@ExampleProperty(value = "Account is frozen to 2020-07-02 17:24:25")})),
            @ApiResponse(code = 404,message = "Account does not exist")
    })
    @SystemControllerLog("用户使用手机登录")
    @ResponseHeader(name="Authorization",description="获取JwtToken")
    @PostMapping("/loginByPhone")
    public ResponseEntity<String> loginByPhone(@ApiIgnore @RequestBody Map<String,String> map,
                                                   @ApiIgnore HttpServletResponse response)  {
        ResponseEntity<String> status =
                authenticatedService.loginByPhone(map.get("phone"),map.get("check"),response);

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
    @SystemControllerLog("用户注册")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        authenticatedService.register(user);
        return ResponseEntity.ok("success");
    }


    @ApiOperation(value = "验证码发送接口,用于用户注册", notes = "Json方式注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "Captcha has been sent"),
            @ApiResponse(code = 500,message = "SMS server is busy"),
    })
    @SystemControllerLog("验证码发送接口,用于用户注册")
    @PostMapping("/sendCaptcha/register")
    public ResponseEntity<String> sendCaptchaToRegister(@ApiIgnore @RequestBody Map<String,String> map) {
        String phone = map.get("phone");
        return authenticatedService.sendCaptcha(phone,"register");
    }



    @ApiOperation(value = "验证码发送接口,用于忘记密码", notes = "Json方式注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "Captcha has been sent"),
            @ApiResponse(code = 500,message = "SMS server is busy"),
    })
    @SystemControllerLog("验证码发送接口,用于忘记密码")
    @PostMapping("/sendCaptcha/forgetPwd")
    public ResponseEntity<String> sendCaptchaToForgetPwd(@ApiIgnore @RequestBody Map<String,String> map) {
        String phone = map.get("phone");
        return authenticatedService.sendCaptcha(phone,"forgetPwd");
    }



    @ApiOperation(value = "验证码发送接口,用于登录", notes = "Json方式注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
    })
    @ApiResponses({
            @ApiResponse(code = 200,message = "Captcha has been sent"),
            @ApiResponse(code = 500,message = "SMS server is busy"),
    })
    @SystemControllerLog("验证码发送接口,用于登录")
    @PostMapping("/sendCaptcha/login")
    public ResponseEntity<String> sendCaptchaToLogin(@ApiIgnore @RequestBody Map<String,String> map) {
        String phone = map.get("phone");
        return authenticatedService.sendCaptcha(phone,"login");
    }





    @ApiOperation(value = "用户激活接口", notes = "通过检验check来判断是否激活成功")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号",name = "phone",dataType = "string",required = true),
            @ApiImplicitParam(value = "验证码",name = "check",dataType = "string",required = true)})
    @ApiResponses({
            @ApiResponse(code = 200,message = "Email has been sent"),
            @ApiResponse(code = 403,message = "Verification code mismatch"),
    })
    @SystemControllerLog("用户激活")
    @PutMapping("/active")
    public ResponseEntity<String> active(@ApiIgnore @RequestBody Map<String,String> map){
        boolean flag = authenticatedService.activeUser(map.get("phone"),map.get("check"));
        if(flag){
            return ResponseEntity.ok("Activation successful");
        }else {
            return ResponseEntity.status(403).body("Verification code mismatch");
        }
    }

    @SystemControllerLog("用户注销")
    @ApiOperation(value = "用户退出登录接口", notes = "请在退出登录事件发生时将heaer中的jwtToken 清除")
    @GetMapping("/logout")
    public ResponseEntity<String> logout(@ApiIgnore HttpServletRequest request) {
        authenticatedService.logout(JwtUtil.getSubject(request));
        return ResponseEntity.ok("Logout");
    }



    @ApiOperation(value = "忘记密码接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true),
            @ApiImplicitParam(value = "新密码", name = "newPwd", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "Modified success"),
            @ApiResponse(code = 403, message = "No permission to change password")
    })
    @SystemControllerLog("忘记密码")
    @PutMapping("/forgetPwd")
    public ResponseEntity<String> forgetPassword(@ApiIgnore @RequestBody Map<String,String> map) {
        User user = userService.updatePassword(map.get("phone"),map.get("newPwd"));
        if(user != null){
            return ResponseEntity.ok("Modified success");
        }

        return ResponseEntity.status(403).body("No permission to change password");
    }




    @ApiOperation(value = "验证验证码接口,用于用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true),
            @ApiImplicitParam(value = "验证码", name = "check", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 404, message = "false")
    })
    @SystemControllerLog("验证验证码接口,用于用户注册")
    @GetMapping("/verifyCaptcha/register")
    public ResponseEntity<String> verifyCaptchaToRegister(String phone,String check) {
        if(authenticatedService.verifyCaptchaToRegister(check,phone)){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.status(404).body("false");
    }




    @ApiOperation(value = "验证验证码接口,用于忘记密码")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true),
            @ApiImplicitParam(value = "验证码", name = "check", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 404, message = "false")
    })
    @SystemControllerLog("验证验证码接口,用于忘记密码")
    @GetMapping("/verifyCaptcha/forgetPwd")
    public ResponseEntity<String> verifyCaptchaToForgetPwd(String phone,String check) {
        if(authenticatedService.verifyCaptchaToForgetPwd(check,phone)){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.status(404).body("false");
    }



    @ApiOperation(value = "检查手机号是否存在接口")
    @ApiImplicitParam(value = "手机号", name = "phone", dataType = "string", required = true)
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 200, message = "false")
    })
    @SystemControllerLog("检查手机号是否存")
    @GetMapping("/checkPhone")
    public ResponseEntity<String> checkPhone(String phone) {
        if(userService.checkPhone(phone)){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.ok("false");
    }


    //缺少检查Redis内是否存在

    @ApiOperation(value = "检查学号是否存在接口")
    @ApiImplicitParam(value = "学号", name = "studentId", dataType = "string", required = true)
    @ApiResponses({
            @ApiResponse(code = 200, message = "true"),
            @ApiResponse(code = 200, message = "false")
    })
    @SystemControllerLog("检查学号是否存在")
    @GetMapping("/checkStudentId")
    public ResponseEntity<String> checkStudentId(String studentId) {
        if(userService.checkStudentId(studentId)){
            return ResponseEntity.ok("true");
        }
        return ResponseEntity.ok("false");
    }

}
