package cn.xpcheng.xpimchat.web.push.bean.api.account;

import cn.xpcheng.xpimchat.web.push.bean.card.UserCard;
import cn.xpcheng.xpimchat.web.push.bean.db.User;
import com.google.gson.annotations.Expose;

/**
 * 账户部分返回的Model
 *
 * @author qq972
 *         Created by qq972 on 2017/7/23.
 */
public class AccountRspModel {
    //用户基本信息
    @Expose
    private UserCard user;
    //当前登录账号
    @Expose
    private String account;
    //当前登录成功后获取的token
    @Expose
    private String token;
    @Expose
    //是否已经绑定到了设备pushId
    private boolean isBind;

    public AccountRspModel(User user) {
        this(user, false);
    }

    public AccountRspModel(User user, boolean isBind) {
        this.user = new UserCard(user);
        this.account = user.getPhone();
        this.token = user.getToken();
        this.isBind = isBind;
    }

    public UserCard getUser() {
        return user;
    }

    public void setUser(UserCard user) {
        this.user = user;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
