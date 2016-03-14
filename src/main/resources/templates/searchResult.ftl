<!DOCTYPE html>
<html lang="en">

<head>
    <title>nooble - search result</title>
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
                    <a href="/">
                    <#if appName??>
                        ${appName}:
                    <#else>
                        nooble
                    </#if>
                    </a>
                </label>
                <input class="uri" type="text" name="q" id="query" required/>
                <input type="submit" value="Search" />
            </form>
        </div>
    </div>
    <br>
    <div class="search-result">
        <div class="result-text">
            Результатов: ${resultCount}
        </div>
        <div>
            <#if results??>
                <#list pages as page>
                    <div class="page-title">
                        <a href="${page.get("path")}">${page.get("title")}</a>
                    </div>
                    <div class="page-path">
                        ${page.get("path")}
                    </div>
                    <div class="page-preview">
                        <#--${page.get("preview")}-->
                    </div>
                    <br>
                </#list>
            </#if>
        </div>
    </div>

</body>
</html>