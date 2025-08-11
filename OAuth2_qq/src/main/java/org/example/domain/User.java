package org.example.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * 用户表
 * @TableName t_user
 */
@TableName(value ="t_user")
@Data
public class User {
    /**
     * 用户ID, 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户名, 唯一
     */
    private String username;

    /**
     * 已加密的密码
     */
    private String password;

    /**
     * 账户是否启用 (1:启用, 0:禁用)
     */
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}