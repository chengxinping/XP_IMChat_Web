package cn.xpcheng.xpimchat.web.push.bean.db;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author 哎呦哥哥
 *         Created by qq972 on 2017/7/19.
 */

@Entity
@Table(name = "TB_MESSAGE")
public class Message {

    public static final int TYPE_STR = 1; //字符串类型
    public static final int TYPE_PIC = 2; //图片类型
    public static final int TYPE_FILE = 3; //字符串类型
    public static final int TYPE_AUDIO = 4; //语音类型

    @Id
    @PrimaryKeyJoinColumn
    //id由代码  客户端生成
    //避免复杂的服务器和客户端的映射关系
    //把生成器的名字定义为uuid2 uuid2在hibernate是常规的  toString
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false) //不可更改 不可为空
    private String id; //主键

    //内容
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    //附件
    @Column
    private String attach;

    //消息类型
    @Column(nullable = false)
    private int type;

    //定义为创建时间戳 创建时写入
    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createAt = LocalDateTime.now();

    //定义为更新时间戳 更新时写入
    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now();

    //发送者
    @JoinColumn(name = "sendId")
    @ManyToOne(optional = false)
    private User sender;
    //这个字段仅仅只为对应sender数据库字段sendId
    @Column(nullable = false, updatable = false, insertable = false)
    private String sendId;

    //接受者  可为空
    @JoinColumn(name = "receiverId")
    @ManyToOne
    private User receiver;
    @Column(updatable = false, insertable = false)
    private String receiverId;

    //群接受者  可为空
    @JoinColumn(name = "groupId")
    @ManyToOne
    private Group group;
    @Column(updatable = false, insertable = false)
    private String groupId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
