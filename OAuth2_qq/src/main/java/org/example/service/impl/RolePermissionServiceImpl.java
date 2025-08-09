package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.RolePermission;
import org.example.service.RolePermissionService;
import org.example.mapper.RolePermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 29323
* @description 针对表【t_role_permission(角色权限关联表)】的数据库操作Service实现
* @createDate 2025-08-09 21:51:45
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService{

}




