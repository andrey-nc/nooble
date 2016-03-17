<#-- @ftlroot "." -->
<!DOCTYPE html>
<html lang="en">

<head>
    <title>Index - ${appName!"nooble"}</title>
    <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/4.0.0-alpha.2/css/bootstrap.min.css" />
    <script type="text/javascript" src="/js/script.js" ></script>
    <#import "/spring.ftl" as spring/>
    <#import "/message.ftl" as message/>
</head>

<body>

    <div class="nooble">
        nooble
    </div>
    <div class="index">
        <form name="search" method="post">
            <label>
                URI:
                <input class="uri" type="text" name="q" required value="${query!""}" />
            </label>
            <input type="submit" value="Index" onclick="showStatus('Indexing...')"/>
            <br>
            <label>
                Глубина рекурсии:
                <#assign rangeValue=1>
                <span id="rangeValue">${rangeValue}</span>
                <span class=""><sup>1</sup></span>
                <input class="text" type="range" name="depth" min="1" max="5" step="1" value="${rangeValue}"
                        onchange="showValue(this.value)" oninput="showValue(this.value)"/>
                <span class=""><sup>5</sup></span>
            </label>
        </form>
    </div>
    <div class="status" id="status">
        <@message.status/>
    </div>
</body>

</html>