package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.Permission;
import org.example.service.PermissionService;
import org.example.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author 29323
* @description 针对表【t_permission(权限表)】的数据库操作Service实现
* @createDate 2025-08-09 21:51:45
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




