package com.cpp.devops.system;

import com.cpp.devops.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.StringJoiner;

/**
 * @program: ding-message-service
 * @description:
 * @author: HuiZhong
 * @create: 2022-09-14 18:06
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBizException(BusinessException ex) {
        Result<Object> result = new Result<>();
        result.setCode(ex.getCode());
        result.setMessage(ex.getMessage());
        return result;
    }

    /**
     * 参数校验不通过异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        StringJoiner sj = new StringJoiner(";");
        ex.getBindingResult().getFieldErrors().forEach(x -> sj.add(x.getDefaultMessage()));
        Result<Object> result = new Result<>();
        result.setCode(505);
        result.setMessage(sj.toString());
        return result;
    }

    /**
     * Controller参数绑定错误
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        Result<Object> result = new Result<>();
        result.setCode(506);
        result.setMessage(ex.getMessage());
        return result;
    }

    /**
     * 其他未知异常
     */
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception ex) {
        log.error(ex.getMessage(), ex);
        Result<Object> result = new Result<>();
        result.setCode(507);
        result.setMessage("服务器内部错误");
        return result;
    }
}
