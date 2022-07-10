package com.lxw.service;

import com.lxw.entity.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告 服务类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface IAdService extends IService<Ad> {

    List<AdVo> adList(String adTypeId);
}
