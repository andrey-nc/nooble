<!DOCTYPE html>
<html lang="en">

<head>
    <title>nooble - index</title>
    <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/4.0.0-alpha.2/css/bootstrap.min.css" />
</head>

<body>

    <div class="nooble">
        nooble
    </div>
    <div class="index">
        <form name="search" method="post">
            <label>
                URI:
                <input class="uri" type="text" name="q" required value="<#if uri??>${uri}</#if>"/>
            </label>
            <br>
            <label>
                Глубина рекурсии:
                1<input class="text" type="range" name="depth" min="1" max="100" value="3"/>100
            </label>
            <input type="submit" value="Index" />
        </form>
    </div>
    <div>
        <#if indexCount??>
            <div class="message">
                Indexed pages: ${indexCount}:
            </div>
        </#if>
    </div>
    <div>
        <#if INDEX_STATUS??>
            <div class="message">
                ${INDEX_STATUS}:
            </div>
        </#if>
    </div>
    <div>
        <#if URL_STATUS??>
            <div class="message">
                ${URL_STATUS}:
            </div>
        </#if>
    </div>


</body>
</html>