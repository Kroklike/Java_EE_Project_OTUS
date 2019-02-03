<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Подсчитать ежемесячный платеж</title>
    <script src="libs/jquery/jquery-3.3.1.min.js"></script>
    <link type="text/css" rel="stylesheet" href="css/application.css">
</head>
<body>

<form id="payments" method="get">

    <div>
        <label for="method">Метод платежа:</label>
        <select id="method" name="method">
            <option selected value="${pageContext.request.contextPath}/api/calculator/v1/calculate">Дифференцированный
                платеж
            </option>
            <option value="${pageContext.request.contextPath}/api/calculator/v2/calculate">Аннуитетный платеж</option>
        </select>
    </div>

    <div>
        <label for="numberOfPeriods">Количество периодов оплаты:</label>
        <input type="number" id="numberOfPeriods" name="numberOfPeriods" autocomplete="off" placeholder="0" min="1"
               required/>
    </div>
    <div>
        <label for="creditAmount">Сумма кредита:</label>
        <input type="number" id="creditAmount" step="0.01" name="creditAmount" autocomplete="off" min="0.01"
               placeholder="0,00"
               required/>
    </div>
    <div>
        <label for="interestRateByYear">Процентная ставка (год):</label>
        <input type="number" id="interestRateByYear" step="0.01" name="interestRateByYear" min="0.01" autocomplete="off"
               placeholder="0,00" required/>
    </div>
    <div>
        <label for="payment">Выплаты:</label>
        <label id="payment"></label>
    </div>
    <div class="submitArea">
        <input type="submit" id="submit" value="Рассчитать!"/>
    </div>
</form>

<script type='text/javascript'>
    $("#payments").submit(function (event) {
        event.preventDefault();
        var url = $("#method").children("option:selected").val();
        var calculate = $.get(url, {
            numberOfPeriods: $('#numberOfPeriods').val(),
            creditAmount: $('#creditAmount').val(),
            interestRateByYear: $('#interestRateByYear').val()
        });
        calculate.done(function (data) {
            document.getElementById('payment').innerHTML = data;
        });
    });
</script>

</body>
</html>