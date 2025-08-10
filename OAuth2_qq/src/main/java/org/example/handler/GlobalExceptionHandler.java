package org.example.handler;

import jakarta.servlet.http.HttpServletResponse;
import org.example.dto.ApiResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器。
 * 使用@RestControllerAdvice注解，可以捕获所有Controller中抛出的异常。
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理自定义的ApiException。
     * @param ex 自定义的业务异常
     * @param response HTTP响应对象，用于手动设置状态码
     * @return 标准的ApiResponse错误体
     */
    @ExceptionHandler(ApiException.class)
    @ResponseBody // 确保返回的是响应体
    public ApiResponse<Object> handleApiException(ApiException ex, HttpServletResponse response) {
        // 手动设置HTTP响应状态码
        response.setStatus(ex.getStatus().value());
        // 返回标准格式的错误响应体
        return ApiResponse.error(ex.getStatus().value(), ex.getMessage());
    }
} 