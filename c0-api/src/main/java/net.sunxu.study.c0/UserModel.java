package net.sunxu.study.c0;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
public class UserModel {
    private Long id;

    private String name;

    private String password;

    private String mailAddress;

    private UserState userState;

    private String remark;

    private Date createTime;
}
