package com.plf.learn.service;

import com.plf.learn.bean.Perms;
import com.plf.learn.bean.Role;
import com.plf.learn.bean.User;

import java.util.List;

/**
 * @author panlf
 * @date 2022/1/2
 */
public interface UserService {
    void register(User user);

    User findByUserName(String username);

    List<Role> findRolesByUserName(String username);
    
    List<Perms> findPermsByRoleId(int id);
}
