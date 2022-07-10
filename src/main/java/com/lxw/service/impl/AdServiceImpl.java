package com.lxw.service.impl;

import com.lxw.entity.Ad;
import com.lxw.mapper.AdMapper;
import com.lxw.service.IAdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxw.vo.AdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 广告 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements IAdService {
    @Autowired
    private AdMapper adMapper;
    @Override
    public List<AdVo> adList(String adTypeId) {
        return adMapper.adList(adTypeId);
    }
}
