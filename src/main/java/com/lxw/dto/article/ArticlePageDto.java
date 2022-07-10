package com.lxw.dto.article;

import com.lxw.dto.base.BasePageDto;
import lombok.Data;

@Data
public class ArticlePageDto extends BasePageDto {
    //文章标题
    private String articleTitle;
}
