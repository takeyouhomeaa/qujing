package edu.fzu.qujing.controller;

import edu.fzu.qujing.annotation.SystemControllerLog;
import edu.fzu.qujing.service.FeedBackService;
import edu.fzu.qujing.util.JwtUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ozg
 */
@Api(tags = "任务欺诈反馈相关操作")
@RestController
@RequestMapping("/feedback")
public class FeedBackController {

    @Autowired
    FeedBackService feedBackService;

    @ApiOperation(value = "任务欺诈反馈接口")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "举报理由", name = "content", dataType = "string", required = true),
            @ApiImplicitParam(value = "举报的任务ID", name = "task", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success"),
    })
    @SystemControllerLog("任务欺诈反馈")
    @PostMapping("/suitTask")
    public ResponseEntity<String> suitTask(@ApiIgnore HttpServletRequest request,
                                           @ApiIgnore @RequestBody Map<String, String> params) {
        feedBackService.save(JwtUtil.getSubject(request), params);

        return ResponseEntity.ok("success");
    }
}
