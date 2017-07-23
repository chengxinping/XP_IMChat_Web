package cn.xpcheng.xpimchat.web.push.service;

import cn.xpcheng.xpimchat.web.push.bean.db.User;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * @author 哎呦哥哥
 * @time 2017年7月1日 10:56:43
 */

//实际访问路径  127.0.0.1/api/account
@Path("/account")
public class AccountService {
    //GET 127.0.0.1/api/account/login
    @GET
    @Path("/login")
    public String get() {
        return "My First Restful:Login";
    }

    //POST 127.0.0.1/api/account/login
    @POST
    @Path("/login")
    //传入
    @Consumes(MediaType.APPLICATION_JSON)
    //返回

    public User post() {
        User user = new User();
        user.setName("哎呦哥哥");
        user.setSex(1);
        return user;
    }
}
