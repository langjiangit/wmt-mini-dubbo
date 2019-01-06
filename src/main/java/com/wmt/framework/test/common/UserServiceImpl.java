package com.wmt.framework.test.common;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weimiantong created on 17/2/12.
 * @version $Id$
 */
@Service("userService")
public class UserServiceImpl implements UserService {


    private static final Map<String, User> userMap = new HashMap<String, User>();


    static {
        userMap.put("kongxuan", new User("kongxuan", "kongxuan@163.com"));
        userMap.put("weimiantong", new User("weimiantong", "weimiantong@163.com"));
    }

    @Override
    public User findByName(String userName) {
        return userMap.get(userName);
    }
}
