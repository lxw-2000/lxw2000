package com.lxw.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lxw.vo.CommentVo;

/**
 * <p>
 * 文章评论 服务类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface ICommentService extends IService<Comment> {

    IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId);
}
