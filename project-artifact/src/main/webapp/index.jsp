<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stat" uri="/stattags" %>
<html>
<head>
    <style>
        table, th, td {
            border: 1px solid black;
            border-collapse: collapse;
        }
    </style>
</head>
<body>
<table>
    <caption><h4>Servlets</h4></caption>
    <tr>
        <td><h3>DB servlets</h3></td>
        <td><h3>Xml and Json servlets</h3></td>
        <td><h3>HTML, CSS, JS</h3></td>
        <td><h3>GWT</h3></td>
        <td><h3>SOAP</h3></td>
    </tr>
    <tr>
        <td><h2>/fillTable</h2></td>
        <td><h2>/marshalEmployees</h2></td>
        <td><h2><a href="html/main.html">Go to web-page</a></h2></td>
        <td><h2><a href="Project.jsp">Go to GWT-project</a></h2></td>
        <td><h2><a href="calculateProfitTax.jsp">Calculate profit tax</a></h2></td>
    </tr>
    <tr>
        <td><h2>/cleanTable</h2></td>
        <td><h2>/xPathEmployeeSearching</h2></td>
        <td><h2></h2></td>
        <td><h2></h2></td>
        <td><h2><a href="${pageContext.request.contextPath}/salaryCalculatorMax">Get max salary among employees</a></h2></td>
    </tr>
    <tr>
        <td><h2>/getEmployees</h2></td>
        <td><h2>/xmlToJsonConvert</h2></td>
        <td><h2></h2></td>
        <td><h2></h2></td>
        <td><h2><a href="${pageContext.request.contextPath}/salaryCalculatorAvg">Calculate employees's average salary</a></h2></td>
    </tr>
    <tr>
        <td><h2>/changeLastNameAndPosition</h2></td>
        <td><h2>/jsonToObjectModel</h2></td>
        <td><h2></h2></td>
        <td><h2></h2></td>
        <td><h2><a href="${pageContext.request.contextPath}/bankZScoreClient">Get Bank's Z-Score by years</a></h2></td>
    </tr>
    <tr>
        <td><h2>/deleteThreeEmployees</h2></td>
        <td><h2></h2></td>
        <td><h2></h2></td>
        <td><h2></h2></td>
        <td><h2><a href="${pageContext.request.contextPath}/broadEffExRateClient">Get Broad Effective Exchange Rate for Russia</a></h2></td>
    </tr>
    <tr>
        <td><h2>/getEmployeesWithMaxSalary</h2></td>
    </tr>
</table>

<div hidden>
<stat:stat pageName="index.jsp"/>
</div>

<%
    Cookie[] cookies = request.getCookies();
    boolean isVersionOk = true;
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("BROWSER_OK")) {
                isVersionOk = Boolean.parseBoolean(cookie.getValue());
                break;
            }
        }
    }
    if (!isVersionOk) {
%>
<%@include file="oldBrowserVersion.jsp" %>
<%
    }
%>

</body>
</html>
