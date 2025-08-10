package org.example.service;

import org.example.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 86133
* @description 针对表【t_user(用户表)】的数据库操作Service
* @createDate 2024-08-10 16:39:13
*/
public interface UserService extends IService<User> {
    User findByUsername(String username);
}
