package com.lxw.service.impl;

import com.lxw.entity.Admin;
import com.lxw.mapper.AdMapper;
import com.lxw.mapper.AdminMapper;
import com.lxw.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxw.vo.AdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 管理员 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
}
