package org.example.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用的API响应封装类。
 * @param <T> 响应数据的类型
 */
@Data
public class ApiResponse<T> implements Serializable {

    private boolean success;
    private Integer code;
    private String message;
    private T data;

    private ApiResponse(ResponseStatus status, T data, String messageOverride) {
        this.success = status == ResponseStatus.SUCCESS;
        this.code = status.getCode();
        this.message = messageOverride != null ? messageOverride : status.getMessage();
        this.data = data;
    }

    // --- 成功的响应 ---

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, data, null);
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(ResponseStatus.SUCCESS, null, null);
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, data, message);
    }
    
    public static <T> ApiResponse<T> successMessage(String message) {
        return new ApiResponse<>(ResponseStatus.SUCCESS, null, message);
    }

    // --- 失败的响应 ---

    public static <T> ApiResponse<T> error(ResponseStatus status) {
        return new ApiResponse<>(status, null, null);
    }

    public static <T> ApiResponse<T> error(ResponseStatus status, String message) {
        return new ApiResponse<>(status, null, message);
    }
} 