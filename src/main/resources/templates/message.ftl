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
    <#if statusError??>
        <div class="status">
        ${statusError}: <#if query??> ${query} </#if>
        </div>
    </#if>

    <#if statusEuccess??>
        <div class="status">
        ${statusSuccess}: <#if query??> ${query} </#if>
        </div>
    </#if>
</div>
</#macro>