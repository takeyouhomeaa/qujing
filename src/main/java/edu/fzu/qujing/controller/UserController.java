package edu.fzu.qujing.controller;

import edu.fzu.qujing.service.UserService;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
            @ApiImplicitParam(value = "密码", name = "password", dataType = "string", required = true),
    })


    @PutMapping("/updatePwd")
    public void updatePassword(@ApiIgnore @RequestBody Map<String,String> map,
                               @ApiIgnore @RequestBody HttpServletRequest request){

        userService.updatePassword(JwtUtil.getSubject(request),map.get("password"));
    }


    @PutMapping("/recoverPwd")
    public void recoverPassword(@ApiIgnore @RequestBody Map<String,String> map) {

    }

}
