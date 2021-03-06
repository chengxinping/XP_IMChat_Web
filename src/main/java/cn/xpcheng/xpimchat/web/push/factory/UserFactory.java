package cn.xpcheng.xpimchat.web.push.factory;

import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.bean.db.UserFollow;
import cn.xpcheng.xpimchat.web.push.utils.Hib;
import cn.xpcheng.xpimchat.web.push.utils.TextUtil;
import com.google.common.base.Strings;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author 哎呦哥哥
 *         Created by qq972 on 2017/7/23.
 */
public class UserFactory {

    //只能自己使用
    public static User findByToken(String token) {
        return Hib.query(session -> (User) session
                .createQuery("from User where token=:inToken")
                .setParameter("inToken", token)
                .uniqueResult());
    }


    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    public static User findByName(String name) {
        return Hib.query(session -> (User) session
                .createQuery("from User where name=:inName")
                .setParameter("inName", name)
                .uniqueResult());
    }

    public static User findById(String id) {
        return Hib.query(session -> session.get(User.class, id));
    }

    /**
     * 更新用户信息到数据库
     *
     * @param user user
     * @return User
     */
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 给当前的账户绑定PushID
     *
     * @param user   当前用户
     * @param pushId pushId
     * @return User
     */
    public static User bindPushId(User user, String pushId) {

        if (Strings.isNullOrEmpty(pushId))
            return null;
        //1.查询是否有其他账户绑定这个设备 取消绑定，避免推送混乱
        //查询的列表不能包括自己
        Hib.queryOnly(session -> {
            List<User> userList = (List<User>) session.createQuery("from User where lower(pushId) =:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();

            for (User u : userList) {
                u.setPushId(null);
                session.saveOrUpdate(user);
            }
        });
        //2.
        if (pushId.equalsIgnoreCase(user.getPushId()))
            //之前已经绑定 不需要额外绑定
            return user;
        else {
            //如果当前账号的pushId和现在要绑定的不一样
            //需要单点登录，之前的设备退出登录
            //给之前设备推送一条消息
            if (Strings.isNullOrEmpty(user.getPushId())) {
                //TODO 推送一个退出登录消息
            }
            user.setPushId(pushId);
            return update(user);
        }
    }

    /**
     * 使用账户密码登录
     *
     * @param account  账号
     * @param password 密码
     * @return User
     */
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        final String encodePassword = encodePassword(password);
        //寻找用户
        User user = Hib.query(session -> (User) session
                .createQuery("from User where phone=:inPhone and password=:inPassword")
                .setParameter("inPhone", accountStr)
                .setParameter("inPassword", encodePassword)
                .uniqueResult());
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 用户注册操作  需要写入数据库，返回数据库里面User信息
     *
     * @param account  账户
     * @param password 密码
     * @param name     用户名
     * @return User
     */
    public static User register(String account, String password, String name) {
        account = account.trim(); //去除电话号码 首尾空格
        password = encodePassword(password); //加密密码

        User user = createUser(account, password, name);
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 注册部分新建用户逻辑
     *
     * @param account  手机号
     * @param password 加密后的密码
     * @param name     用户名
     * @return 返回一个User
     */
    private static User createUser(String account, String password, String name) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);  //账户就是手机号
        //数据库存储
        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    /**
     * 把一个用户进行登录操作
     * 本质上是对token操作
     *
     * @param user User
     * @return User
     */
    private static User login(User user) {
        //使用一个随机的UUID充当token
        String newToken = UUID.randomUUID().toString();
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        return update(user);
    }

    private static String encodePassword(String password) {
        password = password.trim();
        //密码md5加密
        password = TextUtil.getMD5(password);
        //再进行一次对称的Base64加密
        return TextUtil.encodeBase64(password);
    }

    /**
     * 获取我的联系人列表
     *
     * @param self user
     * @return List<User>
     */
    public static List<User> contacts(User self) {
        return Hib.query(session -> {
            //重新加载一次，和当前session绑定
            session.load(self, self.getId());
            Set<UserFollow> follows = self.getFollowing();
            //java8类似Rxjava
            return follows.stream()
                    .map(UserFollow::getTarget)
                    .collect(Collectors.toList());
        });
    }

    /**
     * 关注人的操作
     *
     * @param origin 发起者
     * @param target 被关注人
     * @param alias  备注
     * @return 被关注的人
     */
    public static User follow(final User origin, final User target, final String alias) {
        UserFollow userFollow = getUserFollow(origin, target);
        if (userFollow != null)
            return userFollow.getTarget();
        return Hib.query(session -> {
            session.load(origin, origin.getId());
            session.load(target, target.getId());
            UserFollow originFollow = new UserFollow();
            originFollow.setOrigin(origin);
            originFollow.setTarget(target);
            originFollow.setAlias(alias);
            //发起者是他，我是被关注
            UserFollow targetFollow = new UserFollow();
            targetFollow.setOrigin(target);
            targetFollow.setTarget(origin);

            session.save(originFollow);
            session.save(targetFollow);
            return target;
        });
    }

    /**
     * 查询两个人是否关注
     *
     * @param origin 发起者
     * @param target 被关注者
     * @return 中间类UserFollow
     */
    public static UserFollow getUserFollow(final User origin, final User target) {
        return Hib.query(session -> (UserFollow) session.createQuery("from UserFollow where originId=:originId and targetId=:targetId")
                .setParameter("originId", origin.getId())
                .setParameter("targetId", target.getId())
                .setMaxResults(1)
                .uniqueResult());
    }

    /**
     * 搜索联系人;.
     *
     * @param name 联系人name
     * @return list name为空，返回最近的新用户
     */
    public static List<User> search(String name) {

        return Hib.query(session -> (List<User>) session.createQuery("from User where lower(name) like :name and portrait is not null and description is not null ")
                .setParameter("name", "%" + (Strings.isNullOrEmpty(name) ? "" : name) + "%")
                // .setMaxResults(20)  //至多20条
                .list());
    }
}
