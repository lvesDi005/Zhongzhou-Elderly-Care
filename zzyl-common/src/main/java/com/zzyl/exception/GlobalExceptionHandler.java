package com.zzyl.exception;

import cn.hutool.core.util.ObjectUtil;
import com.zzyl.base.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理自定义异常BaseException。
     * 返回自定义异常中的错误代码和错误消息。
     *
     * @param exception 自定义异常
     * @return 响应数据，包含错误代码和错误消息
     */
    @ExceptionHandler(BaseException.class)
    public ResponseResult<Object> handleBaseException(BaseException exception) {
        if (ObjectUtil.isNotEmpty(exception.getBasicEnum())) {
            log.error("自定义异常处理:{}", exception.getBasicEnum().getMsg());
        }

        return ResponseResult.error(exception.getBasicEnum());

    }

    /**
     * 处理其他未知异常。
     * 返回HTTP响应状态码500，包含错误代码和异常堆栈信息。
     *
     * @param exception 未知异常
     * @return 响应数据，包含错误代码和异常堆栈信息
     */
    @ExceptionHandler(Exception.class)
    public ResponseResult<Object> handleUnknownException(Exception exception) {
        if (ObjectUtil.isNotEmpty(exception.getCause())) {
            log.error("其他未知异常:{}", exception.getMessage());
        }
        return ResponseResult.error(500,exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseResult<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        log.error("参数类型转换异常:{}", exception.getMessage());
        return ResponseResult.error(400, "参数格式错误：" + exception.getName() + " 应为数字类型");
    }

}
