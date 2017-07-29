package cn.xpcheng.xpimchat.web.push.service;

import cn.xpcheng.xpimchat.web.push.bean.db.User;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by qq972 on 2017/7/29.
 */
public class BaseService {
    //添加一个上下文注解
    @Context
    protected SecurityContext securityContext;

    protected User getSelf() {
        return (User) securityContext.getUserPrincipal();
    }

}
