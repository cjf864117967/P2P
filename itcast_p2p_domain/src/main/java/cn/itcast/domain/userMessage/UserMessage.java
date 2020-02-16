package cn.itcast.domain.userMessage;

import javax.persistence.*;

@Entity
@Table(name="t_station_information")
public class UserMessage {
    @Id
    @GeneratedValue()
    @Column(name="s_id", nullable=false)
    private int id;            //主键id

    @Column(name="s_message_state")
    private Integer  messageState; //消息状态

    @Column(name="s_message_content")
    private String messageContent; //消息内容

    @Column(name="s_system_time")
    private String sendTime;        //发送时间

    @Column(name="s_information_type")
    private int messageType;  //消息类型

    @Column(name="s_receive_user_id")
    private int s_receive_user_id;//用户ID

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMessageState() {
        return messageState == null ? 0 : messageState;
    }

    public void setMessageState(Integer messageState) {
        this.messageState = messageState;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public Integer getS_receive_user_id() {
        return s_receive_user_id;
    }

    public void setS_receive_user_id(Integer s_receive_user_id) {
        this.s_receive_user_id = s_receive_user_id;
    }
}
