package com.lxw.dto.user;

import com.lxw.dto.base.BasePageDto;
import lombok.Data;

@Data
public class UserListPageDto extends BasePageDto {
    //用户名
    private String userName;
}
