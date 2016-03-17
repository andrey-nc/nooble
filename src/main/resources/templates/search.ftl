<#-- @ftlroot "." -->

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>${appName!"nooble"} search</title>
        <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
        <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/4.0.0-alpha.2/css/bootstrap.min.css" />
        <#import "/spring.ftl" as spring/>
        <#import "/message.ftl" as message/>
    </head>

    <body>

        <div class="nooble">
            ${appName!"nooble"}
        </div>

        <div class="index">
            <form name="index" method="get" action="search">
                <input class="uri" type="text" name="q" required/>
                <input type="submit" value="Search" onclick="clearStatus()"/>
            </form>
        </div>
        <div class="status" id="status">
            <@message.status/>
        </div>

    </body>
</html>