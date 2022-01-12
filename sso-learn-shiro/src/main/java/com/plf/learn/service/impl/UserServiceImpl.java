package com.plf.learn.service.impl;

import com.plf.learn.bean.Perms;
import com.plf.learn.bean.Role;
import com.plf.learn.bean.User;
import com.plf.learn.mapper.UserMapper;
import com.plf.learn.service.UserService;
import com.plf.learn.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author panlf
 * @date 2022/1/2
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;

    @Override
    public void register(User user) {
        // 明文密码 md5+salt+hash散列
        String salt = SaltUtils.getDefaultSalt();
        user.setSalt(salt);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),salt,1024);
        user.setPassword(md5Hash.toHex());
        userMapper.save(user);
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public List<Role> findRolesByUserName(String username) {
        return userMapper.findRolesByUserName(username).getRoles();
    }

    @Override
    public List<Perms> findPermsByRoleId(int id) {
        return userMapper.findPermsByRoleId(id);
    }
}
