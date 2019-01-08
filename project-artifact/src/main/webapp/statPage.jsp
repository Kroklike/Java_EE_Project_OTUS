<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="stat" uri="/stattags" %>
<%@ page import="java.util.List"%>
<% List eList = (List)session.getAttribute("statList");
    request.setAttribute("sList", eList);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Статистика</title>
    <link type="text/css" rel="stylesheet" href="css/application.css">
</head>
<body>

<div class="back">
    <div class="before-back">
        <%@include file="html/common/header.jsp" %>
        <%@include file="html/common/under-header-menu.jsp" %>
        <div class="container">
            <div class="center-block">
                <span class="description-block">Статистика:</span>
                <table>
                    <c:forEach items="${sList}" var="stat">
                        <tr>
                            <td>Page Name: <c:out value="${stat.pageName}"/></td>
                            <td>IP address: <c:out value="${stat.ipAddress}"/></td>
                            <td>Browser: <c:out value="${stat.browser}"/></td>
                            <td>Date: <c:out value="${stat.date}"/></td>
                            <td>Cookies: <c:out value="${stat.cookies}"/></td>
                            <td>Request data: <c:out value="${stat.requestData}"/></td>
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
