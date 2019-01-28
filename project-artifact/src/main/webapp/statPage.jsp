<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="stat" uri="/stattags" %>
<%@ page import="java.util.List" %>
<% List eList = (List) session.getAttribute("statList");
    request.setAttribute("sList", eList);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Статистика</title>
    <link type="text/css" rel="stylesheet" href="css/application.css">
    <link type="text/css" rel="stylesheet" href="libs/jquery.datetimepicker.css">
    <script src="libs/jquery/jquery-3.3.1.min.js"></script>
    <script src="libs/jquery.datetimepicker.js"></script>
    <script>
        var webSocket = new WebSocket('ws://'+ ${pageContext.request.contextPath} + '/statistic');
        webSocket.onmessage = function (evt) {
            $("#stat").empty();
            var resultData = JSON.parse(evt.data);
            var len = resultData.length;
            var txt = "";
            if (len > 0) {
                for (var i = 0; i < len; i++) {
                    txt += "<tr><td>" + resultData[i].pageName + "</td><td>"
                        + resultData[i].ipAddress + "</td><td>"
                        + resultData[i].browserInfo + "</td><td>"
                        + resultData[i].visitDate.day + "-" +
                        resultData[i].visitDate.month + "-" +
                        resultData[i].visitDate.year + "</td><td>"
                        + resultData[i].cookies + "</td><td>"
                        + resultData[i].requestedParams + "</td></tr>";
                }
                $("#stat").append(txt);
            }
        };
    </script>
</head>
<body>

<div class="back">
    <div class="before-back">
        <%@include file="html/common/header.jsp" %>
        <%@include file="html/common/under-header-menu.jsp" %>
        <div class="container">
            <div class="center-block">
                <span class="description-block">Статистика:</span>
                <table id="stat">
                    <c:forEach items="${sList}" var="stat">
                        <tr>
                            <td>Page Name: <c:out value="${stat.pageName}"/></td>
                            <td>IP address: <c:out value="${stat.ipAddress}"/></td>
                            <td>Browser: <c:out value="${stat.browserInfo}"/></td>
                            <td>Date: <c:out value="${stat.visitDate}"/></td>
                            <td>Cookies: <c:out value="${stat.cookies}"/></td>
                            <td>Request data: <c:out value="${stat.requestedParams}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</div>

<%@include file="html/common/footer.jsp" %>

<div hidden>
    <stat:stat pageName="statPage.jsp"/>
</div>

</body>
</html>
