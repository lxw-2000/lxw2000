<#include "../import/adminTop.ftl">
<#if managerList?? && managerList?size gt 0>
    <#list managerList as manager>
        <div class="panel col-sm-6">
            <div class="panel-body">
                <div class="switch text-left">
                    <input type="checkbox" id="${(manager.managerId)!}" <#if manager.managerBool == 0><#else>checked="checked"</#if>
                           onclick="manager('${(manager.managerId)!}')">
                    <label><b>${(manager.managerName)!}</b></label>
                </div>
            </div>
        </div>
    </#list>
</#if>
<script>
    function manager(managerId) {
        let num = 0;
        if ($("#"+managerId).prop('checked')) {
            num = 1;
        }
        $.post("/lxw2000/managerController", {
                managerId: managerId,
                num: num
            },
            function (data) {
                zuiMsg(data.message);
                return;
            });
    }



</script>
<#include "../import/bottom.ftl">