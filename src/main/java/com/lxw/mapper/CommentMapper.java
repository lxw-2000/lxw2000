package com.lxw.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxw.vo.CommentVo;

/**
 * <p>
 * 文章评论 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId);
}
