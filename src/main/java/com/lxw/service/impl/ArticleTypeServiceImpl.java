package com.lxw.service.impl;

import com.lxw.entity.ArticleType;
import com.lxw.mapper.ArticleTypeMapper;
import com.lxw.service.IArticleTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxw.vo.ArticleTypeTreeVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文章分类i 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Service
public class ArticleTypeServiceImpl extends ServiceImpl<ArticleTypeMapper, ArticleType> implements IArticleTypeService {
    @Autowired
    private ArticleTypeMapper articleTypeMapper;
    /**
     * 获取首页文章类型树形目录
     * @param articleTypeParentId
     * @return
     */
    @Override
    public List<ArticleTypeTreeVo> getIndexArticleTypeList(String articleTypeParentId) {
        return articleTypeMapper.getIndexArticleTypeList(articleTypeParentId);
    }
}
