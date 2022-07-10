<#include "../import/adminTop.ftl">
<div class="panel col-sm-12">
    <div class="panel-body col-sm-6">
        <div class="switch text-left">
            <input type="checkbox">
            <label><b>夜间模式</b></label>
        </div>
    </div>
    <div class="panel-body col-sm-6">
        <div class="switch text-left">
            <input type="checkbox" onclick="CommentController()" id="checkComment">
            <label><b>评论开启</b></label>
        </div>
    </div>
    <div class="panel-body col-sm-6">
        <div class="switch text-left">
            <input type="checkbox">
            <label><b>测试功能1</b></label>
        </div>
    </div>
    <div class="panel-body col-sm-6">
        <div class="switch text-left">
            <input type="checkbox">
            <label><b>测试功能2</b></label>
        </div>
    </div>
</div>
<script>

    function CommentController() {
        let bool = $("#checkComment").prop('checked');
        $.post("/lxw2000/CommentController", {
                bool: bool
            },
            function (data) {
                zuiMsg(data.message);
                return;
            });
    }


</script>
<#include "../import/bottom.ftl">