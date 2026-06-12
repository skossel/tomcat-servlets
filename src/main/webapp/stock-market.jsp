<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Stock Market</title>
</head>
<body>
    <h1>Stock Market</h1>

    <button onclick='location.reload()'>Refresh Market</button>

    <hr>

    <h2>SpaceX</h2>
    <p>Price: CHF ${spacex}</p>
    <p>Change: ${spacexChange}%</p>
    <button onclick="alert('Bought SpaceX stock!')">Buy</button>

    <hr>

    <h2>Ergon</h2>
    <p>Price: CHF ${ergon}</p>
    <p>Change: ${ergonChange}%</p>
    <button onclick="alert('Bought Ergon stock!')">Buy</button>

    <hr>

    <a href="stock-market/my-portfolio">View my own portfolio</a>

    <a href="stock-market/tax-calculator">tax calculator</a>

</body>
</html>
