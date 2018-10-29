$(document).ready(function () {
    $.get("http://localhost:8080/jsoupRBC", {}, function (data, status) {
        var rbcNews = "Новости RBC: <br>";
        $("#rbc-div").append(rbcNews);
        Array.from(data).forEach(function(item, i, arr) {
            var innerArray = Object.entries(item);
            var newsTitle = innerArray[0];
            var href = innerArray[1];
            var newsItem = "<a href='" + href +"'>" + newsTitle + "</a>";
            $("#rbc-div").append(newsItem);
        });
    });
});