$(document).ready(function () {
    $.get("http://localhost:8080/jsoupRBC", {}, function (data, status) {
        var rbcNews = "<span class='description-item'>Новости RBC: <br></span>";
        $("#rbc-div").append(rbcNews);
        if (status === "success") {
            for (var key in data) {
                if (data.hasOwnProperty(key)) {
                    $("#rbc-div")
                        .append("<span class='rbc-news-item a-style'><a href='"
                        + data[key] + "'>" + key + "<br></a></span>");
                }
            }
        }
    });
});