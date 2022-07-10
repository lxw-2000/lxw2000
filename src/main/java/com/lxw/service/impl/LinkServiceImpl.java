package com.lxw.service.impl;

import com.lxw.entity.Link;
import com.lxw.mapper.LinkMapper;
import com.lxw.service.ILinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 友情连接 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements ILinkService {

}
