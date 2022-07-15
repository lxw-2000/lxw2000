package com.lxw.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.dto.article.PublishArticleActionDto;
import com.lxw.entity.*;
import com.lxw.exception.CommonException;
import com.lxw.mapper.ArticleMapper;
import com.lxw.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxw.utils.CommonResult;
import com.lxw.utils.CommonUtils;
import com.lxw.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private IUserCollectionArticleService userCollectionArticleService;

    @Autowired
    private IArticleTagListService articleTagListService;

    @Autowired
    private IUploadFileListService uploadFileListService;

    @Autowired
    private ICommentService commentService;

    @Autowired
    private ICommentReplyService commentReplyService;

    @Autowired
    private ServletContext servletContext;

    @Override
    public IPage<ArticleVo> articleList(Page<ArticleVo> articleVoPage, String articleTitle, String userId) {
        return articleMapper.articleList(articleVoPage,articleTitle,userId);
    }

    @Override
    public IPage<ArticleVo> articleListView(Page<ArticleVo> articlePage, String articleTitle, String articleTypeId) {
        return articleMapper.articleListView(articlePage,articleTitle,articleTypeId);
    }

    @Override
    public IPage<ArticleVo> tagArticleList(Page<ArticleVo> articlePage, String articleTagId) {
        return articleMapper.tagArticleList(articlePage,articleTagId);
    }

    @Override
    public ArticleVo getArticle(String articleId) {
        return articleMapper.getArticle(articleId);
    }

    /**
     * 收藏文章
     *
     * @param user
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult articleCollection(User user, String articleId) {

        if (userCollectionArticleService.count(Wrappers.<UserCollectionArticle>lambdaQuery()
                .eq(UserCollectionArticle::getUserId, user.getUserId())
                .eq(UserCollectionArticle::getArticleId, articleId)) > 0) {
            return CommonResult.failed("客官！该文章您已经收藏了，请到个人中心查看哦");
        }

        UserCollectionArticle userCollectionArticle = new UserCollectionArticle();
        userCollectionArticle.setUserId(user.getUserId());
        userCollectionArticle.setArticleId(articleId);
        userCollectionArticle.setUserCollectionArticleTime(DateUtil.date());
        if (!userCollectionArticleService.save(userCollectionArticle)) {
            return CommonResult.failed("收藏失败啦，刷新页面重试");
        }

        //添加收藏次数
        Article article = getById(articleId);
        if (Objects.nonNull(article)) {
            Integer articleCollectionNumber = article.getArticleCollectionNumber();
            ++articleCollectionNumber;
            article.setArticleCollectionNumber(articleCollectionNumber);
            if (!updateById(article)) {
                throw new CommonException("收藏失败");
            }
        }
        return CommonResult.success("恭喜，收藏成功，客官可以到个人中心查看");
    }

    /**
     * 发布文章方法
     * 2.图片先于文章保存，如果不保存文章图片也会提交，不合理
     * @param publishArticleActionDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult publishArticleAction(HttpServletRequest request, PublishArticleActionDto publishArticleActionDto) {
        //保存文章
        User user = (User) request.getSession().getAttribute("user");
        Article article = new Article();
        article.setArticleId(publishArticleActionDto.getArticleId());
        article.setArticleTypeId(publishArticleActionDto.getArticleTypeId());
        article.setArticleCoverUrl(publishArticleActionDto.getArticleCoverUrl());
        article.setUserId(user.getUserId());
        article.setArticleTitle(publishArticleActionDto.getArticleTitle());
        article.setArticleContext(publishArticleActionDto.getArticleContext());
        //新增初始化文章
        if (StrUtil.isBlank(article.getArticleId())) {
            article.setArticleAddTime(DateUtil.date());
            article.setArticleGoodNumber(0);
            article.setArticleLookNumber(0);
            article.setArticleHot(0);
            article.setArticleCollectionNumber(0);
        }
        //如果是修改的话，查出原来文章的点赞数，收藏数，是否为热门文章，观看数
        Article articleOld = getById(article.getArticleId());
        article.setArticleGoodNumber(articleOld.getArticleGoodNumber());
        article.setArticleLookNumber(articleOld.getArticleLookNumber());
        article.setArticleHot(articleOld.getArticleHot());
        article.setArticleCollectionNumber(articleOld.getArticleCollectionNumber());

        if (!saveOrUpdate(article)) {
            return CommonResult.failed("操作失败，请刷新页面重试!");
        }

        //保持文章的标签
        String[] articleTagIds = publishArticleActionDto.getArticleTagIds();
        if (Objects.nonNull(articleTagIds) && articleTagIds.length > 0) {
            //删除原先的标签数据
            articleTagListService.remove(Wrappers.<ArticleTagList>lambdaQuery().eq(ArticleTagList::getArticleId, article.getArticleId()));
        }

        ArrayList<ArticleTagList> articleTagLists = new ArrayList<>();
        for (String articleTagId : articleTagIds) {
            ArticleTagList articleTagList = new ArticleTagList();
            articleTagList.setArticleId(article.getArticleId());
            articleTagList.setArticleTagId(articleTagId);
            articleTagLists.add(articleTagList);
        }
        if (!articleTagListService.saveBatch(articleTagLists, 50)) {
            throw new CommonException("操作文章失败，保存文章标签失败");
        }

        servletContext.removeAttribute("articleTypeList");

        return CommonResult.success("操作成功");
    }

    /**
     * 删除文章，同时要删除标签表，评论表，用户收藏表，图片封面的内容
     * @param articleId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult delArticle(String articleId) {
        if (StrUtil.isBlank(articleId)){
            return CommonResult.failed("参数不正确，删除失败！");
        }
        Article article = getById(articleId);
        if (Objects.isNull(article)){
            return CommonResult.failed("该文章可能已被删除，请刷新页面");
        }

        //删除文章封面图片
        String articleCoverUrl = article.getArticleCoverUrl();
        //检查该图片是否被其他文章使用
        if(count(Wrappers.<Article>lambdaQuery().eq(Article::getArticleCoverUrl,articleCoverUrl))<=1){
            File file = new File(CommonUtils.getClasspath(), "static" + article.getArticleCoverUrl());
            FileUtil.del(file);
            //删除文件对应的数据库记录
            uploadFileListService.remove(Wrappers.<UploadFileList>lambdaQuery().eq(UploadFileList::getFileUrl,article.getArticleCoverUrl()));
        }

        //删除文章
        if (!removeById(articleId)) {
            return CommonResult.failed("删除失败，可能该文章已经被删除");
        }
        //删除文章对应的标签
        articleTagListService.remove(Wrappers.<ArticleTagList>lambdaQuery().eq(ArticleTagList::getArticleId, articleId));

        //删除用户收藏的文章
        userCollectionArticleService.remove(Wrappers.<UserCollectionArticle>lambdaQuery().eq(UserCollectionArticle::getArticleId, articleId));

        //删除文章评论
        List<Comment> commentList = commentService.list(Wrappers.<Comment>lambdaQuery()
                .eq(Comment::getArticleId, articleId)
                .select(Comment::getCommentId));
        if (CollUtil.isNotEmpty(commentList)) {
            List<String> commentIdList = commentList.stream().map(Comment::getCommentId).collect(Collectors.toList());
            commentService.removeByIds(commentIdList);
            commentReplyService.remove(Wrappers.<CommentReply>lambdaQuery().in(CommentReply::getCommentId, commentIdList));
        }

        servletContext.removeAttribute("articleTypeList");
        return CommonResult.success("删除成功！");
    }
}
