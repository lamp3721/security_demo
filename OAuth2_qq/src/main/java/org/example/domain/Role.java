package org.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 角色表
 * @TableName t_role
 */
@TableName(value ="t_role")
@Data
public class Role {
    /**
     * 角色ID, 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 角色名称 (e.g., 管理员)
     */
    private String name;

    /**
     * 角色编码 (e.g., ROLE_ADMIN), 必须唯一
     */
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 角色拥有的权限列表
     */
    @com.baomidou.mybatisplus.annotation.TableField(exist = false)
    private List<Permission> permissions;
}