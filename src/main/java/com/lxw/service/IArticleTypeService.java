package com.lxw.service;

import com.lxw.entity.ArticleType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.vo.ArticleTypeTreeVo;

import java.util.List;

/**
 * <p>
 * 文章分类i 服务类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface IArticleTypeService extends IService<ArticleType> {

    List<ArticleTypeTreeVo> getIndexArticleTypeList(String articleTypeParentId);
}
