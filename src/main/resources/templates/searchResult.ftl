<!DOCTYPE html>
<html lang="en">

<head>
    <title>${query!"search"} - ${appName!"nooble"}</title>
    <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
    <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/4.0.0-alpha.2/css/bootstrap.min.css" />
</head>

<body>
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="webjars/bootstrap/4.0.0-alpha.2/js/bootstrap.min.js"></script>

    <div class="" id="searchForm">
        <div class="search-bg">
            <form method="get" action="/search">
                <label class="nooble-little" for="query">
                    <a href="/">${appName!"nooble"}</a>
                </label>
                <input class="uri" type="text" name="q" id="query" value="${query!""}" required/>
                <input type="submit" value="Search" />
            </form>
        </div>
    </div>
    <br>
    <div class="search-result">
        <div class="" id="">
            <#if statusError??>
                ${statusError}
            <#else>
                <div class="result-text">
                    Результатов: <#if pages??>${resultCount!"-"} (${searchTime!"-"} сек.)</#if>
                </div>
            </#if>
        </div>
        <div class="results" id="results">
            <#if pages??>
                <#list pages as page>
                    <h3 class="page-title">
                        <a href="${page.path}">${page.title}</a>
                    </h3>
                    <div class="page-path">
                        ${page.path}
                    </div>
                    <div class="page-preview">
                        <#--${page.get("preview")}-->
                    </div>
                    <br>
                </#list>
            </#if>
        </div>
        <#-- Button "View more ..." -->
        <div id="newResults"></div>
        <#-- Button "View more ..." -->
        <div id="view-block">
<#--
            <form method="get" action="/search">
                <input type="hidden" name="q" value="${query!""}">
                <input type="hidden" name="start" value="${start!""}">
                <input type="submit" id="viewMore" value="View more ...">
            </form>
-->
        </div>
    </div>


</body>
</html>