<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Департаменты</title>
    <script src="libs/jquery/jquery-3.3.1.min.js"></script>
    <script src="js/functions.js"></script>
    <link type="text/css" rel="stylesheet" href="css/application.css">
</head>
<body>

<button id="btnAdd">New</button>
<table id="tblData" class="departments">
    <thead>
    <tr>
        <th>Department name</th>
        <th>City</th>
        <th>Full address</th>
        <th></th>
    </tr>
    </thead>
    <tbody>
    </tbody>
</table>

<script>
    $("#btnAdd").bind("click", addDepart);
    loadAllDepart();
</script>

</body>
</html>