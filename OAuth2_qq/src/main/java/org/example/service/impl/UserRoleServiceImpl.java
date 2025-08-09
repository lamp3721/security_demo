package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.UserRole;
import org.example.service.UserRoleService;
import org.example.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 29323
* @description 针对表【t_user_role(用户角色关联表)】的数据库操作Service实现
* @createDate 2025-08-09 21:51:45
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




