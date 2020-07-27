package com.nowcoder.community.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String conversation;
    private String content;
    private int status;
    private Date createTime;
}
