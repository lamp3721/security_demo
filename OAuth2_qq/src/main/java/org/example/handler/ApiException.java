package org.example.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 自定义API业务异常类。
 * 用于在业务逻辑中封装和抛出特定的错误信息和HTTP状态码。
 */
@Getter
public class ApiException extends RuntimeException {

    private final HttpStatus status;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public ApiException(String message) {
        // 默认使用 400 Bad Request 状态码
        this(HttpStatus.BAD_REQUEST, message);
    }
} 