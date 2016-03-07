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
                <input class="uri" type="range" name="searchDepth"/>
            </label>
            <input type="submit" value="Index" />
        </form>
    </div>

</body>
</html>