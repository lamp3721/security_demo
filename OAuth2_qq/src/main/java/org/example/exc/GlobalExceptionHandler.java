package org.example.exc;

import jakarta.servlet.http.HttpServletResponse;
import org.example.result.ApiResponse;
import org.example.result.ResponseStatus;
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

        // 如果ApiException是基于ResponseStatus创建的，则直接使用
        if (ex.getResponseStatus() != null) {
            return ApiResponse.error(ex.getResponseStatus());
        }
        
        // 否则，使用传统方式创建
        return ApiResponse.error(ResponseStatus.BAD_REQUEST, ex.getMessage());
    }
} 