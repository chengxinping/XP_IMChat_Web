package cn.xpcheng.xpimchat.web.push.service;

import cn.xpcheng.xpimchat.web.push.bean.api.base.ResponseModel;
import cn.xpcheng.xpimchat.web.push.bean.api.user.UpdateInfoModel;
import cn.xpcheng.xpimchat.web.push.bean.card.UserCard;
import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.factory.UserFactory;
import com.google.common.base.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 获取联系人列表
     *
     * @return 联系人列表
     */
    @GET
    @Path("/contact")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> contact() {
        User self = getSelf();

        List<User> users = UserFactory.contacts(self);

        List<UserCard> userCards = users.stream()
                .map(user -> {
                    return new UserCard(self, true);
                })
                .collect(Collectors.toList());
        return ResponseModel.buildOk(userCards);
    }

    /**
     * 关注某人
     *
     * @param followId 关注人的id
     * @return
     */
    @PUT
    @Path("/follow/{followId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> follow(@PathParam("followId") String followId) {
        User self = getSelf();
        //自己不能关注自己
        if (self.getId().equalsIgnoreCase(followId))
            return ResponseModel.buildParameterError();
        User follower = UserFactory.findById(followId);
        if (follower == null)
            return ResponseModel.buildNotFoundUserError(null);
        follower = UserFactory.follow(self, follower, null);
        if (follower == null)
            return ResponseModel.buildServiceError();

        //TODO 通知我关注的人 我关注了他
        return ResponseModel.buildOk(new UserCard(follower, true));

    }
}
