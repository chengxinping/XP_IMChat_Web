package cn.xpcheng.xpimchat.web.push.bean.db;


import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户的Model 对应数据库
 *
 * @author 哎呦哥哥
 *         Created by qq972 on 2017/7/12.
 */
@Entity
@Table(name = "TB_USER")
public class User implements Principal{

    @Id
    @PrimaryKeyJoinColumn
    //主键存储类型 uuid
    @GeneratedValue(generator = "uuid")
    //把生成器的名字定义为uuid2 uuid2在hibernate是常规的  toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false) //不可更改 不可为空
    private String id; //主键

    @Column(updatable = false, unique = true, length = 128)
    private String name;

    @Column(updatable = false, unique = true, length = 62)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column
    private String portrait;

    @Column
    private String description;

    @Column(nullable = false)
    private int sex = 0;  //有初始值  所以不为空

    @Column(unique = true)
    private String token;

    @Column
    private String pushId;  //推送的pushID

    //定义为创建时间戳 创建时写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //定义为更新时间戳 更新时写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //最后一次收到消息的时间
    @Column
    private LocalDateTime lastReceiveAt = LocalDateTime.now();


    //我关注人的列表
    //对应数据库字段为TB_FOLLOW.originId
    //定义为懒加载  默认加载User信息时  并不查询这个集合
    @JoinColumn(name = "originId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> following = new HashSet<>();

    //关注我的列表
    @JoinColumn(name = "targetId")
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFollow> followers = new HashSet<>();


    //我所有创建的群  对应字段为group中的ownerId
    @JoinColumn(name = "ownerId")
    //懒加载集合 当访问group.size()也不加载具体信息，只有遍历才加载
    @LazyCollection(LazyCollectionOption.EXTRA)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Group> groups = new HashSet<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDateTime getLastReceiveAt() {
        return lastReceiveAt;
    }

    public void setLastReceiveAt(LocalDateTime lastReceiveAt) {
        this.lastReceiveAt = lastReceiveAt;
    }

    public Set<UserFollow> getFollowing() {
        return following;
    }

    public void setFollowing(Set<UserFollow> following) {
        this.following = following;
    }

    public Set<UserFollow> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<UserFollow> followers) {
        this.followers = followers;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public void setGroups(Set<Group> groups) {
        this.groups = groups;
    }
}
