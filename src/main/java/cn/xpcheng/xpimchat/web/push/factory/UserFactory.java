package cn.xpcheng.xpimchat.web.push.factory;

import cn.xpcheng.xpimchat.web.push.bean.db.User;
import cn.xpcheng.xpimchat.web.push.utils.Hib;
import cn.xpcheng.xpimchat.web.push.utils.TextUtil;
import org.hibernate.Session;

/**
 * @author 哎呦哥哥
 *         Created by qq972 on 2017/7/23.
 */
public class UserFactory {
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

        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);  //账户就是手机号

        //进行数据库操作 首先创建一个会话
        Session session = Hib.session();
        session.beginTransaction();
        try {
            //保存操作
            session.save(user);
            //提交事务
            session.getTransaction().commit();
            return user;
        } catch (Exception e) {
            session.getTransaction().rollback(); //回滚
            return null;
        }
    }

    private static String encodePassword(String password) {
        password = password.trim();
        //密码md5加密
        password = TextUtil.getMD5(password);
        //再进行一次对称的Base64加密
        return TextUtil.encodeBase64(password);
    }
}
