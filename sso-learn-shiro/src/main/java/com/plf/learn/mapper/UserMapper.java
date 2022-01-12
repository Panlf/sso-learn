package com.plf.learn.mapper;

import com.plf.learn.bean.Perms;
import com.plf.learn.bean.Role;
import com.plf.learn.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author panlf
 * @date 2022/1/2
 */
@Mapper
public interface UserMapper {
    void save(User user);

    User findByUserName(String username);

    User findRolesByUserName(String username);

    List<Perms> findPermsByRoleId(int id);
}
