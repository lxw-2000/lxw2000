package com.lxw.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxw.entity.User;
import com.lxw.utils.CommonResult;
import com.lxw.vo.ArticleVo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 文章列表
     * @param articleVoPage
     * @param articleTitle
     * @param userId
     * @return
     */
    IPage<ArticleVo> articleList(Page<ArticleVo> articleVoPage, @Param("articleTitle") String articleTitle, @Param("userId") String userId);

    IPage<ArticleVo> articleListView(Page<ArticleVo> articlePage, String articleTitle, String articleTypeId);

    IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId);

    ArticleVo getArticle(String articleId);

}
