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
                .map(user -> new UserCard(user, true))
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
        if (self.getId().equalsIgnoreCase(followId)
                || Strings.isNullOrEmpty(followId))
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

    /**
     * 获取某人的信息
     *
     * @param id 某人的id
     * @return 某人UserCard
     */
    @GET
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<UserCard> getUser(@PathParam("id") String id) {
        if (Strings.isNullOrEmpty(id))
            return ResponseModel.buildParameterError();
        User self = getSelf();
        if (self.getId().equalsIgnoreCase(id))
            return ResponseModel.buildOk(new UserCard(self, true));
        User user = UserFactory.findById(id);
        if (user == null)
            return ResponseModel.buildNotFoundUserError(null);
        boolean isFollow = UserFactory.getUserFollow(self, user) != null;
        return ResponseModel.buildOk(new UserCard(user, isFollow));
    }

    /**
     * 搜索人
     * 简化分页，只返回20条数据
     *
     * @return 联系人列表
     */
    @GET
    @Path("/search/{name:(.*)?}") //名字为任意字符，可为空
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<List<UserCard>> search(@DefaultValue("") @PathParam("name") String name) {
        User self = getSelf();
        //查询数据
        List<User> searchUsers = UserFactory.search(name);
        //封装成UserCard  并且要判断这些人有没有被关注  状态修改
        final List<User> contacts = UserFactory.contacts(self); //我的联系人

        //java8   user->userCard
        List<UserCard> userCards = searchUsers.stream()
                .map(user -> {
                    //判断搜索的人中是否有自己和联系人
                    //双重循环 低效
                    boolean isFollow = user.getId().equalsIgnoreCase(self.getId())
                            || contacts.stream().anyMatch(
                            user1 -> user1.getId()
                                    .equalsIgnoreCase(user.getId())
                    );

                    return new UserCard(user, isFollow);
                }).collect(Collectors.toList());

        return ResponseModel.buildOk(userCards);
    }
}
