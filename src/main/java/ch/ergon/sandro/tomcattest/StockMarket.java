package ch.ergon.sandro.tomcattest;

import java.io.*;
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "stock-market", value = Routes.STOCK_MARKET)
public class StockMarket extends HttpServlet {
    private final Random random = new Random();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        double spacex = random.nextInt(150);
        double ergon = random.nextInt(350);

        int spacexChange = random.nextInt(21);
        int ergonChange = random.nextInt(21);

        request.setAttribute("spacex", spacex);
        request.setAttribute("ergon", ergon);
        request.setAttribute("spacexChange", spacexChange);
        request.setAttribute("ergonChange", ergonChange);

        request.getRequestDispatcher("/stock-market.jsp").forward(request, response);
    }
}