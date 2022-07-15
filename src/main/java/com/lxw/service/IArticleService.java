package com.lxw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.dto.article.PublishArticleActionDto;
import com.lxw.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.entity.User;
import com.lxw.utils.CommonResult;
import com.lxw.vo.ArticleVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface IArticleService extends IService<Article> {

    IPage<ArticleVo> articleList(Page<ArticleVo> articleVoPage, String articleTitle, String userId);

    IPage<ArticleVo> articleListView(Page<ArticleVo> articlePage, String articleTitle, String articleTypeId);

    IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId);

    ArticleVo getArticle(String articleId);

    CommonResult articleCollection(User user, String articleId);

    CommonResult publishArticleAction(HttpServletRequest request, PublishArticleActionDto publishArticleActionDto);

    CommonResult delArticle(String articleId);
}
