<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="stat" uri="/stattags" %>
<%@ page import="java.util.List"%>
<% List eList = (List)session.getAttribute("empList");
    request.setAttribute("eList", eList);
%>
<html>
<head>
    <meta charset="UTF-8">
    <title>Результат поиска</title>
    <link type="text/css" rel="stylesheet" href="css/application.css">
</head>
<body>

<div class="back">
    <div class="before-back">
        <%@include file="html/common/header.jsp" %>
        <%@include file="html/common/under-header-menu.jsp" %>
        <div class="container">
            <div class="center-block">
                <span class="description-block">Результаты поиска:</span>
                <table>
                    <c:forEach items="${eList}" var="employee">
                        <tr>
                            <td>First name: <c:out value="${employee.firstName}"/></td>
                            <td>Last name: <c:out value="${employee.lastName}"/></td>
                            <td>Middle name: <c:out value="${employee.middleName}"/></td>
                            <td>Position: <c:out value="${employee.positionEntity.positionName}"/></td>
                            <td>Town: <c:out value="${employee.departmentEntity.city}"/></td>
                            <td>Birthday date: <c:out value="${employee.birthdayDate}"/></td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </div>
    </div>
</div>

<%@include file="html/common/footer.jsp" %>

<div hidden>
    <stat:stat pageName="resultPage.jsp"/>
</div>

</body>
</html>
