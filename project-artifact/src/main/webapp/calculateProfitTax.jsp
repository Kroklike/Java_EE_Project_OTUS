<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Подсчитать налог на прибыль организации</title>
    <script src="libs/jquery/jquery-3.3.1.min.js"></script>
    <link type="text/css" rel="stylesheet" href="css/application.css">
</head>
<body>

<form id="taxResult" method="post" action="http://localhost:8080/taxCalculator">
    <div>
        <label for="income">Доходы (от реализации и внереализационные) нарастающим итогом с начала года:</label>
        <input type="number" id="income" step="0.01" name="income" autocomplete="off" placeholder="0,00"/>
    </div>
    <div>
        <label for="expenses">Расходы (связанные с производством и реализацией и внереализационные):</label>
        <input type="number" id="expenses" step="0.01" name="expenses" autocomplete="off" placeholder="0,00"/>
    </div>
    <div>
        <label for="taxRate">Налоговая ставка отчетного периода (год):</label>
        <input type="number" id="taxRate" step="0.01" name="taxRate" autocomplete="off" placeholder="0,00"/>
    </div>
    <div>
        <label for="tax">Налог:</label>
        <label id="tax"></label>
    </div>
    <div class="submitArea">
        <input type="submit" id="submit" value="Рассчитать налог!"/>
    </div>
</form>

<script type='text/javascript'>
    $("#taxResult").submit(function (event) {
        event.preventDefault();
        var $form = $(this), url = $form.attr('action');
        var calculate = $.post(url, {
            income: $('#income').val(),
            expenses: $('#expenses').val(),
            taxRate: $('#taxRate').val()
        });
        calculate.done(function (data) {
            document.getElementById('tax').innerHTML = data;
        });
    });
</script>

</body>
</html>