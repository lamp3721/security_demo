package org.example.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * 通用的API响应封装类。
 * @param <T> 响应数据的类型
 */
@Data
public class ApiResponse<T> implements Serializable {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    // 私有化构造函数，强制使用静态工厂方法创建实例
    private ApiResponse(boolean success, Integer code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功的响应，包含数据。
     * @param data 响应数据
     * @return 成功的ApiResponse实例
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), "操作成功", data);
    }
    
    /**
     * 成功的响应，包含自定义消息和数据。
     * @param message 自定义消息
     * @param data 响应数据
     * @return 成功的ApiResponse实例
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), message, data);
    }
    
    /**
     * 成功的响应，只包含自定义消息，无数据。
     * @param message 自定义消息
     * @return 成功的ApiResponse实例
     */
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, HttpStatus.OK.value(), message, null);
    }

    /**
     * 失败的响应。
     * @param code 错误状态码
     * @param message 错误消息
     * @return 失败的ApiResponse实例
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }
    
    /**
     * 失败的响应，使用默认的500状态码。
     * @param message 错误消息
     * @return 失败的ApiResponse实例
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }
} 