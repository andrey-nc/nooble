<#-- @ftlroot "." -->
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Index - ${appName!"nooble"}</title>
    <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
<#--    <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/3.3.6/css/bootstrap.min.css" />-->
    <script type="text/javascript" src="/js/script.js" ></script>
    <#import "/spring.ftl" as spring/>
    <#import "/message.ftl" as message/>
</head>

<body>

    <div class="nooble">
        nooble
    </div>
    <div class="nooble-form">
        <form name="search" method="post">
            <input id="nooble-input" class="uri nooble-input-margin" type="text" name="q" required value="${query!""}" />
            <br>
            <label id="range-text">
                Index depth:
                <#assign depthDefault=1 depthMaxDefault=3>
                <span id="range-value">${depth!depthDefault}</span>
                <input class="index-depth" type="range" name="depth" min="1" max="${depthMax!depthMaxDefault}" step="1" value="${depth!depthDefault}"
                        onchange="showValue(this.value)" oninput="showValue(this.value)"/>
            </label>
            <br>
            <input class="nooble-button" type="submit" value="Index" onclick="showStatus('Indexing...')"/>
        </form>
    </div>
    <div class="status" id="status">
        <@message.status/>
    </div>
</body>

</html>