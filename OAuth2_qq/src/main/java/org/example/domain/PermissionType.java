package org.example.domain;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 权限类型枚举
 */
@Getter
public enum PermissionType {
    /**
     * 目录
     */
    DIRECTORY(1, "目录"),
    /**
     * 菜单
     */
    MENU(2, "菜单"),
    /**
     * 按钮
     */
    BUTTON(3, "按钮");

    @EnumValue
    private final int value;
    private final String description;

    PermissionType(int value, String description) {
        this.value = value;
        this.description = description;
    }
} 