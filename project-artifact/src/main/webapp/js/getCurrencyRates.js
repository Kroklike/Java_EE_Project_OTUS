$(document).ready(function () {
    $.get("http://localhost:8080/currencyRatesToJson", {}, function (data, status) {
        var currencyRates = "Текущий курс валют: <br>";
        var currencyData = "EUR " + data.EUR + " | USD " + data.USD;
        $("#currency-div").append(currencyRates);
        $("#currency-div").append(currencyData);
    });
});