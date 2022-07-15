package com.lxw.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lxw
 * @since 2022-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Manager implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 功能id
     */
    @TableId(value = "manager_id")
    private String managerId;

    /**
     * 功能名称
     */
    private String managerName;

    /**
     * 功能状态
     */
    private Integer managerBool;


}
