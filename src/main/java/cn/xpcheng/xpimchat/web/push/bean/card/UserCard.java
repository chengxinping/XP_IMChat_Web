package cn.xpcheng.xpimchat.web.push.bean.card;

import cn.xpcheng.xpimchat.web.push.bean.db.User;
import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

/**
 * Created by qq972 on 2017/7/23.
 */
public class UserCard {
    @Expose
    private String id;

    @Expose
    private String name;

    @Expose
    private String phone;

    @Expose
    private String portrait;

    @Expose
    private String desc;

    @Expose
    private int sex = 0;  //有初始值  所以不为空

    //用户关注人数量
    @Expose
    private int follows;

    //用户粉丝数量
    @Expose
    private int following;

    //用户信息最后更新时间
    @Expose
    private LocalDateTime notifyAt;

    //我与当前User关系状态（是否已经关注）
    @Expose
    private boolean isFollow;

    public UserCard(final User user) {
        this(user, false);
    }

    public UserCard(final User user, final boolean isFollow) {
        this.isFollow = isFollow;
        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.desc = user.getDescription();
        this.sex = user.getSex();
        this.notifyAt = user.getUpdateAt();
        //TODO 得到关注人和粉丝的数量
//        user.getFollowers().size() //会报错
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public LocalDateTime getNotifyAt() {
        return notifyAt;
    }

    public void setNotifyAt(LocalDateTime notifyAt) {
        this.notifyAt = notifyAt;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }
}
