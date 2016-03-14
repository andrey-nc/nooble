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
                <input class="uri" type="text" name="q" required/>
            </label>
            <br>
            <label>
                Глубина рекурсии:
                <input class="text" type="range" name="searchDepth"/>
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
            ${INDEX_STATUS}:
        </#if>
    </div>
    <div>
    <#if URL_STATUS??>
        ${URL_STATUS}:
    </#if>
    </div>


</body>
</html>