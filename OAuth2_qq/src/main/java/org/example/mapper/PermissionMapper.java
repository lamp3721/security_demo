package org.example.mapper;

import org.example.domain.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 86133
* @description 针对表【t_permission(权限表)】的数据库操作Mapper
* @createDate 2024-08-10 16:39:13
* @Entity org.example.domain.Permission
*/
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据用户ID查询用户的权限编码列表
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);
}




