package edu.fzu.qujing.controller;

import edu.fzu.qujing.annotation.SystemControllerLog;
import edu.fzu.qujing.bean.Suit;
import edu.fzu.qujing.service.SuitService;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Api(tags = "任务举报相关操作")
@RestController
@RequestMapping("/suit")
public class SuitController {

    @Autowired
    SuitService suitService;


    @ApiOperation(value = "举报任务接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "举报理由", name = "content", dataType = "string", required = true),
            @ApiImplicitParam(value = "举报类型", name = "type", dataType = "string", required = true),
            @ApiImplicitParam(value = "举报的任务ID", name = "task", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
    })
    @SystemControllerLog("举报任务")
    @PostMapping("/suitTask")
    public ResponseEntity<String> suitTask(@ApiIgnore HttpServletRequest request,
                                   @ApiIgnore @RequestBody Map<String, String> params) {
        suitService.save(JwtUtil.getSubject(request), params);

        return ResponseEntity.ok("success");
    }

}
