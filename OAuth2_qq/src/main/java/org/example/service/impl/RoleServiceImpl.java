package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.Role;
import org.example.service.RoleService;
import org.example.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author 29323
* @description 针对表【t_role(角色表)】的数据库操作Service实现
* @createDate 2025-08-09 21:51:45
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




