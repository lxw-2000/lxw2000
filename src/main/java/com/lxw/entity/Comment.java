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
 * 文章评论
 * </p>
 *
 * @author jobob
 * @since 2022-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文章评论id
     */
    @TableId(value = "comment_id")
    private String commentId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 用户id（评论人）
     */
    private String userId;

    /**
     * 文章评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 点赞次数
     */
    private Integer commentGoodNumber;


}
