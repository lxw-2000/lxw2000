package com.lxw.vo;

import com.lxw.entity.Article;
import lombok.Data;

import java.util.List;

@Data
public class ArticleTypeTreeVo {
    private String articleTypeId;
    private String articleTypeName;
    private List<ArticleTypeTreeVo> articleTypeTreeVoList;
    private List<Article> articleList;
}
