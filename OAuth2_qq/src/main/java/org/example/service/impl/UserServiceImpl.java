package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.domain.User;
import org.example.service.UserService;
import org.example.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 86133
* @description 针对表【t_user(用户表)】的数据库操作Service实现
* @createDate 2024-08-10 16:39:13
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Override
    public User findByUsername(String username) {
        return this.baseMapper.selectByUsername(username);
    }
}




