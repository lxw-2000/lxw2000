package com.lxw.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.entity.Comment;
import com.lxw.mapper.CommentMapper;
import com.lxw.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxw.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文章评论 服务实现类
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Override
    public IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId) {
        return commentMapper.getArticleCommentList(commentVoPage,articleId);
    }
}
