package cn.xpcheng.xpimchat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author 哎呦哥哥
 *         Created by qq972 on 2017/7/23.
 */
@Entity
@Table(name = "TB_GROUP")
public class Group {

    @Id
    @PrimaryKeyJoinColumn
    //主键存储类型 uuid
    @GeneratedValue(generator = "uuid")
    //把生成器的名字定义为uuid2 uuid2在hibernate是常规的  toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false) //不可更改 不可为空
    private String id; //主键

    //群名称
    @Column(updatable = false, unique = true, length = 128)
    private String name;

    //群描述
    @Column(nullable = false)
    private String description;

    //群头像
    @Column(nullable = false)
    private String picture;

    //定义为创建时间戳 创建时写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //定义为更新时间戳 更新时写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();


    //群创建者   optional（可选） 必须有创建者   fetch加载方式急加载（加载群信息必须加载owner）
    //cascade:联级级别为ALL，代表所有的更改都将进行更新
    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "ownerId")
    private User owner;
    @Column(nullable = false, updatable = false, insertable = false)
    private String ownerId;



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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
}
