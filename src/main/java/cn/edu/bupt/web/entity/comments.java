package cn.edu.bupt.web.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class comments {
    private String productUid;
    private String userId;
    private String Comment;
    private Date commentDate;
}