package com.second.practiceproject2.model;

import org.springframework.stereotype.Component;

/**
 * Created by nowcoder on 2016/7/3.
 */
@Component
public class HostHolder {
    //给每个线程都分配了对象，每个线程对users都有拷贝，通过公共接口访问
    private static ThreadLocal<User> users = new ThreadLocal<User>();
    //getUser时会取当前线程关联的变量
    public User getUser() {
        return users.get();
    }

    public void setUser(User user) {
        users.set(user);
    }

    public void clear() {
        users.remove();;
    }
}
