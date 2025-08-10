package org.example.result;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * API响应状态的枚举类。
 * 集中管理所有API的响应状态码和消息。
 */
@Getter
public enum ResponseStatus {
    SUCCESS(HttpStatus.OK.value(), "操作成功"),

    // --- 客户端错误 ---
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "请求参数错误或无效"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED.value(), "认证失败，请先登录"),
    FORBIDDEN(HttpStatus.FORBIDDEN.value(), "权限不足，无法访问此资源"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "请求的资源不存在"),

    // --- 服务端错误 ---
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器内部错误，请联系管理员"),

    // --- 自定义业务错误 ---
    USERNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST.value(), "错误：该用户名已被占用！"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED.value(), "用户名或密码错误"),
    ACCOUNT_DISABLED(HttpStatus.UNAUTHORIZED.value(), "账户已被禁用，请联系管理员");


    private final int code;
    private final String message;

    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }
} 