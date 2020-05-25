package edu.fzu.qujing.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@ControllerAdvice(basePackages ={"edu.fzu.qujing.controller"})
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);


    @ExceptionHandler(value = Exception.class)
    public Map<String, String> javaExceptionHandler(HttpServletResponse resp, Exception ex) {
        Map<String, String> map = new HashMap<>();
        logger.error("捕获到Exception异常，异常信息为{}", ex.getMessage());
        map.put("msg", ex.getMessage());
        map.put("code", "500");
        return map;
    }

}
