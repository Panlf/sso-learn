package com.plf.learn.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author panlf
 * @date 2022/1/2
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private int id;
    private String username;
    private String password;
    private String salt;

    private List<Role> roles;
}
