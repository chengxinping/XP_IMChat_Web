package cn.xpcheng.xpimchat.web.push.service;

import cn.xpcheng.xpimchat.web.push.bean.api.base.ResponseModel;
import cn.xpcheng.xpimchat.web.push.bean.api.user.UpdateInfoModel;
import cn.xpcheng.xpimchat.web.push.bean.card.UserCard;
import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.factory.UserFactory;
import com.google.common.base.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * @author 哎呦哥哥
 *         用户信息处理
 *         Created by qq972 on 2017/7/27.
 */
@Path("/user")
public class UserService extends BaseService {

    /**
     * 用户信息修改接口
     *
     * @param model model
     * @return 返回自己的个人信息
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> update(UpdateInfoModel model) {
        if (!UpdateInfoModel.check(model))
            return ResponseModel.buildParameterError();

        User user = getSelf();
        //更新用户信息
        user = model.updateToUser(user);
        //更新到数据库
        user = UserFactory.update(user);
        //构建自己的用户信息
        return ResponseModel.buildOk(new UserCard(user, true));
    }
}
