<!DOCTYPE html>
<html lang="en">
    <head>
        <title>${appName!"oops"} search</title>
        <link rel="stylesheet" type="text/css" media="all" href="/css/styles.css" />
        <link rel="stylesheet" type="text/css" media="all" href="/webjars/bootstrap/4.0.0-alpha.2/css/bootstrap.min.css" />
    </head>

    <body>

        <div class="nooble">
            ${appName!"oops"}
        </div>

        <div class="index">
            <form name="index" method="get" action="search">
                <input class="uri" type="text" name="q" required/>
                <input type="submit" value="Search" />
            </form>
        </div>

    </body>
</html>