package edu.fzu.qujing.controller;


import org.apache.shiro.ShiroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


/**
 * @author ozg
 */
@ApiIgnore
@RestController
@ControllerAdvice(basePackages ={"edu.fzu.qujing.controller"})
public class ExceptionController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionController.class);


    @ExceptionHandler(ShiroException.class)
    public ResponseEntity<String> shiroException(ShiroException ex) {
        return ResponseEntity.status(400).body("Username or password incorrect");
    }


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> javaExceptionHandler(HttpServletResponse resp, Exception ex) {
        logger.error("捕获到Exception异常，异常信息为{}", ex.getMessage());
        return ResponseEntity.status(500).body(ex.getMessage());
    }



}
