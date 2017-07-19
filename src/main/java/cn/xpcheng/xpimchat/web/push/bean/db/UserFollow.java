package cn.xpcheng.xpimchat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户关系Model  用于用户之间好友的实现
 *
 * @author 哎呦哥哥
 *         Created by qq972 on 2017/7/12.
 */
@Entity
@Table(name = "TB_USER_FOLLOW")
public class UserFollow {
    @Id
    @PrimaryKeyJoinColumn
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false)
    private String id;

    //定义发起人  比我我关注你  origin就是我
    //多对一   ->可以关注很多人  每一次关注都是一条数据
    // 一个user对应多个userFollow
    //optional 不可选，必须存储  一条记录必须有一个被关注人
    @ManyToOne(optional = false)
    @JoinColumn(name = "originId")  //定义关联表字段名为originId 对应User.id
    private User origin;

    //把这个列提取到model中
    @Column(nullable = false, updatable = false, insertable = false)
    private String originId;

    //被关注人   你
    //被关注人可以被很多人也可以被很多人关注
    @ManyToOne(optional = false)
    @JoinColumn(name = "targetId")
    private User target;

    //把这个列提取到model中
    @Column(nullable = false, updatable = false, insertable = false)
    private String targetId;

    //别名，对target的备注
    @Column
    private String alias;

    //定义为创建时间戳 创建时写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //定义为更新时间戳 更新时写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
}
