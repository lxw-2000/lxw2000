package com.lxw.mapper;

import com.lxw.entity.ArticleType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxw.vo.ArticleTypeTreeVo;

import java.util.List;

/**
 * <p>
 * 文章分类i Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface ArticleTypeMapper extends BaseMapper<ArticleType> {

    List<ArticleTypeTreeVo> getIndexArticleTypeList(String articleTypeParentId);
}
