package cn.xpcheng.xpimchat.web.push.service;

import cn.xpcheng.xpimchat.web.push.bean.api.account.RegisterModel;
import cn.xpcheng.xpimchat.web.push.bean.card.UserCard;
import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author 哎呦哥哥
 * @time 2017年7月1日 10:56:43
 */

//实际访问路径  127.0.0.1/api/account
@Path("/account")
public class AccountService {

    @POST
    @Path("/register")
    //传入
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public UserCard register(RegisterModel model) {
        User user = UserFactory.findByPhone(model.getAccount().trim());
        if (user != null) {
            UserCard card = new UserCard();
            card.setName("该手机号已被注册！");
            return card;
        }
        user = UserFactory.findByName(model.getName().trim());
        if (user != null) {
            UserCard card = new UserCard();
            card.setName("该昵称已被占用！");
            return card;
        }
        user = UserFactory.register(model.getAccount(), model.getPassword(), model.getName());
        if (user != null) {
            UserCard card = new UserCard();
            card.setName(user.getName());
            card.setPhone(user.getPhone());
            card.setSex(user.getSex());
            card.setNotifyAt(user.getUpdateAt());
            card.setFollow(true);
            return card;
        }
        return null;
    }
}
