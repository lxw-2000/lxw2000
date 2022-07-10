package com.lxw;
import java.util.Date;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.crypto.SecureUtil;
import com.lxw.entity.Admin;
import com.lxw.entity.Article;
import com.lxw.entity.User;
import com.lxw.service.IAdminService;
import com.lxw.service.IArticleService;
import com.lxw.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class Lxw2000ApplicationTests {
    @Autowired
    private IUserService userService;
    ArrayList<User> userList = new ArrayList();
    
    @Autowired
    private IArticleService articleService;
    ArrayList<Article> articleArrayList = new ArrayList();

    @Autowired
    private IAdminService adminService;
    /**
     * 构造用户测试
     */
    @Test
    void contextLoads() {
        for (int i = 0; i < 50; i++) {
            User user = new User();
            user.setUserName(i+"uName");
            user.setUserPassword(SecureUtil.md5("123456"));
            user.setUserFrozen(0);
            user.setUserPublishArticle(0);
            user.setUserRegisterTime(DateUtil.date());
            userList.add(user);
        }
        userService.saveBatch(userList,50);
    }

    /**
     * 构造文章测试
     */
    @Test
    void article(){
        for (int i = 0; i < 50; i++) {
            Article article = new Article();
            article.setArticleTitle("wenz"+i);
            article.setArticleAddTime(new Date());
            article.setArticleContext("");
            article.setArticleGoodNumber(0);
            article.setArticleLookNumber(0);
            article.setArticleHot(0);
            article.setArticleCollectionNumber(0);
            articleArrayList.add(article);
        }
        articleService.saveBatch(articleArrayList);
    }

    /**
     * 构造Admin用户
     */
    @Test
    void adminData(){
        Admin admin = new Admin();
        admin.setAdminName("admin");
        admin.setAdminPassword(SecureUtil.md5("admin"+"156056"));
        adminService.save(admin);
    }

    @Test
    void huTool(){
        System.out.println(DateUtil.currentSeconds());
    }

}
