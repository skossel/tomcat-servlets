<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Tax Calculator</title>
</head>
<body>
    <h1>Tax Calculator</h1>

    <%
        Object income = request.getParameter("income");
        if (income == null) {
    %>
        <p>Provide an income via url params.</p>
        <p>Example: ?income=5000</p>
    <%
        } else {
    %>
        <p>Income: CHF ${income}</p>
        <p>Tax (15%): CHF ${tax}</p>
        <p>Net Income: CHF ${netIncome}</p>
    <%
        }
    %>

    <a href="../stock-market">Back to Stock Market</a>
</body>
</html>
