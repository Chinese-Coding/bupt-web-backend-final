package cn.edu.bupt.web.common;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@ResponseBody
@ControllerAdvice(annotations = {RestController.class, Controller.class})
public class GlobalExceptionHandler {
    /**
     * 参数错误
     * <p>
     * TODO: 优化返回的错误信息, 修改成不携带后端函数名
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public R<String> exceptionHandler(ConstraintViolationException e) {
        var message = e.getMessage();
        return R.error(message);
    }
}
