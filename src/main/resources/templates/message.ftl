<#import "/spring.ftl" as spring/>

<#macro status>
<div>
    <#if indexCount??>
        <div class="status">
            Indexed pages: <b>${indexCount}:</b>
        </div>
    </#if>
</div>
<div>
    <#if status.error??>
        <div class="status">
        ${status.error}: <#if query??> ${query} </#if>
        </div>
    </#if>

    <#if status.success??>
        <div class="status">
        ${status.success}: <#if query??> ${query} </#if>
        </div>
    </#if>
</div>
</#macro>