package com.plf.learn;

import com.plf.learn.bean.User;
import com.plf.learn.service.UserService;
import com.plf.learn.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author panlf
 * @date 2022/1/2
 */
@SpringBootTest(classes = ShiroApplication.class)
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private UserService userService;

    @Test
    public void save(){
        System.out.println(userService);
        userService.register(new User().builder().username("zhangsan").password("123456").build());
        userService.register(new User().builder().username("xiaochen").password("123456").build());
    }
}
