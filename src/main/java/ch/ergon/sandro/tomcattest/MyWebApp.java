package ch.ergon.sandro.tomcattest;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.*;
import java.util.stream.Collectors;

@WebServlet(name = "myWebApp", urlPatterns = {"/*"})
public class MyWebApp extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        System.out.println("Servlet MyWebApp was accessed from: " + request.getServletPath() + " with path info: " + pathInfo);

        String fileName = getFileName(pathInfo);

        String content = readFileContent(fileName);
        response.getWriter().println(content + " llb banking");
    }

    private String getFileName(String pathInfo) {
        String name = pathInfo.substring(1);
        String reversed = new StringBuilder(name).reverse().toString();
        reversed += ".dso";
        return reversed;
    }

    private String readFileContent(String fileName) throws IOException {
        InputStream inputStream = getServletContext().getResourceAsStream("/" + fileName);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}
