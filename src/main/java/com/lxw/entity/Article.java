package com.lxw.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 文章
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章id
     */
    @TableId(value = "article_id")
    private String articleId;

    /**
     * 文章分类id
     */
    private String articleTypeId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章封面
     */
    private String articleCoverUrl;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章添加时间
     */
    private Date articleAddTime;

    /**
     * 文章内容
     */
    private String articleContext;

    /**
     * 点赞次数
     */
    private Integer articleGoodNumber;

    /**
     * 观看次数
     */
    private Integer articleLookNumber;

    /**
     * 是否是热门文章 0否，1是
     */
    private Integer articleHot;

    /**
     * 收藏次数
     */
    private Integer articleCollectionNumber;


}
