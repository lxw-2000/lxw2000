package com.lxw.dto.base;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BasePageDto {
    @NotNull(message = "未获取到当前页码")
    private Integer PageNumber = 1;
    private Integer PageSize = 20;
}
