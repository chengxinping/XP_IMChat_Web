package cn.xpcheng.xpimchat.web.push.service;

import javax.ws.rs.*;
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

}
