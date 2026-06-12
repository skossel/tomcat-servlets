package ch.ergon.sandro.tomcattest;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "taxCalculator", value = Routes.TAX_CALCULATOR)
public class TaxCalculator extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        String incomeParam = request.getParameter("income");

        if (incomeParam != null) {
            double income = Double.parseDouble(incomeParam);

            double taxRate = 0.15;
            double tax = income * taxRate;
            double netIncome = income - tax;

            request.setAttribute("income", income);
            request.setAttribute("tax", tax);
            request.setAttribute("netIncome", netIncome);
        }

        request.getRequestDispatcher("/tax-calculator.jsp").forward(request, response);
    }

}
