function showValue(newValue){
    document.getElementById("rangeValue").innerHTML = newValue;
}

function showStatus(value){
    clearStatus();
    document.getElementById("status").innerHTML = value;
}

function clearStatus(){
    document.getElementById("status").innerHTML = "";
}

function loadDocs() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (xhttp.readyState == 4 && xhttp.status == 200) {
            document.getElementById("demo").innerHTML = xhttp.responseText;
        }
    };
    xhttp.open("GET", ".txt", true);
    xhttp.send();
}

function ajax() {
    $(document).on("click", "#viewMore", function () {
        $.get("search", function (responseText) {
            $("#newResults").text(responseText);
        });
    });
}

$(document).on("click", "#viewMore", function() {
    $.get("search", function(responseJson) {
        var $ul = $("<ul>").appendTo($("#newResults"));
        $.each(responseJson, function(index, item) {
            $("<li>").text(item).appendTo($ul);
        });
    });
});

$(document).on("click", "#viewMore", function() {
    $.get("search", function(responseJson) {
        var $div = $("<div>").appendTo($("#newResults"));
        $.each(responseJson, function(index, doc) {
            $("<h3>").attr("class","page-title").append($("<a>")).attr("href",doc.path).text(doc.title).appendTo($div)
            $("<div>").attr("class","page-path").text(doc.path).appendTo($div)
            $("<div>").attr("class","page-preview").text(doc.content).appendTo($div)
            $("<br>").appendTo($div)
        });
    });
});


