<#include "../import/adminTop.ftl">
<div class="panel col-xs-12">
    <div class="col-xs-6">
        <div class="panel-body">
            <h3><i class="icon icon-desktop"></i>系统类型:${osName!}</h3>
            <h3><i class="icon icon-server"></i>服务器IP:${hostAddress!}</h3>
            <h3><i class="icon icon-user"></i>用户数:${userCount!}</h3>
        </div>
    </div>
    <div class="col-xs-6">
        <div class="panel-body">
            <h3><i class="icon icon-th"></i>文章类型数:${articleTypeCount!}</h3>
            <h3><i class="icon icon-tags"></i>文章标签数:${articleTagCount!}</h3>
            <h3><i class="icon icon-stack"></i>文章数:${articleCount!}</h3>
        </div>
    </div>
</div>
<#include "../import/bottom.ftl">