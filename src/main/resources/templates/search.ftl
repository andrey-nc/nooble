<#-- @ftlroot "." -->

<!DOCTYPE html>
<html lang="en">
    <head>
        <title>${appName!"nooble"} search</title>
        <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
        <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/3.3.6/css/bootstrap.min.css" />
        <#import "/spring.ftl" as spring/>
        <#import "/message.ftl" as message/>
    </head>

    <body>

        <div class="nooble-form">
            <div class="nooble">${appName!"nooble"}</div>
            <form name="index" method="get" action="search">
                <input id="nooble-input" class="uri nooble-input-margin" type="text" name="q" required/>
                <br>
                <input class="nooble-button" type="submit" value="Search" onclick="clearStatus()"/>
            </form>
        </div>
        <div class="status" id="status">
            <@message.status/>
        </div>

    </body>
</html>