package com.lxw.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.OsInfo;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lxw.dto.ad.AdDto;
import com.lxw.dto.article.ArticlePageDto;
import com.lxw.dto.user.UserDto;
import com.lxw.dto.user.UserListPageDto;
import com.lxw.entity.*;
import com.lxw.service.*;
import com.lxw.utils.CommonPage;
import com.lxw.utils.CommonResult;
import com.lxw.vo.AdVo;
import com.lxw.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.xml.stream.events.DTD;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/lxw2000")
@Slf4j
public class AdminController {
    @Autowired
    private IAdminService adminService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private IArticleTagService articleTagService;
    @Autowired
    private IArticleTagListService articleTagListService;
    @Autowired
    private IArticleTypeService articleTypeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private ILinkService linkService;
    @Autowired
    private IAdService adService;
    @Autowired
    private IAdTypeService adTypeService;
    @Autowired
    private IUploadFileListService uploadFileListService;

    /**
     * 登录
     *
     * @param request
     * @return
     */
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        if (Objects.nonNull(request.getSession().getAttribute("admin"))) {
            return "redirect:/lxw2000/";
        }
        return "/admin/adminLogin";
    }

    /**
     * 登录验证
     *
     * @param request
     * @param adminName
     * @param adminPassword
     * @param verifyCode
     * @return
     */
    @PostMapping("/adminLogin")
    @ResponseBody
    public CommonResult adminLogin(HttpServletRequest request, String adminName, String adminPassword, String verifyCode) {
        HttpSession session = request.getSession();
        if (StrUtil.isBlank(verifyCode) || !verifyCode.equals(session.getAttribute("circleCaptchaCode"))) {
            session.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("验证码不正确，请重试！");
        }
        Admin admin = adminService.getOne(Wrappers.<Admin>lambdaQuery().eq(Admin::getAdminName, adminName)
                .eq(Admin::getAdminPassword, SecureUtil.md5(adminName + adminPassword)), false);
        if (Objects.isNull(admin)) {
            request.removeAttribute("circleCaptchaCode");
            return CommonResult.failed("用户名或密码不正确！");
        }
        session.setAttribute("admin", admin);
        return CommonResult.success("登录成功！");
    }


    /**
     * 退出登录
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public String logOut(HttpServletRequest request) {
        request.getSession().removeAttribute("admin");
        return "redirect:/lxw2000/login";
    }


    @GetMapping("/")
    /**
     * 管理端-首页
     */
    public String adminIndex(Model model) {
        //获取系统信息
        HostInfo hostInfo = SystemUtil.getHostInfo();
        OsInfo osInfo = SystemUtil.getOsInfo();
        model.addAttribute("hostAddress", hostInfo.getAddress());
        model.addAttribute("osName", osInfo.getName());
        //文章数量
        int articleCount = articleService.count();
        int articleTagCount = articleTagService.count();
        int articleTypeCount = articleTypeService.count();
        model.addAttribute("articleCount", articleCount);
        model.addAttribute("articleTagCount", articleTagCount);
        model.addAttribute("articleTypeCount", articleTypeCount);
        //用户数量
        int userCount = userService.count();
        model.addAttribute("userCount", userCount);

        return "admin/index";
    }

    /**
     * 管理端-功能开关
     *
     * @return
     */
    @GetMapping("/Manager")
    public String Manager() {
        return "admin/manager";
    }

    @PostMapping("/CommentController")
    @ResponseBody
    public CommonResult commentController(Boolean bool) {
        if (bool) {
            servletContext.setAttribute("Comment",true);
            return CommonResult.success("评论功能已开启！");
        }
        servletContext.removeAttribute("Comment");
        return CommonResult.success("评论功能关闭了！");

    }

    /**
     * 管理端-用户列表
     *
     * @param userListPageDto
     * @param model
     * @return
     */
    @GetMapping("/user/list")
    public String userList(UserListPageDto userListPageDto, Model model) {
        Integer pageNumber = userListPageDto.getPageNumber();
        Integer pageSize = userListPageDto.getPageSize();
        String userName = userListPageDto.getUserName();
        IPage<User> userPage = new Page<>(pageNumber, pageSize);
        LambdaQueryWrapper<User> userLambdaQueryWrapper = Wrappers.<User>lambdaQuery().orderByDesc(User::getUserRegisterTime);
        if (StrUtil.isNotBlank(userName)) {
            userLambdaQueryWrapper.like(User::getUserName, userName);
            model.addAttribute("userName", userName);
        }
        IPage<User> userIPage = userService.page(userPage, userLambdaQueryWrapper);
        model.addAttribute("userPage", CommonPage.restPage(userIPage));
        return "/admin/userList";
    }

    /**
     * 删除用户
     */
    @PostMapping("/user/del")
    @ResponseBody
    public CommonResult userDel(String userId) {
        if (StrUtil.isBlank(userId)) {
            return CommonResult.failed("参数错误，请刷新重试！");
        }
        if (articleService.count(Wrappers.<Article>lambdaQuery().eq(Article::getUserId, userId)) > 0) {
            return CommonResult.failed("该用户发布过文章，无法删除，请尝试冻结用户");
        }
        if (userService.removeById(userId)) {
            return CommonResult.success("删除成功！");
        }
        return CommonResult.failed("删除失败");
    }

    /**
     * 更新用户
     */
    @PostMapping("user/update")
    @ResponseBody
    public CommonResult userUpdate(UserDto userDto) {
        String userId = userDto.getUserId();
        String userPassword = userDto.getUserPassword();
        User user = userService.getById(userId);
        Date userRegisterTime = user.getUserRegisterTime();
        if (Objects.isNull(user)) {
            CommonResult.failed("用户id 不正确");
        }

        if (StrUtil.isBlank(userPassword)) {
            userDto.setUserPassword(null);
        } else {
            userDto.setUserPassword(SecureUtil.md5(userRegisterTime + userPassword));
        }
        BeanUtils.copyProperties(userDto, user);
        if (userService.updateById(user)) {
            return CommonResult.success("更新成功！");
        }
        return CommonResult.failed("更新失败,请重试！");
    }

    /**
     * 文章类型列表，包含文章数量
     *
     * @return
     */
    @GetMapping("/article/type/list")
    public String articleTypeList(Model model, String articleTypeParentId) {
        List<ArticleType> articleType0List = articleTypeService.list(Wrappers.<ArticleType>lambdaQuery().isNull(ArticleType::getArticleTypeParentId).or().eq(ArticleType::getArticleTypeParentId, "").orderByAsc(ArticleType::getArticleTypeSort));
        LambdaQueryWrapper<ArticleType> queryWrapper = Wrappers.<ArticleType>lambdaQuery()
                .isNotNull(ArticleType::getArticleTypeParentId)
                .ne(ArticleType::getArticleTypeParentId, "")
                .orderByAsc(ArticleType::getArticleTypeSort);
        if (StrUtil.isNotBlank(articleTypeParentId)) {
            queryWrapper.eq(ArticleType::getArticleTypeParentId, articleTypeParentId);
            model.addAttribute("articleTypeName", articleTypeService.getById(articleTypeParentId).getArticleTypeName());
        }
        List<ArticleType> articleType1List = articleTypeService.list(queryWrapper);


        model.addAttribute("articleType0List", articleType0List);
        model.addAttribute("articleType1List", articleType1List);
        return "/admin/articleTypeList";
    }

    /**
     * 添加文章类型
     *
     * @param articleType
     * @return
     */
    @PostMapping("/article/type/addOrUpdate")
    @ResponseBody
    public CommonResult articleTypeAdd(@Valid ArticleType articleType) {
        servletContext.removeAttribute("articleTypeList");
        String articleTypeId = articleType.getArticleTypeId();
        if (StrUtil.isNotBlank(articleType.getArticleTypeParentId()) && StrUtil.isNotBlank(articleType.getArticleTypeId()) && articleType.getArticleTypeParentId().equals(articleType.getArticleTypeId())) {
            return CommonResult.failed("不能将自己分配到自己的目录下");
        }

        if (StrUtil.isBlank(articleTypeId)) {
            articleType.setArticleTypeAddTime(DateUtil.date());
            if (articleTypeService.save(articleType)) {

                return CommonResult.success("添加成功");
            }
        }
        if (articleTypeService.updateById(articleType)) {
            return CommonResult.success("修改成功");
        }

        return CommonResult.failed("操作失败");
    }

    /**
     * 删除文章类型
     */
    @PostMapping("/article/type/del")
    @ResponseBody
    public CommonResult delArticleType(@NotBlank(message = "文章分类Id 不能为空") String articleTypeId) {
        if (articleService.count(Wrappers.<Article>lambdaQuery().
                eq(Article::getArticleTypeId, articleTypeId)) > 0) {
            return CommonResult.failed("请先删除该分类下的文章");
        }
        if (articleTypeService.count(Wrappers.<ArticleType>lambdaQuery().
                eq(ArticleType::getArticleTypeParentId, articleTypeId)) > 0) {
            return CommonResult.failed("请先删除该分类的下级");
        }
        if (articleTypeService.removeById(articleTypeId)) {
            servletContext.removeAttribute("articleTypeList");
            return CommonResult.success("删除成功！");
        }
        return CommonResult.failed("删除失败，请重试！");
    }

    /**
     * 文章标签页
     */
    @GetMapping("/article/tag/list")
    public String articleTagList(Model model) {
        List<ArticleTag> articleTagList = articleTagService.list(Wrappers.<ArticleTag>lambdaQuery().orderByDesc(ArticleTag::getArticleTagAddTime));
        model.addAttribute("articleTagList", articleTagList);
        return "/admin/articleTagList";
    }

    /**
     * 修改文章标签
     */
    @PostMapping("/article/tag/addOrUpdate")
    @ResponseBody
    public CommonResult addOrUpdateArticleTag(ArticleTag articleTag) {
        servletContext.removeAttribute("articleTagList");
        String articleTagId = articleTag.getArticleTagId();
        if (StrUtil.isBlank(articleTagId)) {
            articleTag.setArticleTagAddTime(DateUtil.date());
            if (articleTagService.save(articleTag)) {
                return CommonResult.success("添加文章标签成功！");
            }
            return CommonResult.failed("添加标签失败！");
        }
        if (articleTagService.updateById(articleTag)) {
            return CommonResult.success("修改文章标签成功！");
        }
        return CommonResult.failed("修改失败！");
    }

    /**
     * 删除文章标签
     */
    @PostMapping("/article/tag/del")
    @ResponseBody
    public CommonResult delArticleTag(@NotBlank(message = "删除失败，没有获取到标签id") String articleTagId) {
        if (articleTagListService.count(Wrappers.<ArticleTagList>lambdaQuery().
                eq(ArticleTagList::getArticleTagId, articleTagId)) > 0) {
            return CommonResult.failed("该问标签已经被使用，无法删除，请先删除和文章的关联");
        }
        if (articleTagService.removeById(articleTagId)) {
            servletContext.removeAttribute("articleTagList");
            return CommonResult.success("删除文章标签成功！");
        }
        return CommonResult.failed("删除失败！");
    }

    /**
     * 文章列表
     */
    @GetMapping("/article/list")
    public String articleList(ArticlePageDto articlePageDto, Model model) {
        Page<ArticleVo> articleVoPage = new Page<>(articlePageDto.getPageNumber(), articlePageDto.getPageSize());
        IPage<ArticleVo> articleVoIPage = articleService.articleList(articleVoPage, articlePageDto.getArticleTitle(), null);
        model.addAttribute("articleVoIPage", CommonPage.restPage(articleVoIPage));
        if (StrUtil.isNotBlank(articlePageDto.getArticleTitle())) {
            model.addAttribute("articleTitle", articlePageDto.getArticleTitle());
        }
        return "/admin/articleList";
    }

    /**
     * 设为热门文章
     */
    @PostMapping("/article/hot")
    @ResponseBody
    public CommonResult articleHot(String articleId) {
        Article article = articleService.getOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, articleId));
        Integer articleHot = article.getArticleHot();
        try {
            if (articleHot == 1) {
                article.setArticleHot(0);
                articleService.updateById(article);
                servletContext.removeAttribute("articleHotList");
                return CommonResult.success("取消热门成功");
            } else {
                article.setArticleHot(1);
                articleService.updateById(article);
                servletContext.removeAttribute("articleHotList");
                return CommonResult.success("设置热门成功");
            }
        } catch (Exception e) {
            return CommonResult.failed("出错了！");
        }
    }

    /**
     * 文章删除
     */
    @PostMapping("/article/del")
    @ResponseBody
    public CommonResult articleDel(String articleId) {
        return articleService.delArticle(articleId);
    }

    /**
     * 友情连接列表页面
     */
    @GetMapping("/link/list")
    public String linkList(Model model) {
        List<Link> linkList = linkService.list(Wrappers.<Link>lambdaQuery().orderByAsc(Link::getLinkSort));
        model.addAttribute("linkList", linkList);
        return "/admin/linkList";
    }

    /**
     * 更新友情链接
     */
    @PostMapping("/link/addOrUpdate")
    @ResponseBody
    public CommonResult linkAddOrUpdate(Link link) {
        servletContext.removeAttribute("linkList");
        String linkId = link.getLinkId();
        if (StrUtil.isBlank(linkId)) {
            link.setLinkAddTime(DateUtil.date());
            if (linkService.save(link)) {
                return CommonResult.success("增加成功！");
            }
            return CommonResult.failed("增加失败！");
        }
        if (linkService.updateById(link)) {
            return CommonResult.success("更新成功！");
        }
        return CommonResult.failed("更新失败！");
    }

    /**
     * 删除友情链接
     */
    @PostMapping("/link/del")
    @ResponseBody
    public CommonResult linkDel(String linkId) {
        if (linkService.removeById(linkId)) {
            servletContext.removeAttribute("linkList");
            return CommonResult.success("删除成功！");
        }
        return CommonResult.failed("删除失败！");
    }

    /**
     * 广告列表
     */
    @GetMapping("/ad/list")
    public String adList(String adTypeId, Model model) {
        List<AdType> adTypeList = adTypeService.list(Wrappers.<AdType>lambdaQuery().orderByAsc(AdType::getAdTypeId));
        model.addAttribute("adTypeList", adTypeList);
        List<AdVo> adVoList = adService.adList(adTypeId);
        model.addAttribute("adVoList", adVoList);
        return "/admin/adList";
    }

    /**
     * 更新，添加广告
     */
    @PostMapping("/ad/addOrUpdate")
    @ResponseBody
    public CommonResult addOrUpdateAd(AdDto adDto, MultipartFile file) throws Exception {
        if (Objects.nonNull(file)) {
            adDto.setAdImgUrl(uploadFileListService.getUploadFileUrl(file));
        }
        String adId = adDto.getAdId();
        Ad ad = new Ad();
        BeanUtils.copyProperties(adDto, ad);
        ad.setAdBeginTime(DateUtil.parseDateTime(adDto.getAdBeginTime()));
        ad.setAdEndTime(DateUtil.parseDateTime(adDto.getAdEndTime()));

        //移除首页广告缓存
        servletContext.removeAttribute("adIndexList");
        servletContext.removeAttribute("adArticleList");

        if (StrUtil.isBlank(adId)) {
            ad.setAdAddTime(DateUtil.date());
            if (adService.save(ad)) {
                return CommonResult.success("添加成功！");
            }
            return CommonResult.failed("添加失败！");
        }
        if (adService.updateById(ad)) {
            return CommonResult.success("更新成功！");
        }
        return CommonResult.failed("更新失败！");
    }

    /**
     * 更新广告类型
     */
    @PostMapping("/ad/type/addOrUpdate")
    @ResponseBody
    public CommonResult adTypeAddOrUpdate(AdType adType) {
        String adTypeId = adType.getAdTypeId();
        if (StrUtil.isBlank(adTypeId)) {
            //添加广告类型
            adType.setAdTypeAddTime(DateUtil.date());
            if (adTypeService.save(adType)) {
                return CommonResult.success("添加成功");
            }
            return CommonResult.success("添加失败");
        }

        //修改广告类型
        if (adTypeService.updateById(adType)) {
            return CommonResult.success("修改成功");
        }
        return CommonResult.failed("修改失败");
    }

    /**
     * 删除广告
     */
    @PostMapping("ad/del")
    @ResponseBody
    public CommonResult delAd(String adId) {
        if (adService.removeById(adId)) {
            servletContext.removeAttribute("adIndexList");
            servletContext.removeAttribute("adArticleList");
            return CommonResult.success("删除成功！");
        }
        return CommonResult.failed("删除失败！");
    }
}
