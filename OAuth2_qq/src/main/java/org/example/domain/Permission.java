package org.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 权限表
 * @TableName t_permission
 */
@TableName(value ="t_permission")
@Data
public class Permission {
    /**
     * 权限ID, 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 权限名称 (e.g., 查看用户)
     */
    private String name;

    /**
     * 权限编码 (e.g., sys:user:list), 必须唯一
     */
    private String code;

    /**
     * 权限类型 (1:目录, 2:菜单, 3:按钮)
     */
    private PermissionType type;

    /**
     * 创建时间
     */
    private Date createTime;
}