package com.lxw.mapper;

import com.lxw.entity.Ad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxw.vo.AdVo;

import java.util.List;

/**
 * <p>
 * 广告 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface AdMapper extends BaseMapper<Ad> {

    List<AdVo> adList(String adTypeId);
}
